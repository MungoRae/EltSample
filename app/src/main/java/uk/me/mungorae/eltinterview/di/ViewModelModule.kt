package uk.me.mungorae.eltinterview.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import uk.me.mungorae.eltinterview.ui.list.ListViewModel

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ListViewModel::class)
    abstract fun bindViewModel(viewModel: ListViewModel): ViewModel
}