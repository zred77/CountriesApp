package com.veresz.countries.repository

import com.veresz.countries.api.CountryService
import com.veresz.countries.model.Country
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(
    private val countriesService: CountryService
) : CountryRepository {

    override suspend fun getCountries(): List<Country> {
        return countriesService.getAllCountry()
    }
}
