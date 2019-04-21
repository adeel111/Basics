package com.example.adeeliftikhar.messenger.Internet

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo

object CheckInternetConnectivity {

    fun isConnected(context: Context): Boolean {
        //        Used to check Internet Connection...

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val networkInfo = connectivityManager.allNetworkInfo
            if (networkInfo != null) {
                for (i in networkInfo.indices) {
                    if (networkInfo[i].state == NetworkInfo.State.CONNECTED) {
                        return true
                    }
                }
            }
        }
        return false
    }
}