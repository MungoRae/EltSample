package uk.me.mungorae.eltinterview.test

import uk.me.mungorae.eltinterview.App
import uk.me.mungorae.eltinterview.database.AppDatabase
import uk.me.mungorae.eltinterview.di.AppComponent
import uk.me.mungorae.eltinterview.di.AppModule
import uk.me.mungorae.eltinterview.test.api.FakeInternetConnection
import uk.me.mungorae.eltinterview.test.api.FakeTasksApi
import uk.me.mungorae.eltinterview.test.di.DaggerTestAppComponent
import uk.me.mungorae.eltinterview.test.di.TestAppComponent
import uk.me.mungorae.eltinterview.test.di.TestConnectionModule
import uk.me.mungorae.eltinterview.test.di.TestItemsApiModule
import javax.inject.Inject

class TestApp : App() {
    val tasksApi = FakeTasksApi()
    val connection = FakeInternetConnection()

    @Inject
    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()

        (appComponent() as TestAppComponent).inject(this)
    }

    override fun createAppComponent(): AppComponent {
        return DaggerTestAppComponent.builder()
            .appModule(AppModule(this))
            .testItemsApiModule(TestItemsApiModule(tasksApi))
            .testConnectionModule(TestConnectionModule(connection))
            .build()
    }
}