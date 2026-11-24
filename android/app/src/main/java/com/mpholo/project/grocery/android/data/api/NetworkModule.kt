package com.mpholo.project.grocery.android.data.api

import com.mpholo.project.grocery.android.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Tiny service locator that builds the Retrofit-backed [GroceryApi].
 * Kept dependency-free (no Hilt) to stay simple.
 */
object NetworkModule {

    val groceryApi: GroceryApi by lazy { createApi(BuildConfig.BASE_URL) }

    fun createApi(baseUrl: String): GroceryApi {
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GroceryApi::class.java)
    }
}
