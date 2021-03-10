package uk.me.mungorae.eltinterview.api

import io.reactivex.Single


interface TasksApi {

    fun getTasks(): Single<List<Task>>

    class Retrofit(private val tasksApi: RetrofitTasksApi): TasksApi {
        override fun getTasks(): Single<List<Task>> {
            return tasksApi.getTasks()
        }
    }
}