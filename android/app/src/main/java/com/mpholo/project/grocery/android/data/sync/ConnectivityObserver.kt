package com.mpholo.project.grocery.android.data.sync

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Exposes whether the device currently has validated internet, as a StateFlow
 * that flips to true as soon as a usable network becomes available. Used to
 * trigger a sync the moment the device comes back online.
 */
class ConnectivityObserver(context: Context) {

    private val cm = context.getSystemService(ConnectivityManager::class.java)

    private val _online = MutableStateFlow(computeOnline())
    val online: StateFlow<Boolean> = _online.asStateFlow()

    init {
        cm?.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                _online.value = computeOnline()
            }

            override fun onLost(network: Network) {
                _online.value = computeOnline()
            }

            override fun onCapabilitiesChanged(network: Network, caps: NetworkCapabilities) {
                _online.value = computeOnline()
            }
        })
    }

    fun isOnline(): Boolean = computeOnline()

    private fun computeOnline(): Boolean {
        val network = cm?.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}
