package uk.me.mungorae.eltinterview.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import uk.me.mungorae.eltinterview.api.RetrofitTasksApi
import uk.me.mungorae.eltinterview.api.Task
import uk.me.mungorae.eltinterview.api.TasksApi
import javax.inject.Singleton

@Module
open class TasksApiModule {

    @Singleton
    @Provides
    open fun provideItemsApi(retrofitTasksApi: RetrofitTasksApi): TasksApi {
        return TasksApi.Retrofit(retrofitTasksApi)
    }
}