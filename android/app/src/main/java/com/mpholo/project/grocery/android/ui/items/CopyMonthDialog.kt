package com.mpholo.project.grocery.android.ui.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
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
import java.util.Calendar

private val MONTH_NAMES = listOf(
    "January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
)

/**
 * Dialog to choose a target month + year to copy the current monthly grocery into.
 * Returns a 1-based month and a 4-digit year on confirm.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CopyMonthDialog(
    sourceTitle: String,
    onDismiss: () -> Unit,
    onConfirm: (month: Int, year: Int) -> Unit
) {
    val now = remember { Calendar.getInstance() }
    var monthIndex by remember { mutableStateOf(now.get(Calendar.MONTH)) } // 0-based
    var yearText by remember { mutableStateOf((now.get(Calendar.YEAR)).toString()) }
    var expanded by remember { mutableStateOf(false) }

    val year = yearText.toIntOrNull()
    val valid = year != null && year in 2000..2100

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Copy \"$sourceTitle\" to…") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = MONTH_NAMES[monthIndex],
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Month") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        MONTH_NAMES.forEachIndexed { index, name ->
                            DropdownMenuItem(
                                text = { Text(name) },
                                onClick = {
                                    monthIndex = index
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = yearText,
                    onValueChange = { yearText = it.filter { c -> c.isDigit() }.take(4) },
                    label = { Text("Year") },
                    singleLine = true,
                    isError = yearText.isNotEmpty() && !valid,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    "Items are copied with their quantities and prices, and reset to not-in-basket.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (valid) onConfirm(monthIndex + 1, year!!) },
                enabled = valid
            ) { Text("Copy") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
