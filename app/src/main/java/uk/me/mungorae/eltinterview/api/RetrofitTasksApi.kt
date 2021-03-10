package uk.me.mungorae.eltinterview.api

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RetrofitTasksApi {

    @GET("/tasks.json")
    fun getTasks(): Single<List<Task>>

    companion object {
        fun create(): RetrofitTasksApi {
            return Retrofit.Builder()
                .baseUrl("https://adam-deleteme.s3.amazonaws.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(RetrofitTasksApi::class.java)
        }
    }
}