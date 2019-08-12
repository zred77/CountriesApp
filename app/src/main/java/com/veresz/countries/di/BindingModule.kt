package com.veresz.countries.di

import com.veresz.countries.ui.MainActivity
import com.veresz.countries.ui.countrydetail.CountryDetailFragment
import com.veresz.countries.ui.countrylist.CountryListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BindingModule {

    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun countryListFragment(): CountryListFragment

    @ContributesAndroidInjector
    abstract fun countryDetailFragment(): CountryDetailFragment
}
