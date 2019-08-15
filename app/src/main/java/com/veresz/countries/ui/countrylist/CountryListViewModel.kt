package com.veresz.countries.ui.countrylist

import androidx.lifecycle.*
import com.veresz.countries.model.Country
import com.veresz.countries.repository.CountryRepository
import com.veresz.countries.ui.ViewState
import javax.inject.Inject
import kotlinx.coroutines.launch

class CountryListViewModel constructor(
    private val countryRepository: CountryRepository
) : ViewModel() {

    private val viewStateLiveData = MutableLiveData<ViewState>()
    private val allCountriesLiveData by lazy {
        val liveData = MutableLiveData<List<Country>>()
        refresh()
        liveData
    }
    var selectedFilters = mutableSetOf<String>()
    val viewState: LiveData<ViewState> = viewStateLiveData
    val regionFilters = allCountriesLiveData.map { countryList ->
        countryList.distinctBy {
            it.region
        }.map {
            it.region
        }.filter {
            it.isNotBlank()
        }
    }
    val countries = MediatorLiveData<List<Country>>().apply {
        addSource(allCountriesLiveData) {
            filterCountries()
        }
    }

    fun testMethod() {
        viewModelScope.launch {
            countryRepository.getCountries()
        }
    }

    fun refresh() {
        viewModelScope.launch {
            runCatching {
                viewStateLiveData.value = ViewState.Loading
                countryRepository.getCountries()
            }.onSuccess {
                viewStateLiveData.value = ViewState.Data
                allCountriesLiveData.value = it
            }.onFailure {
                viewStateLiveData.value = ViewState.Error
            }
        }
    }

    fun resetFilters() {
        selectedFilters.clear()
        filterCountries()
    }

    fun onFilterChecked(filter: String, checked: Boolean) {
        if (checked) {
            selectedFilters.add(filter)
        } else {
            selectedFilters.remove(filter)
        }
        filterCountries()
    }

    private fun filterCountries() {
        allCountriesLiveData.value?.let {
            countries.value = it.filter {
                selectedFilters.isEmpty() || selectedFilters.contains(it.region)
            }
        }
    }

    class Factory @Inject constructor(
        private val countryRepository: CountryRepository
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CountryListViewModel(countryRepository) as T
        }
    }
}
