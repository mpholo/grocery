package com.mpholo.project.grocery.android.ui.products

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mpholo.project.grocery.android.data.model.Product

/**
 * Dialog to edit an existing product's name, description and price.
 * Prefilled from [product]; same validation as adding (name 3-50 chars,
 * price non-negative).
 */
@Composable
fun EditProductDialog(
    product: Product,
    onDismiss: () -> Unit,
    onConfirm: (name: String, description: String?, price: Double) -> Unit
) {
    var name by remember { mutableStateOf(product.productName) }
    var description by remember { mutableStateOf(product.productDescription ?: "") }
    var priceText by remember {
        mutableStateOf(
            if (product.productPrice % 1.0 == 0.0) product.productPrice.toLong().toString()
            else product.productPrice.toString()
        )
    }

    val nameValid = name.trim().length in 3..50
    val price = priceText.toDoubleOrNull()
    val priceValid = price != null && price >= 0.0
    val valid = nameValid && priceValid

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit product") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true,
                    isError = name.isNotEmpty() && !nameValid,
                    supportingText = { Text("3-50 characters") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = priceText,
                    onValueChange = { priceText = it },
                    label = { Text("Price") },
                    singleLine = true,
                    isError = priceText.isNotEmpty() && !priceValid,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (valid) onConfirm(name, description, price!!) },
                enabled = valid
            ) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
