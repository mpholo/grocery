package com.mpholo.project.grocery.android.data.api

import com.mpholo.project.grocery.android.data.model.CopyMonthRequest
import com.mpholo.project.grocery.android.data.model.CreateGroceryItemRequest
import com.mpholo.project.grocery.android.data.model.CreateProductRequest
import com.mpholo.project.grocery.android.data.model.GroceryItem
import com.mpholo.project.grocery.android.data.model.GroceryItemPatchRequest
import com.mpholo.project.grocery.android.data.model.MonthSummary
import com.mpholo.project.grocery.android.data.model.Product
import com.mpholo.project.grocery.android.data.model.ProductListResponse
import com.mpholo.project.grocery.android.data.model.UpdateBudgetRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/** Retrofit description of the grocery backend's JSON API. */
interface GroceryApi {

    @GET("api/v1/monthlygroceries/summaries")
    suspend fun getMonths(): List<MonthSummary>

    @GET("api/v1/groceryitems")
    suspend fun getItems(@Query("monthlyGroceryId") monthlyGroceryId: Int): List<GroceryItem>

    @POST("api/v1/groceryitems")
    suspend fun createItem(@Body body: CreateGroceryItemRequest): GroceryItem

    @PATCH("api/v1/groceryitems/{id}")
    suspend fun patchItem(
        @Path("id") groceryItemId: Int,
        @Body body: GroceryItemPatchRequest
    ): GroceryItem

    @DELETE("api/v1/groceryitems/{id}")
    suspend fun deleteItem(@Path("id") groceryItemId: Int): Response<Unit>

    @POST("api/v1/monthlygroceries/{id}/copy")
    suspend fun copyMonth(
        @Path("id") monthlyGroceryId: Int,
        @Body body: CopyMonthRequest
    ): MonthSummary

    @PATCH("api/v1/monthlygroceries/{id}")
    suspend fun updateMonth(
        @Path("id") monthlyGroceryId: Int,
        @Body body: UpdateBudgetRequest
    ): MonthSummary

    @GET("api/v1/products")
    suspend fun getProducts(): ProductListResponse

    @POST("api/v1/products")
    suspend fun createProduct(@Body body: CreateProductRequest): Product

    @DELETE("api/v1/products/{id}")
    suspend fun deleteProduct(@Path("id") productId: Int): Response<Unit>
}
