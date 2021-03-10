package uk.me.mungorae.eltinterview.test.di

import dagger.Module
import dagger.Provides
import uk.me.mungorae.eltinterview.api.InternetConnection
import uk.me.mungorae.eltinterview.test.api.FakeInternetConnection

@Module
class TestConnectionModule(private val connection: FakeInternetConnection) {

    @Provides
    fun provideInternetConnection(): InternetConnection = connection
}