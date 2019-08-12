package com.veresz.countries.repository

import com.veresz.countries.model.Country

interface CountryRepository {

    suspend fun getCountries(): List<Country>
}
