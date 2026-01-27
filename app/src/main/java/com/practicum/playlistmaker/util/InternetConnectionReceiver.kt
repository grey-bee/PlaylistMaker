package com.practicum.playlistmaker.util

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.annotation.RequiresPermission
import com.practicum.playlistmaker.R

class InternetConnectionReceiver : BroadcastReceiver() {
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.net.conn.CONNECTIVITY_CHANGE") {
            val conManager =
                context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities = conManager.getNetworkCapabilities(conManager.activeNetwork)
            val isConnected =
                capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            if (isConnected != true) {
                val text = context.getString(R.string.no_connection)
                Toast.makeText(context, text, Toast.LENGTH_LONG).show()
            }
        }
    }
}