package uk.me.mungorae.eltinterview.test.api

import uk.me.mungorae.eltinterview.api.InternetConnection

class FakeInternetConnection: InternetConnection {
    private var offline = false

    fun setOffline(offline: Boolean = true) {
        this.offline = offline
    }

    override fun hasInternet(): Boolean {
        return !offline
    }
}