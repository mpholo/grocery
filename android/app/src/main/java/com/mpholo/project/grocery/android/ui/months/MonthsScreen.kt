package com.mpholo.project.grocery.android.ui.months

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mpholo.project.grocery.android.data.model.MonthSummary
import com.mpholo.project.grocery.android.ui.StateContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthsScreen(
    onMonthClick: (MonthSummary) -> Unit,
    onProductsClick: () -> Unit,
    viewModel: MonthsViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    // null means "no explicit choice yet" -> fall back to the current year below.
    var selectedYear by rememberSaveable { mutableStateOf<String?>(null) }
    val currentYear = remember {
        java.util.Calendar.getInstance().get(java.util.Calendar.YEAR).toString()
    }

    // Refresh the months list every time the screen returns to the foreground
    // (e.g. after copying a month on the items screen, or re-opening the app),
    // so newly created months appear without restarting.
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) viewModel.load()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Monthly Groceries", fontWeight = FontWeight.SemiBold) },
                actions = {
                    IconButton(onClick = onProductsClick) {
                        Icon(Icons.Filled.Inventory2, contentDescription = "Products")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { padding ->
        StateContent(
            state = state,
            onRetry = viewModel::load,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) { months ->
            val years = months.mapNotNull { it.year() }.distinct().sortedDescending()
            // Default to the current year; fall back to the most recent year with data.
            val activeYear = selectedYear?.takeIf { years.contains(it) }
                ?: currentYear.takeIf { years.contains(it) }
                ?: years.firstOrNull()
            // Only the selected year, earliest month first (Jan → Dec).
            val visible = months
                .filter { activeYear != null && it.year() == activeYear }
                .sortedBy { it.startDate ?: "" }

            Column(Modifier.fillMaxSize()) {
                if (years.isNotEmpty()) {
                    YearFilter(
                        years = years,
                        selected = activeYear,
                        onSelect = { selectedYear = it }
                    )
                }

                if (visible.isEmpty()) {
                    Text(
                        "No monthly groceries for this year.",
                        modifier = Modifier.padding(24.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(visible, key = { it.monthlyGroceryId }) { month ->
                            MonthCard(month = month, onClick = { onMonthClick(month) })
                        }
                    }
                }
            }
        }
    }
}

/** Year parsed from the ISO startDate ("yyyy-MM-dd"). */
private fun MonthSummary.year(): String? = startDate?.takeIf { it.length >= 4 }?.substring(0, 4)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun YearFilter(years: List<String>, selected: String?, onSelect: (String) -> Unit) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(years) { year ->
            FilterChip(
                selected = selected == year,
                onClick = { onSelect(year) },
                label = { Text(year) }
            )
        }
    }
}

@Composable
private fun MonthCard(month: MonthSummary, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        // Visible edge on the dark background (surface == background there).
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = month.period ?: "Month #${month.monthlyGroceryId}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                val from = month.startDate
                val to = month.endDate
                if (from != null || to != null) {
                    Text(
                        text = "${from ?: "?"} → ${to ?: "?"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                val remaining = month.budgetAmount - month.spent
                val overBudget = remaining < 0
                Text(
                    text = if (overBudget) "Over by R %.2f".format(-remaining)
                    else "R %.2f left".format(remaining),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (overBudget) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Surface(
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Text(
                    text = "R %.0f".format(month.budgetAmount),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }
        }
    }
}
