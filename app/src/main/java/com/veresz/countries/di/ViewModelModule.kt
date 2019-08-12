package com.veresz.countries.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.veresz.countries.ui.countrylist.CountryListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CountryListViewModel::class)
    internal abstract fun bindMainViewModel(viewModel: CountryListViewModel): ViewModel
}
