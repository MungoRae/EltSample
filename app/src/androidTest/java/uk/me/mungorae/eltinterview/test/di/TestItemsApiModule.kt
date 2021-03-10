package uk.me.mungorae.eltinterview.test.di

import dagger.Module
import dagger.Provides
import uk.me.mungorae.eltinterview.api.TasksApi

@Module
class TestItemsApiModule(private val tasksApi: TasksApi) {

    @Provides
    fun provideItemsApi(): TasksApi {
        return tasksApi
    }
}