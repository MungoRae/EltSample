package uk.me.mungorae.eltinterview.repository

import io.reactivex.Completable
import io.reactivex.Single
import uk.me.mungorae.eltinterview.api.Task
import java.io.IOException

class FakeTasksRepository : TasksRepository {
    val localTasks = mutableListOf<Task>()
    var connectionDown = false

    override fun loadTasks(filter: TasksFilter?): Single<List<Task>> {
        return Single.just(localTasks.filter { local ->
            filter == null || local.type in filter.types
        })
    }

    override fun updateTasks(): Completable {
        return if (connectionDown)
            Completable.error(IOException())
        else
            Completable.complete()
    }
}