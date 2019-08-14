package com.veresz.countries.ui.countrydetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.veresz.countries.model.Country
import com.veresz.countries.util.livedata
import javax.inject.Inject

class CountryDetailViewModel(
    savedState: SavedStateHandle
) : ViewModel() {

    val country by savedState.livedata<Country>()

    class Factory @Inject constructor(
        private val country: Country
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return CountryDetailViewModel(SavedStateHandle().apply {
                set("country", country)
            }) as T
        }
    }
}
