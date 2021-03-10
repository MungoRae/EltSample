package uk.me.mungorae.eltinterview.di

import dagger.Component
import uk.me.mungorae.eltinterview.ui.list.ListFragment
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        TasksApiModule::class,
        ViewModelBuilderModule::class,
        ViewModelModule::class,
        ConnectionModule::class,
    ]
)
interface AppComponent {
    fun inject(listFragment: ListFragment)
}