package uk.me.mungorae.eltinterview.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import uk.me.mungorae.eltinterview.api.InternetConnection
import uk.me.mungorae.eltinterview.api.Task
import uk.me.mungorae.eltinterview.api.Type
import uk.me.mungorae.eltinterview.repository.TasksFilter
import uk.me.mungorae.eltinterview.repository.TasksRepository
import uk.me.mungorae.eltinterview.rxjava.SchedulerProvider
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ListViewModel @Inject constructor(
    private val tasksRepository: TasksRepository,
    private val schedulerProvider: SchedulerProvider,
    private val connection: InternetConnection,
) : ViewModel() {
    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks

    private val _showOffline = MutableLiveData<Boolean>()
    val showOffline: LiveData<Boolean> = _showOffline

    private val _noItemsVisible = MutableLiveData<Boolean>()
    val noItemsVisible: LiveData<Boolean> = _noItemsVisible

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _showError = MutableLiveData<Error>()
    val showError: LiveData<Error> = _showError

    private val _filterItems = MutableLiveData<List<Filter>>()
    val filterItems: LiveData<List<Filter>> = _filterItems

    private val disposable = CompositeDisposable()

    fun onViewCreated() {
        _noItemsVisible.postValue(false)
        _showOffline.postValue(false)
        _showError.postValue(Error.None)

        loadAllTasks()
        downloadTasks()
        monitorConnection()
    }

    fun onViewDestroyed() {
        disposable.clear()
    }

    fun onRefreshButtonClicked() {
        downloadTasks()
    }

    fun onFilterItemSelected(filter: Filter) {
        filterItems.value
            ?.map { existing ->
                if (existing.type == filter.type) {
                    Filter(existing.type, !existing.selected)
                } else {
                    existing
                }
            }
            ?.let {
                _filterItems.postValue(it)
                it
            }
            ?.let { list ->
                if (list.none { it.selected }) {
                    loadTasks()
                } else {
                    list.filter { it.selected }
                        .map { it.type }
                        .let { TasksFilter(it) }
                        .let { loadTasks(it) }
                }
            }
    }

    private fun loadAllTasks() {
        loadTasks()
        updateFilters()
    }

    private fun loadTasks(filter: TasksFilter? = null) {
        tasksRepository.loadTasks(filter)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.main())
            .subscribe(
                {
                    showTasks(it)
                },
                {
                    Timber.tag("ELT").e(it)
                    _showError.postValue(Error.DatabaseError)
                }
            )
            .also { disposable.add(it) }
    }

    private fun downloadTasks() {
        tasksRepository.updateTasks()
            .doOnSubscribe {
                _isLoading.postValue(true)
            }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.main())
            .subscribe(
                {
                    loadAllTasks()
                    _isLoading.postValue(false)
                },
                {
                    Timber.tag("ELT").e(it)
                    _isLoading.postValue(false)
                    if (showOffline.value == false) {
                        _showError.postValue(Error.ConnectionError)
                    }
                }
            )
            .also { disposable.add(it) }
    }

    private fun showTasks(tasks: List<Task>) {
        _noItemsVisible.postValue(tasks.isEmpty())
        _tasks.postValue(tasks)
    }

    private fun updateFilters() {
        tasksRepository.loadTasks()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.main())
            .subscribe { tasks ->
                val existing = filterItems.value?.filter { item ->
                    tasks.find { it.type == item.type } != null
                }
                tasks.map { it.type }
                    .toSet()
                    .map { type ->
                        existing?.find { it.type == type } ?: Filter(type, false)
                    }
                    .sortedBy { it.type.name }
                    .let { _filterItems.postValue(it) }
            }
            .also { disposable.add(it) }
    }

    private fun monitorConnection() {
        onConnectivityChanged(connection.hasInternet())

        // monitor every 2 seconds
        Single.timer(2, TimeUnit.SECONDS)
            .map { connection.hasInternet() }
            .repeat()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.main())
            .subscribe(
                { hasInternet -> onConnectivityChanged(hasInternet) },
                { Timber.tag("ELT").e(it) }
            )
            .also { disposable.add(it) }
    }

    private fun onConnectivityChanged(connected: Boolean) {
        if (connected) {
            _showOffline.postValue(false)
        } else {
            _showOffline.postValue(true)
        }
    }

    sealed class Error {
        object ConnectionError : Error()
        object DatabaseError : Error()
        object None : Error()
    }

    data class Filter(val type: Type, val selected: Boolean)
}