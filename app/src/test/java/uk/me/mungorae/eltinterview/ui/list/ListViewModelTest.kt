package uk.me.mungorae.eltinterview.ui.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.times
import uk.me.mungorae.eltinterview.api.InternetConnection
import uk.me.mungorae.eltinterview.api.Task
import uk.me.mungorae.eltinterview.api.Type
import uk.me.mungorae.eltinterview.repository.FakeTasksRepository
import uk.me.mungorae.eltinterview.rxjava.TestSchedulerProvider

class ListViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun testTaskAreShown() {
        runAfterSetupEnvironment { env ->
            env.repo.localTasks.addAll(EXAMPLE_TASKS)

            env.viewModel.onViewCreated()

            // once from local and once after download
            verify(env.tasksObserver, times(2)).onChanged(EXAMPLE_TASKS)
        }
    }

    @Test
    fun testDownloadFailsGracefully() {
        runAfterSetupEnvironment { env ->
            env.repo.localTasks.addAll(EXAMPLE_TASKS)
            env.repo.connectionDown = true

            env.viewModel.onViewCreated()

            verify(env.tasksObserver).onChanged(EXAMPLE_TASKS)
            verify(env.errorObserver).onChanged(any<ListViewModel.Error.ConnectionError>())
        }
    }

    @Test
    fun testOfflineBanner() {
        runAfterSetupEnvironment { env ->
            whenever(env.connection.hasInternet()).thenReturn(false)
            env.repo.connectionDown = true

            env.viewModel.onViewCreated()

            verify(env.showOfflineObserver).onChanged(true)
        }
    }

    @Test
    fun testConnectionErrorNotShownIfOffline() {
        runAfterSetupEnvironment { env ->
            whenever(env.connection.hasInternet()).thenReturn(false)
            env.repo.connectionDown = true

            env.viewModel.onViewCreated()

            verify(env.errorObserver).onChanged(any<ListViewModel.Error.None>())
        }
    }

    @Test
    fun testFilter() {
        val expectedFilters = listOf(
            ListViewModel.Filter(Type.GENERAL, false),
            ListViewModel.Filter(Type.MEDICATION, false),
            ListViewModel.Filter(Type.NUTRITION, false),
        )
        runAfterSetupEnvironment { env ->
            env.repo.localTasks.addAll(EXAMPLE_TASKS)

            env.viewModel.onViewCreated()

            // once for local and once for download
            verify(env.filtersObserver, times(2)).onChanged(expectedFilters)
        }
    }

    @Test
    fun testFilterChange() {
        runAfterSetupEnvironment { env ->
            env.repo.localTasks.addAll(EXAMPLE_TASKS)

            env.viewModel.onViewCreated()

            env.viewModel.onFilterItemSelected(EXAMPLE_FILTERS[0])

            verify(env.tasksObserver).onChanged(EXAMPLE_TASKS.filter { it.type == Type.GENERAL })
            verify(env.filtersObserver).onChanged(EXAMPLE_FILTERS.map {
                if (it.type == Type.GENERAL) ListViewModel.Filter(Type.GENERAL, true)
                else it
            })
        }
    }

    @Test
    fun testFilterUndo() {
        runAfterSetupEnvironment { env ->
            env.repo.localTasks.addAll(EXAMPLE_TASKS)

            env.viewModel.onViewCreated()

            env.viewModel.onFilterItemSelected(EXAMPLE_FILTERS[0])
            env.viewModel.onFilterItemSelected(EXAMPLE_FILTERS[0])

            inOrder(env.tasksObserver) {
                verify(env.tasksObserver, times(2)).onChanged(EXAMPLE_TASKS)
                verify(env.tasksObserver).onChanged(EXAMPLE_TASKS.filter { it.type == Type.GENERAL })
                verify(env.tasksObserver).onChanged(EXAMPLE_TASKS)
            }
        }
    }

    @Test
    fun testAllFiltered() {
        runAfterSetupEnvironment { env ->
            env.repo.localTasks.addAll(EXAMPLE_TASKS)

            env.viewModel.onViewCreated()

            env.viewModel.onFilterItemSelected(EXAMPLE_FILTERS[0])
            env.viewModel.onFilterItemSelected(EXAMPLE_FILTERS[1])
            env.viewModel.onFilterItemSelected(EXAMPLE_FILTERS[2])

            inOrder(env.tasksObserver) {
                verify(env.tasksObserver, times(2)).onChanged(EXAMPLE_TASKS)
                verify(env.tasksObserver).onChanged(EXAMPLE_TASKS.filter { it.type == EXAMPLE_FILTERS[0].type })
                verify(env.tasksObserver).onChanged(EXAMPLE_TASKS.filter {
                    it.type in listOf(
                        EXAMPLE_FILTERS[0].type,
                        EXAMPLE_FILTERS[1].type,
                    )
                })
                verify(env.tasksObserver).onChanged(EXAMPLE_TASKS.filter {
                    it.type in listOf(
                        EXAMPLE_FILTERS[0].type,
                        EXAMPLE_FILTERS[1].type,
                        EXAMPLE_FILTERS[2].type,
                    )
                })
            }
        }
    }

    private fun runAfterSetupEnvironment(
        test: (Environment) -> Unit
    ) {
        val repo = FakeTasksRepository()
        val connection = ONLINE_CONNECTION
        val vm = ListViewModel(repo, TestSchedulerProvider(), connection)

        val tasksObserver = mock<Observer<List<Task>>>()
        val filtersObserver = mock<Observer<List<ListViewModel.Filter>>>()
        val errorObserver = mock<Observer<ListViewModel.Error>>()
        val showOfflineObserver = mock<Observer<Boolean>>()
        vm.tasks.observeForever(tasksObserver)
        vm.filterItems.observeForever(filtersObserver)
        vm.showError.observeForever(errorObserver)
        vm.showOffline.observeForever(showOfflineObserver)
        val env = Environment(
            vm,
            repo,
            connection,
            tasksObserver,
            filtersObserver,
            errorObserver,
            showOfflineObserver
        )

        test.invoke(env)

        vm.tasks.removeObserver(tasksObserver)
        vm.filterItems.removeObserver(filtersObserver)
        vm.showError.removeObserver(errorObserver)
        vm.showOffline.removeObserver(showOfflineObserver)
    }

    data class Environment(
        val viewModel: ListViewModel,
        val repo: FakeTasksRepository,
        val connection: InternetConnection,
        val tasksObserver: Observer<List<Task>>,
        val filtersObserver: Observer<List<ListViewModel.Filter>>,
        val errorObserver: Observer<ListViewModel.Error>,
        val showOfflineObserver: Observer<Boolean>,
    )

    companion object {
        private val EXAMPLE_TASKS = listOf(
            Task(id = 1, name = "n1", description = "d1", type = Type.GENERAL),
            Task(id = 2, name = "n2", description = "d2", type = Type.MEDICATION),
            Task(id = 3, name = "n3", description = "d3", type = Type.NUTRITION),
            Task(id = 4, name = "n4", description = "d4", type = Type.GENERAL),
        )

        private val EXAMPLE_FILTERS = listOf(
            ListViewModel.Filter(Type.GENERAL, false),
            ListViewModel.Filter(Type.MEDICATION, false),
            ListViewModel.Filter(Type.NUTRITION, false),
        )

        private val ONLINE_CONNECTION = mock<InternetConnection> {
            on { hasInternet() } doReturn true
        }
    }
}