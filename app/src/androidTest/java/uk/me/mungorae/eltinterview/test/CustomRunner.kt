package uk.me.mungorae.eltinterview.test

import android.app.Application
import android.app.Instrumentation
import android.content.Context
import io.cucumber.android.runner.CucumberAndroidJUnitRunner

/**
 * Used to replace the application class with a custom one for tests.
 * This gives me better access to the dependency tree for faking things like
 * the api calls.
 */
class CustomRunner: CucumberAndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return Instrumentation.newApplication(TestApp::class.java, context)
    }
}