@file:JvmName("CountryUtil")

package com.veresz.countries.util

import android.content.Context
import com.veresz.countries.R
import com.veresz.countries.model.Country

fun getCurrencies(country: Country, context: Context): String {
    return country.currencies
        .joinToString(separator = "\n") {
            context.getString(R.string.detail_currencies_with_name_and_symbol, it.name, it.symbol, it.code)
        }
}

fun getRegion(country: Country, context: Context): String {
    return context.getString(R.string.detail_region_with_subregion, country.region, country.subregion)
        .removeSuffix(", ")
}

fun getLanguages(country: Country, context: Context): String {
    return country.languages
        .joinToString(separator = "\n") {
            context.getString(R.string.detail_language_with_nativename, it.name, it.nativeName)
        }
}
