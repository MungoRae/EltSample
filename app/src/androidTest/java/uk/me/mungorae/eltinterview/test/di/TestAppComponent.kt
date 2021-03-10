package uk.me.mungorae.eltinterview.test.di

import dagger.Component
import uk.me.mungorae.eltinterview.di.*
import uk.me.mungorae.eltinterview.test.TestApp

@Component(
    modules = [
        AppModule::class,
        TestItemsApiModule::class,
        ViewModelBuilderModule::class,
        ViewModelModule::class,
        TestConnectionModule::class,
    ]
)
interface TestAppComponent: AppComponent {
    fun inject(listFragment: TestApp)
}