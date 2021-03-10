package uk.me.mungorae.eltinterview.database

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import uk.me.mungorae.eltinterview.api.Type

@Dao
interface TasksDao {

    @Query("SELECT * FROM dbtask")
    fun loadAllTasks(): Single<List<DbTask>>

    @Query("SELECT * FROM dbtask WHERE type IN(:types)")
    fun loadTasksOfType(types: List<Type>): Single<List<DbTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(tasks: List<DbTask>): Completable

    @Query("DELETE FROM dbtask")
    fun deleteAll(): Completable
}