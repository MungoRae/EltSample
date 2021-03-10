package uk.me.mungorae.eltinterview

import android.app.Application
import timber.log.Timber
import uk.me.mungorae.eltinterview.di.AppComponent
import uk.me.mungorae.eltinterview.di.AppModule
import uk.me.mungorae.eltinterview.di.DaggerAppComponent

open class App: Application() {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        appComponent = createAppComponent()
    }

    fun appComponent() = appComponent

    open fun createAppComponent(): AppComponent {
        return DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}