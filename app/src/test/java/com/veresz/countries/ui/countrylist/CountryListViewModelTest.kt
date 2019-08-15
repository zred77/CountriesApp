package com.veresz.countries.ui.countrylist

import com.veresz.countries.InstantTaskExecutorExtension
import com.veresz.countries.repository.CountryRepository
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.*
import org.junit.jupiter.api.TestInstance.*
import org.junit.jupiter.api.extension.*

@ExperimentalCoroutinesApi
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(InstantTaskExecutorExtension::class, MockKExtension::class)
class CountryListViewModelTest {

    @MockK
    lateinit var repository: CountryRepository

    lateinit var subject: CountryListViewModel

    private val testDispatcher = TestCoroutineDispatcher()

    @BeforeAll
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @BeforeEach
    fun beforeEach() {
        subject = CountryListViewModel(repository)
    }

    @AfterAll
    fun cleanUp() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `WHEN instantiated THEN data requested from repository`() {
        coVerify(exactly = 1) { repository.getCountries() }
    }
}
