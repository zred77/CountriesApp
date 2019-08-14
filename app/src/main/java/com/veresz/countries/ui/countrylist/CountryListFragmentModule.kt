package com.veresz.countries.ui.countrylist

import androidx.lifecycle.ViewModelProvider
import com.veresz.countries.di.scope.FragmentScope
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class CountryListFragmentModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [CountryListModule::class])
    abstract fun countryListFragment(): CountryListFragment

    @Module(includes = [CountryListModule.BindModule::class])
    internal class CountryListModule {

        @Module
        abstract class BindModule {

            @Binds
            abstract fun bindViewModelProviderFactory(factory: CountryListViewModel.Factory): ViewModelProvider.Factory
        }
    }
}
