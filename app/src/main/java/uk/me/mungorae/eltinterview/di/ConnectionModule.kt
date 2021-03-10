package uk.me.mungorae.eltinterview.di

import android.content.Context
import dagger.Module
import dagger.Provides
import uk.me.mungorae.eltinterview.api.InternetConnection

@Module
class ConnectionModule {

    @Provides
    fun provideInternetConnection(context: Context): InternetConnection {
        return InternetConnection.Default(context)
    }
}