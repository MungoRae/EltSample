package uk.me.mungorae.eltinterview.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import uk.me.mungorae.eltinterview.api.RetrofitTasksApi
import uk.me.mungorae.eltinterview.api.TasksApi
import uk.me.mungorae.eltinterview.database.AppDatabase
import uk.me.mungorae.eltinterview.database.TasksDao
import uk.me.mungorae.eltinterview.repository.TasksRepository
import uk.me.mungorae.eltinterview.rxjava.SchedulerProvider

@Module
class AppModule(private val context: Context) {

    @Provides
    fun provideApplicationContext(): Context {
        return context
    }

    @Provides
    fun provideRetrofitTaskApi(): RetrofitTasksApi {
        return RetrofitTasksApi.create()
    }

    @Provides
    fun provideDatabase(): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "elt-database"
        ).build()
    }

    @Provides
    fun provideTasksDao(database: AppDatabase): TasksDao {
        return database.tasksDao()
    }

    @Provides
    fun provideSchedulerProvider(): SchedulerProvider {
        return SchedulerProvider.Default()
    }

    @Provides
    fun provideTasksRepository(tasksApi: TasksApi, tasksDao: TasksDao): TasksRepository {
        return TasksRepository.Default(tasksApi, tasksDao)
    }
}