package com.veresz.countries.di

import com.veresz.countries.BuildConfig
import com.veresz.countries.api.CountryService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
class NetworkModule {

    @Provides
    fun provideOkhttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    @Provides
    fun provideCountriesService(client: OkHttpClient): CountryService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.RESTCOUNTRIES_BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(CountryService::class.java)
    }
}
