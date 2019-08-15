package com.veresz.countries.repository

import com.veresz.countries.api.CountryService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.*

@ExtendWith(MockKExtension::class)
class CountryRepositoryImplTest {

    @MockK
    lateinit var mockService: CountryService

    lateinit var subject: CountryRepository

    @Test
    fun `WHEN getCountries() then api called`() = runBlocking {
        coEvery { mockService.getAllCountry() } returns listOf()
        subject = CountryRepositoryImpl(mockService)

        subject.getCountries()
        coVerify(exactly = 1) { mockService.getAllCountry() }
    }
}
