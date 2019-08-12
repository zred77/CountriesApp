package com.veresz.countries.di

import com.veresz.countries.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BindingModule {

    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity
}
