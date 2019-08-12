package com.veresz.countries.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RegionalBloc(
    val acronym: String,
    val name: String,
    val otherAcronyms: List<String>,
    val otherNames: List<String>
) : Parcelable
