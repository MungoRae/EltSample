package uk.me.mungorae.eltinterview.test.cucumber

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.matcher.ViewMatchers
import uk.me.mungorae.eltinterview.api.Task
import uk.me.mungorae.eltinterview.test.TestApp
import uk.me.mungorae.eltinterview.test.api.FakeTasksApi

class ItemsApiActions {
    private var tasksApi: FakeTasksApi? = null

    fun setupServer() {
        val testApp = ApplicationProvider.getApplicationContext<TestApp>()
        tasksApi = testApp.tasksApi
    }

    fun returnTasks(tasks: List<Task> = emptyList()) {
        tasksApi?.tasks?.clear()
        tasksApi?.tasks?.addAll(tasks)
        Espresso.onView(ViewMatchers.isRoot()).perform(waitFor(500))
    }
}