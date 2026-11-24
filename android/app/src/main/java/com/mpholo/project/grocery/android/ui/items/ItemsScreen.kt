package com.mpholo.project.grocery.android.ui.items

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mpholo.project.grocery.android.data.model.GroceryItem
import com.mpholo.project.grocery.android.ui.StateContent
import com.mpholo.project.grocery.android.ui.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsScreen(
    monthlyGroceryId: Int,
    title: String,
    budget: Double,
    onBack: () -> Unit,
    viewModel: ItemsViewModel = viewModel(
        factory = ItemsViewModel.provideFactory(monthlyGroceryId, budget)
    )
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var editing by remember { mutableStateOf<GroceryItem?>(null) }
    var showCopyDialog by remember { mutableStateOf(false) }
    var editingBudget by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    var searchActive by remember { mutableStateOf(false) }
    var itemQuery by remember { mutableStateOf("") }

    LaunchedEffect(state.message) {
        state.message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.consumeMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        searchActive = !searchActive
                        if (!searchActive) itemQuery = ""
                    }) {
                        Icon(
                            if (searchActive) Icons.Filled.Close else Icons.Filled.Search,
                            contentDescription = if (searchActive) "Close search" else "Search items"
                        )
                    }
                    IconButton(
                        onClick = {
                            viewModel.loadProducts()
                            showAddDialog = true
                        },
                        enabled = !state.addingItem
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add product")
                    }
                    IconButton(onClick = { showCopyDialog = true }, enabled = !state.copying) {
                        Icon(Icons.Filled.ContentCopy, contentDescription = "Copy to another month")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        val uiState = when {
            state.loading -> UiState.Loading
            state.error != null -> UiState.Error(state.error!!)
            else -> UiState.Success(state.items)
        }

        StateContent(
            state = uiState,
            onRetry = viewModel::load,
            modifier = Modifier.fillMaxSize().padding(padding)
        ) { itemList ->
            val filtered = if (itemQuery.isBlank()) itemList
            else itemList.filter {
                (it.productName ?: "").contains(itemQuery.trim(), ignoreCase = true)
            }
            val inBasket = filtered.filter { it.purchased }
            val toBuy = filtered.filterNot { it.purchased }

            Column(Modifier.fillMaxSize()) {
                if (searchActive) {
                    OutlinedTextField(
                        value = itemQuery,
                        onValueChange = { itemQuery = it },
                        label = { Text("Search items") },
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                SummaryCard(
                    purchased = state.purchasedCount,
                    total = itemList.size,
                    budget = state.budget,
                    basketTotal = state.basketTotal,
                    plannedTotal = state.plannedTotal,
                    updatingBudget = state.updatingBudget,
                    onEditBudget = { editingBudget = true }
                )

                if (filtered.isEmpty()) {
                    Text(
                        if (itemList.isEmpty()) "No items in this month."
                        else "No items match \"$itemQuery\".",
                        modifier = Modifier.padding(24.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (inBasket.isNotEmpty()) {
                            item(key = "hdr-basket") {
                                SectionHeader("In basket", inBasket.size, Modifier.animateItem())
                            }
                            items(inBasket, key = { it.groceryItemId }) { item ->
                                ItemRow(
                                    item = item,
                                    updating = item.groceryItemId in state.updatingIds,
                                    onToggle = { viewModel.togglePurchased(item) },
                                    onEdit = { editing = item },
                                    modifier = Modifier.animateItem()
                                )
                            }
                        }
                        if (toBuy.isNotEmpty()) {
                            item(key = "hdr-tobuy") {
                                SectionHeader("To buy", toBuy.size, Modifier.animateItem())
                            }
                            items(toBuy, key = { it.groceryItemId }) { item ->
                                ItemRow(
                                    item = item,
                                    updating = item.groceryItemId in state.updatingIds,
                                    onToggle = { viewModel.togglePurchased(item) },
                                    onEdit = { editing = item },
                                    modifier = Modifier.animateItem()
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    editing?.let { item ->
        EditItemDialog(
            item = item,
            onDismiss = { editing = null },
            onConfirm = { quantity, price ->
                viewModel.updatePriceAndQuantity(item, quantity, price)
                editing = null
            },
            onDelete = {
                viewModel.deleteItem(item)
                editing = null
            }
        )
    }

    if (showCopyDialog) {
        CopyMonthDialog(
            sourceTitle = title,
            onDismiss = { showCopyDialog = false },
            onConfirm = { month, year ->
                viewModel.copyToMonth(month, year)
                showCopyDialog = false
            }
        )
    }

    if (editingBudget) {
        EditBudgetDialog(
            currentBudget = state.budget,
            onDismiss = { editingBudget = false },
            onConfirm = { newBudget ->
                viewModel.updateBudget(newBudget)
                editingBudget = false
            }
        )
    }

    if (showAddDialog) {
        // Offer only products not already present in this month.
        val existingProductIds = state.items.mapNotNull { it.productId }.toSet()
        AddItemDialog(
            products = state.products.filter {
                it.productId != null && it.productId !in existingProductIds
            },
            loading = state.loadingProducts,
            onDismiss = { showAddDialog = false },
            onPick = { product ->
                viewModel.addItem(product)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun SummaryCard(
    purchased: Int,
    total: Int,
    budget: Double,
    basketTotal: Double,
    plannedTotal: Double,
    updatingBudget: Boolean,
    onEditBudget: () -> Unit
) {
    val remaining = budget - basketTotal
    val overBudget = remaining < 0
    val progress = if (budget > 0) (basketTotal / budget).toFloat().coerceIn(0f, 1f) else 0f
    val accent = if (overBudget) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
    // Bar fades green -> amber -> red as the basket fills: green at empty,
    // amber around half the budget, full red once the basket reaches budget.
    val barColor = if (progress < 0.5f) {
        lerp(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.tertiary,
            progress / 0.5f
        )
    } else {
        lerp(
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.error,
            (progress - 0.5f) / 0.5f
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(Modifier.fillMaxWidth().padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Basket total", style = MaterialTheme.typography.labelLarge)
                if (updatingBudget) {
                    CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    IconButton(onClick = onEditBudget, modifier = Modifier.size(28.dp)) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "Edit budget",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    "%.2f".format(basketTotal),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "of %.0f budget".format(budget),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp).height(8.dp).clip(RoundedCornerShape(6.dp)),
                color = barColor,
                trackColor = MaterialTheme.colorScheme.surface
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("In basket: $purchased / $total", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = if (overBudget) "Over by %.2f".format(-remaining) else "Left %.2f".format(remaining),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = accent
                )
            }
            Text(
                "Planned (all items): %.2f".format(plannedTotal),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
private fun SectionHeader(label: String, count: Int, modifier: Modifier = Modifier) {
    Text(
        text = "$label  ·  $count",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier.padding(top = 8.dp, bottom = 2.dp)
    )
}

@Composable
private fun ItemRow(
    item: GroceryItem,
    updating: Boolean,
    onToggle: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val container by animateColorAsState(
        targetValue = if (item.purchased) MaterialTheme.colorScheme.secondaryContainer
        else MaterialTheme.colorScheme.surface,
        label = "cardColor"
    )
    val onContainer = if (item.purchased) MaterialTheme.colorScheme.onSecondaryContainer
    else MaterialTheme.colorScheme.onSurface

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = container, contentColor = onContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = if (item.purchased) 0.dp else 2.dp),
        // Unchecked cards share the background colour, so give them an outline —
        // otherwise they have no visible edge (most noticeable in dark mode,
        // where elevation shadows barely register). Use the higher-contrast
        // `outline` tone so it reads clearly on the near-black dark background.
        border = if (item.purchased) null
        else BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.purchased,
                onCheckedChange = { if (!updating) onToggle() },
                enabled = !updating,
                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
            )
            Column(Modifier.weight(1f).padding(start = 4.dp)) {
                Text(
                    text = item.productName ?: "Item #${item.groceryItemId}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = if (item.purchased) TextDecoration.LineThrough else null
                )
                Text(
                    text = "Qty ${item.quantity} × %.2f".format(item.actualPrice),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            PricePill(item.actualPrice * item.quantity)

            if (updating) {
                Box(Modifier.size(48.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(Modifier.size(22.dp), strokeWidth = 2.dp)
                }
            } else {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit price and quantity")
                }
            }
        }
    }
}

@Composable
private fun PricePill(total: Double) {
    Surface(
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
    ) {
        Text(
            text = "%.2f".format(total),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}
