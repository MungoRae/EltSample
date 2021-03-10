package uk.me.mungorae.eltinterview.test.cucumber

import io.cucumber.java.After
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import uk.me.mungorae.eltinterview.api.Task
import uk.me.mungorae.eltinterview.api.Type

class Steps {

    private val app = AppActions()

    @Before
    fun before() {
        app.startApp()
    }

    @After
    fun after() {
        app.listScreenActions.finishScreen()
    }

    @When("the list screen is started")
    fun the_list_screen_is_started() {
        app.listScreenActions.startScreen()
    }

    @Then("I will see that the app is loading data")
    fun i_will_see_that_the_app_is_loading_data() {
        app.listScreenActions.hasLoadingMessage()
    }

    @Given("no tasks stored locally")
    fun no_tasks_stored_locally() {
        app.databaseActions.clearTasks()
    }

    @When("the server returns no tasks")
    fun the_server_returns_no_items() {
        app.itemsApiActions.returnTasks(emptyList())
    }

    @Then("I will see a message telling me there are no tasks")
    fun i_will_see_a_message_telling_me_there_are_no_items() {
        app.listScreenActions.hasNoItemsMessage()
    }

    @Then("the loading message has gone")
    fun the_loading_message_has_gone() {
        app.listScreenActions.hasNoLoadingMessage()
    }

    @When("the server returns a task with name {string}, description {string}, and type {string}")
    fun when_the_server_returns_with(name: String, description: String, type: String) {
        app.itemsApiActions
            .returnTasks(listOf(Task(0, name, description, Type.valueOf(type.toUpperCase()))))
    }

    @Then("I will see the name {string}")
    fun i_will_see_the_name(name: String) {
        app.listScreenActions.hasItemWithName(name)
    }

    @Then("I will see the description {string}")
    fun i_will_see_the_description(description: String) {
        app.listScreenActions.hasItemWithDescription(description)
    }
    
    @Given("the app is offline")
    fun the_app_is_offline() {
        app.connectionActions.setOffline()
    }
    
    @Then("I will see an offline banner telling me the app is offline")
    fun i_will_see_an_offline_banner_telling_me_the_app_is_offline() {
        app.listScreenActions.hasOfflineBanner()
    }
}