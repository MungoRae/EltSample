package uk.me.mungorae.eltinterview.test.api

import io.reactivex.Single
import uk.me.mungorae.eltinterview.api.Task
import uk.me.mungorae.eltinterview.api.TasksApi
import uk.me.mungorae.eltinterview.api.Type
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

class FakeTasksApi : TasksApi {
    val tasks = mutableListOf<Task>()

    override fun getTasks(): Single<List<Task>> {
        return Single.timer(400, TimeUnit.MILLISECONDS)
            .map { tasks }
    }
}