package com.veresz.countries.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Language(
    val iso639_1: String,
    val iso639_2: String,
    val name: String,
    val nativeName: String
) : Parcelable
