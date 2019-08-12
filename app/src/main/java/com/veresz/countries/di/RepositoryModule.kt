package com.veresz.countries.di

import com.veresz.countries.repository.CountryRepository
import com.veresz.countries.repository.CountryRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindsCountryRepository(repository: CountryRepositoryImpl): CountryRepository
}
