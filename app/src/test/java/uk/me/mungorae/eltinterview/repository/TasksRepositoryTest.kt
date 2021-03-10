package uk.me.mungorae.eltinterview.repository

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Test
import uk.me.mungorae.eltinterview.api.Task
import uk.me.mungorae.eltinterview.api.TasksApi
import uk.me.mungorae.eltinterview.api.Type
import uk.me.mungorae.eltinterview.database.DbTask
import uk.me.mungorae.eltinterview.database.TasksDao


class TasksRepositoryTest {

    @Test
    fun testLoadAll() {
        val tasksApi = mock<TasksApi>()
        val tasksDao = mock<TasksDao> {
            on { loadAllTasks() } doReturn Single.just(EXAMPLE_DB_TASKS)
        }

        val repository = TasksRepository.Default(tasksApi, tasksDao)

        val actual = repository.loadTasks().blockingGet()

        assertEquals(EXAMPLE_TASKS, actual)
    }

    @Test
    fun testLoadFilter() {
        val tasksApi = mock<TasksApi>()
        val filterTypes = listOf(Type.GENERAL)
        val tasksDao = mock<TasksDao> {
            on { loadTasksOfType(filterTypes) } doReturn Single
                .just(EXAMPLE_DB_TASKS.filter { it.type in filterTypes })
        }

        val repository = TasksRepository.Default(tasksApi, tasksDao)

        val actual = repository.loadTasks(TasksFilter(filterTypes)).blockingGet()

        assertEquals(EXAMPLE_TASKS.filter { it.type in filterTypes }, actual)
    }

    companion object {
        private val EXAMPLE_DB_TASKS = listOf(
            DbTask(id = 1, name = "n1", description = "d1", type = Type.GENERAL),
            DbTask(id = 2, name = "n2", description = "d2", type = Type.MEDICATION),
            DbTask(id = 3, name = "n3", description = "d3", type = Type.NUTRITION),
            DbTask(id = 4, name = "n4", description = "d4", type = Type.GENERAL),
        )

        private val EXAMPLE_TASKS = listOf(
            Task(id = 1, name = "n1", description = "d1", type = Type.GENERAL),
            Task(id = 2, name = "n2", description = "d2", type = Type.MEDICATION),
            Task(id = 3, name = "n3", description = "d3", type = Type.NUTRITION),
            Task(id = 4, name = "n4", description = "d4", type = Type.GENERAL),
        )
    }
}