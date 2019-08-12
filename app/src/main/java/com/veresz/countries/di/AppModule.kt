package com.veresz.countries.di

import android.content.Context
import com.veresz.countries.App
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun providesContext(app: App): Context = app.applicationContext
}
