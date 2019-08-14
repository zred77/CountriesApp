package com.veresz.countries.ui

import com.veresz.countries.di.scope.ActivityScope
import com.veresz.countries.ui.countrydetail.CountryDetailFragmentModule
import com.veresz.countries.ui.countrylist.CountryListFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [CountryListFragmentModule::class, CountryDetailFragmentModule::class])
    abstract fun mainActivity(): MainActivity
}
