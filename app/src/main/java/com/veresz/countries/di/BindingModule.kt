package com.veresz.countries.di

import com.veresz.countries.ui.MainActivityModule
import dagger.Module

@Module(includes = [MainActivityModule::class])
abstract class BindingModule
