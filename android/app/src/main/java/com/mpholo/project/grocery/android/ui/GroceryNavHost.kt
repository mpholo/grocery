package com.mpholo.project.grocery.android.ui

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mpholo.project.grocery.android.ui.items.ItemsScreen
import com.mpholo.project.grocery.android.ui.months.MonthsScreen
import com.mpholo.project.grocery.android.ui.products.ProductsScreen

private object Routes {
    const val MONTHS = "months"
    const val PRODUCTS = "products"
    const val ITEMS = "items/{monthlyGroceryId}?title={title}&budget={budget}"
    fun items(monthlyGroceryId: Int, title: String, budget: Double) =
        "items/$monthlyGroceryId?title=${Uri.encode(title)}&budget=$budget"
}

@Composable
fun GroceryNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.MONTHS) {
        composable(Routes.MONTHS) {
            MonthsScreen(
                onMonthClick = { month ->
                    val title = month.period ?: "Month #${month.monthlyGroceryId}"
                    navController.navigate(Routes.items(month.monthlyGroceryId, title, month.budgetAmount))
                },
                onProductsClick = { navController.navigate(Routes.PRODUCTS) }
            )
        }

        composable(Routes.PRODUCTS) {
            ProductsScreen(onBack = { navController.popBackStack() })
        }
        composable(
            route = Routes.ITEMS,
            arguments = listOf(
                navArgument("monthlyGroceryId") { type = NavType.IntType },
                navArgument("title") {
                    type = NavType.StringType
                    defaultValue = "Items"
                },
                navArgument("budget") {
                    type = NavType.StringType
                    defaultValue = "0"
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("monthlyGroceryId") ?: 0
            val title = backStackEntry.arguments?.getString("title") ?: "Items"
            val budget = backStackEntry.arguments?.getString("budget")?.toDoubleOrNull() ?: 0.0
            ItemsScreen(
                monthlyGroceryId = id,
                title = title,
                budget = budget,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
