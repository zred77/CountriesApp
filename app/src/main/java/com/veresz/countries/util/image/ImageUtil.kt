package com.veresz.countries.util.image

import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.veresz.countries.BuildConfig
import com.veresz.countries.util.image.ImageSize.small

private const val EXTENSION_PNG = ".png"

fun ImageView.load(
    url: String,
    placeHolder: Drawable? = null,
    error: Drawable? = null,
    circular: Boolean = false
) {
    if (TextUtils.isEmpty(url)) {
        setImageDrawable(placeHolder)
        return
    }

    val requestOptions = RequestOptions()
    if (placeHolder != null) requestOptions.placeholder(placeHolder)
    if (error != null) requestOptions.error(error)
    if (circular) requestOptions.circleCrop()

    Glide.with(context)
        .asBitmap()
        .apply(requestOptions)
        .load(url)
        .into(this)
}

fun ImageView.loadFlag(alpha2CountryCode: String, imageSize: ImageSize = small, circular: Boolean = false) {

    val url = BuildConfig.FLAGS_BASE_URL + resources.getString(imageSize.flagSize) + alpha2CountryCode.toLowerCase() + EXTENSION_PNG + "?raw=true"
    load(url = url, circular = circular)
}
