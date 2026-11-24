package com.mpholo.project.grocery.android.ui.items

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

/**
 * Dialog for changing a month's budget amount.
 * Save is enabled only for a non-negative number.
 */
@Composable
fun EditBudgetDialog(
    currentBudget: Double,
    onDismiss: () -> Unit,
    onConfirm: (budget: Double) -> Unit
) {
    // Show a whole number when the budget has no fractional part (e.g. 7500 not 7500.0).
    var budgetText by remember {
        mutableStateOf(
            if (currentBudget % 1.0 == 0.0) currentBudget.toLong().toString()
            else currentBudget.toString()
        )
    }

    val budget = budgetText.toDoubleOrNull()
    val valid = budget != null && budget >= 0.0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit budget") },
        text = {
            OutlinedTextField(
                value = budgetText,
                onValueChange = { budgetText = it },
                label = { Text("Budget amount") },
                singleLine = true,
                isError = budgetText.isNotEmpty() && !valid,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
        },
        confirmButton = {
            TextButton(
                onClick = { if (valid) onConfirm(budget!!) },
                enabled = valid
            ) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
