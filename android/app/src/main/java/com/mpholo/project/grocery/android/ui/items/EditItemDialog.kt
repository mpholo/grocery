package com.mpholo.project.grocery.android.ui.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mpholo.project.grocery.android.data.model.GroceryItem

/**
 * Dialog for changing an item's quantity and unit price.
 * Validates that quantity is a positive integer and price a non-negative number
 * before enabling Save.
 */
@Composable
fun EditItemDialog(
    item: GroceryItem,
    onDismiss: () -> Unit,
    onConfirm: (quantity: Int, actualPrice: Double) -> Unit
) {
    var quantityText by remember { mutableStateOf(item.quantity.toString()) }
    var priceText by remember { mutableStateOf(item.actualPrice.toString()) }

    val quantity = quantityText.toIntOrNull()
    val price = priceText.toDoubleOrNull()
    val valid = quantity != null && quantity > 0 && price != null && price >= 0.0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(item.productName ?: "Edit item") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = quantityText,
                    onValueChange = { quantityText = it },
                    label = { Text("Quantity") },
                    singleLine = true,
                    isError = quantityText.isNotEmpty() && (quantity == null || quantity <= 0),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = priceText,
                    onValueChange = { priceText = it },
                    label = { Text("Unit price") },
                    singleLine = true,
                    isError = priceText.isNotEmpty() && (price == null || price < 0.0),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (valid) onConfirm(quantity!!, price!!) },
                enabled = valid
            ) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
