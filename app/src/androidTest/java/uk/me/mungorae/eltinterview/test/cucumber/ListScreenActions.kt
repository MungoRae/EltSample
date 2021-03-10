package uk.me.mungorae.eltinterview.test.cucumber

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matchers
import uk.me.mungorae.eltinterview.MainActivity
import uk.me.mungorae.eltinterview.R

class ListScreenActions {
    var activityScenario: ActivityScenario<MainActivity>? = null

    fun startScreen() {
        activityScenario = ActivityScenario.launch(MainActivity::class.java)
    }

    fun finishScreen() {
        activityScenario?.onActivity {
            it.finish()
        }
    }

    fun hasLoadingMessage() {
        Espresso.onView(ViewMatchers.withId(R.id.list_progress_loading))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    fun hasNoLoadingMessage() {
        Espresso.onView(ViewMatchers.withId(R.id.list_progress_loading))
            .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))
    }

    fun hasNoItemsMessage() {
        Espresso.onView(ViewMatchers.withId(R.id.list_text_no_items))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    fun hasItemWithName(name: String) {
        Espresso.onView(ViewMatchers.withId(R.id.list_list_view))
            .check(
                ViewAssertions.matches(
                    atPosition(
                        0,
                        ViewMatchers.hasDescendant(ViewMatchers.withText(name))
                    )
                )
            )
    }

    fun hasItemWithDescription(description: String) {
        Espresso.onView(ViewMatchers.withId(R.id.list_list_view))
            .check(
                ViewAssertions.matches(
                    atPosition(
                        0,
                        ViewMatchers.hasDescendant(ViewMatchers.withText(description))
                    )
                )
            )
    }

    fun hasOfflineBanner() {
        Espresso.onView(ViewMatchers.withId(R.id.list_banner_connection))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}