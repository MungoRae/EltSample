package uk.me.mungorae.eltinterview.test.cucumber

import androidx.test.core.app.ApplicationProvider
import uk.me.mungorae.eltinterview.test.TestApp
import uk.me.mungorae.eltinterview.test.api.FakeInternetConnection

class ConnectionActions {
    private var connection: FakeInternetConnection? = null

    fun setupConnection() {
        val testApp = ApplicationProvider.getApplicationContext<TestApp>()
        connection = testApp.connection
    }

    fun setOffline() {
        connection?.setOffline()
    }
}