package com.veresz.countries.ui.countrylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.veresz.countries.model.Country
import com.veresz.countries.repository.CountryRepository
import javax.inject.Inject
import kotlinx.coroutines.launch

class CountryListViewModel @Inject constructor(
    private val countryRepository: CountryRepository
) : ViewModel() {

    private val loadingStateLiveData = MutableLiveData<Boolean>()
    private val countryLiveData by lazy {
        val liveData = MutableLiveData<Result<List<Country>>>()
        refresh()
        liveData
    }

    fun countries(): LiveData<Result<List<Country>>> = countryLiveData
    fun loadingState(): LiveData<Boolean> = loadingStateLiveData

    fun refresh() {
        viewModelScope.launch {
            runCatching {
                loadingStateLiveData.value = true
                countryRepository.getCountries()
            }.onSuccess {
                loadingStateLiveData.value = false
                countryLiveData.value = Result.success(it)
            }.onFailure {
                loadingStateLiveData.value = false
                countryLiveData.value = Result.failure(it)
            }
        }
    }
}
