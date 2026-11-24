package com.mpholo.project.grocery.android

import android.app.Application
import com.mpholo.project.grocery.android.data.AppGraph

class GroceryApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppGraph.init(this)
    }
}
