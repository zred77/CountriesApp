package com.veresz.countries.ui.countrydetail

import androidx.lifecycle.ViewModelProvider
import com.veresz.countries.di.scope.FragmentScope
import com.veresz.countries.model.Country
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class CountryDetailFragmentModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [CountryDetailFragmentModule.ProviderModule::class])
    abstract fun countryDetailFragment(): CountryDetailFragment

    @Module(includes = [ProviderModule.BindModule::class])
    internal class ProviderModule {

        @Provides
        fun provideCountry(countryDetailFragment: CountryDetailFragment): Country {
            return countryDetailFragment.getCountry()
        }

        @Module
        internal abstract class BindModule {

            @Binds
            abstract fun bindViewModelProviderFactory(factory: CountryDetailViewModel.Factory): ViewModelProvider.Factory
        }
    }
}
