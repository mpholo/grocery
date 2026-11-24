package com.mpholo.project.grocery.android.ui.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mpholo.project.grocery.android.data.model.Product

/**
 * Dialog for adding a product from the catalog to the current month.
 * Supports searching by name/description; the list is sorted alphabetically.
 * Tapping a product adds it (quantity 1, at the product's price).
 */
@Composable
fun AddItemDialog(
    products: List<Product>,
    loading: Boolean,
    onDismiss: () -> Unit,
    onPick: (Product) -> Unit
) {
    var query by remember { mutableStateOf("") }

    val visible = remember(products, query) {
        val q = query.trim()
        products
            .filter {
                q.isBlank() ||
                    it.productName.contains(q, ignoreCase = true) ||
                    (it.productDescription?.contains(q, ignoreCase = true) == true)
            }
            .sortedBy { it.productName.lowercase() }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add product") },
        text = {
            Column {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Search products") },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )

                when {
                    loading -> Box(
                        Modifier.fillMaxWidth().padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }

                    visible.isEmpty() -> Text(
                        if (products.isEmpty()) "No products left to add — every product is already in this month."
                        else "No products match \"$query\".",
                        modifier = Modifier.padding(top = 16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    else -> LazyColumn(
                        modifier = Modifier.fillMaxWidth().heightIn(max = 320.dp).padding(top = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        items(visible, key = { it.productId ?: it.productName.hashCode() }) { product ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onPick(product) }
                                    .padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    product.productName,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(end = 12.dp)
                                )
                                Text(
                                    "R %.2f".format(product.productPrice),
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        }
    )
}
