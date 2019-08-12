package com.veresz.countries

import com.veresz.countries.di.AppComponent
import com.veresz.countries.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber

class App : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        initTimber()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return createAppComponent()
    }

    private fun createAppComponent(): AppComponent {
        return DaggerAppComponent
            .builder()
            .application(this)
            .build()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
