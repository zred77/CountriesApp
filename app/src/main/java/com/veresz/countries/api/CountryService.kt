package com.veresz.countries.api

import com.veresz.countries.model.Country
import retrofit2.http.GET

interface CountryService {

    @GET("all")
    suspend fun getAllCountry(): List<Country>
}
