package com.mpholo.project.grocery.android.data.model

/**
 * Flat month summary as returned by
 * `GET /api/v1/monthlygroceries/summaries`.
 * Dates are kept as ISO strings ("yyyy-MM-dd") to avoid pulling in a
 * date adapter; the UI only displays them.
 */
data class MonthSummary(
    val monthlyGroceryId: Int,
    val budgetAmount: Double,
    val period: String?,
    val startDate: String?,
    val endDate: String?,
    val spent: Double = 0.0   // total of in-basket items; remaining = budget - spent
)

/**
 * A grocery item as returned by the API
 * (`GET /api/v1/groceryitems?monthlyGroceryId=...`).
 */
data class GroceryItem(
    val groceryItemId: Int,
    val quantity: Int,
    val actualPrice: Double,
    val purchased: Boolean,
    val totalPrice: Double,
    val productId: Int?,
    val productName: String?,
    val monthlyGroceryId: Int,
    val period: String?
)

/**
 * Partial-update body for `PATCH /api/v1/groceryitems/{id}`.
 * Only non-null fields are sent/applied, so quantity, price and the
 * "added to basket" (purchased) flag can be changed independently.
 */
data class GroceryItemPatchRequest(
    val quantity: Int? = null,
    val actualPrice: Double? = null,
    val purchased: Boolean? = null
)

/**
 * Body for copying a monthly grocery into a target month/year
 * (`POST /api/v1/monthlygroceries/{id}/copy`). `budgetAmount` null reuses the
 * source month's budget. Copied items are reset to not-in-basket server-side.
 */
data class CopyMonthRequest(
    val month: Int,   // 1-12
    val year: Int,
    val budgetAmount: Double? = null
)

/**
 * Body for updating a month via `PATCH /api/v1/monthlygroceries/{id}`.
 * Currently only the budget is editable.
 */
data class UpdateBudgetRequest(
    val budgetAmount: Double
)

/**
 * Body for adding a product to a month as a new grocery item
 * (`POST /api/v1/groceryitems`).
 */
data class CreateGroceryItemRequest(
    val monthlyGroceryId: Int,
    val productId: Int,
    val quantity: Int = 1,
    val actualPrice: Double
)

/** A product in the catalog (`GET /api/v1/products`). */
data class Product(
    val productId: Int?,
    val productName: String,
    val productDescription: String?,
    val productPrice: Double
)

/** Wrapper the products endpoint returns: {"products":[...]}. */
data class ProductListResponse(
    val products: List<Product> = emptyList()
)

/** Body for creating a product (`POST /api/v1/products`). */
data class CreateProductRequest(
    val productName: String,
    val productDescription: String?,
    val productPrice: Double
)
