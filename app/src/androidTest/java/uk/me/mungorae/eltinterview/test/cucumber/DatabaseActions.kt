package uk.me.mungorae.eltinterview.test.cucumber

import androidx.test.core.app.ApplicationProvider
import uk.me.mungorae.eltinterview.test.TestApp

class DatabaseActions {
    fun clearTasks() {
        val testApp = ApplicationProvider.getApplicationContext<TestApp>()
        testApp.database.tasksDao().deleteAll().blockingAwait()
    }
}