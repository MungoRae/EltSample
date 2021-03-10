package uk.me.mungorae.eltinterview.api

import android.content.Context
import android.net.ConnectivityManager

interface InternetConnection {
    fun hasInternet(): Boolean

    /*
     * I know there are new Apis for Android 10 but for brevity of this exercise
     * I am going to use what I know. They still work.
     */
    @Suppress("DEPRECATION")
    class Default(
        context: Context
    ) : InternetConnection {

        private val connectivityManager: ConnectivityManager = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        override fun hasInternet(): Boolean {
            return connectivityManager.activeNetworkInfo
                .let { it?.isConnectedOrConnecting == true }
        }
    }
}