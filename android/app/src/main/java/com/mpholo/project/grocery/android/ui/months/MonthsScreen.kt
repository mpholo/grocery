package com.mpholo.project.grocery.android.ui.months

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
    var selectedYear by rememberSaveable { mutableStateOf<String?>(null) }

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
            // Drop a stale selection if that year is no longer present.
            val activeYear = selectedYear?.takeIf { years.contains(it) }
            val visible = if (activeYear == null) months
            else months.filter { it.year() == activeYear }

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
private fun YearFilter(years: List<String>, selected: String?, onSelect: (String?) -> Unit) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selected == null,
                onClick = { onSelect(null) },
                label = { Text("All") }
            )
        }
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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
