package com.mpholo.project.grocery.android.data.local

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mpholo.project.grocery.android.data.model.GroceryItem
import com.mpholo.project.grocery.android.data.model.MonthSummary

/** A pending offline basket toggle, replayed to the server on reconnect. */
data class ToggleOp(val groceryItemId: Int, val purchased: Boolean)

/**
 * Lightweight on-device cache (SharedPreferences + Gson) so lists are viewable
 * offline, plus a queue of pending basket toggles made while offline.
 */
class LocalStore(context: Context) {

    private val prefs = context.getSharedPreferences("grocery_cache", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val monthsType = object : TypeToken<List<MonthSummary>>() {}.type
    private val itemsType = object : TypeToken<List<GroceryItem>>() {}.type
    private val opsType = object : TypeToken<List<ToggleOp>>() {}.type

    // ---- Months ----
    fun saveMonths(months: List<MonthSummary>) =
        prefs.edit().putString(KEY_MONTHS, gson.toJson(months)).apply()

    fun loadMonths(): List<MonthSummary> =
        prefs.getString(KEY_MONTHS, null)?.let { gson.fromJson(it, monthsType) } ?: emptyList()

    // ---- Items per month ----
    fun saveItems(monthId: Int, items: List<GroceryItem>) =
        prefs.edit().putString(itemsKey(monthId), gson.toJson(items)).apply()

    fun loadItems(monthId: Int): List<GroceryItem> =
        prefs.getString(itemsKey(monthId), null)?.let { gson.fromJson(it, itemsType) } ?: emptyList()

    // ---- Pending toggles (last write wins per item) ----
    fun pendingToggles(): List<ToggleOp> =
        prefs.getString(KEY_OPS, null)?.let { gson.fromJson(it, opsType) } ?: emptyList()

    fun queueToggle(groceryItemId: Int, purchased: Boolean) {
        val map = pendingToggles().associate { it.groceryItemId to it.purchased }.toMutableMap()
        map[groceryItemId] = purchased
        persistOps(map.map { ToggleOp(it.key, it.value) })
    }

    fun clearToggle(groceryItemId: Int) {
        val remaining = pendingToggles().filterNot { it.groceryItemId == groceryItemId }
        persistOps(remaining)
    }

    /** Map of itemId -> desired purchased state, for overlaying on fetched/cached items. */
    fun pendingMap(): Map<Int, Boolean> = pendingToggles().associate { it.groceryItemId to it.purchased }

    private fun persistOps(ops: List<ToggleOp>) =
        prefs.edit().putString(KEY_OPS, gson.toJson(ops)).apply()

    private fun itemsKey(monthId: Int) = "items_$monthId"

    private companion object {
        const val KEY_MONTHS = "months"
        const val KEY_OPS = "pending_toggles"
    }
}
