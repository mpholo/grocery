package com.mpholo.project.grocery.android.data

import android.content.Context
import com.mpholo.project.grocery.android.data.local.LocalStore
import com.mpholo.project.grocery.android.data.sync.ConnectivityObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

/**
 * Process-wide singletons: the local cache, the connectivity observer and the
 * repository. Initialised once from the Application. When the device comes back
 * online, pending offline basket toggles are flushed immediately.
 */
object AppGraph {

    lateinit var store: LocalStore
        private set
    lateinit var connectivity: ConnectivityObserver
        private set
    lateinit var repository: GroceryRepository
        private set

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun init(context: Context) {
        if (::repository.isInitialized) return
        store = LocalStore(context.applicationContext)
        connectivity = ConnectivityObserver(context.applicationContext)
        repository = GroceryRepository()

        // Flush the moment connectivity returns (drop the initial value; react to changes).
        scope.launch {
            connectivity.online.drop(1).collect { online ->
                if (online) repository.syncPending()
            }
        }
        // And attempt once at startup in case there are leftover pending toggles.
        scope.launch { repository.syncPending() }
    }
}
