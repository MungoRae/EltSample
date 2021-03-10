package uk.me.mungorae.eltinterview.repository

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.SingleTransformer
import uk.me.mungorae.eltinterview.api.Task
import uk.me.mungorae.eltinterview.api.TasksApi
import uk.me.mungorae.eltinterview.database.DbTask
import uk.me.mungorae.eltinterview.database.TasksDao

interface TasksRepository {

    fun loadTasks(filter: TasksFilter? = null): Single<List<Task>>
    fun updateTasks(): Completable

    class Default(
        private val tasksApi: TasksApi,
        private val tasksDao: TasksDao,
    ) : TasksRepository {

        override fun loadTasks(filter: TasksFilter?): Single<List<Task>> {
            return if (filter == null) {
                tasksDao.loadAllTasks()
                    .compose(DbTaskTransformer())
            } else {
                tasksDao.loadTasksOfType(filter.types)
                    .compose(DbTaskTransformer())
            }
        }

        override fun updateTasks(): Completable {
            return tasksApi.getTasks()
                .flatMapCompletable { tasks ->
                    tasksDao.insertAll(
                        tasks.map { DbTask(it.id, it.name, it.description, it.type) }
                    )
                }
        }
    }

    class DbTaskTransformer: SingleTransformer<List<DbTask>, List<Task>> {
        override fun apply(upstream: Single<List<DbTask>>): SingleSource<List<Task>> {
            return upstream.map { dbTask ->
                dbTask.map { Task(it.id, it.name, it.description, it.type) }
            }
        }
    }
}

