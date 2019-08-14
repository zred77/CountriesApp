package com.veresz.countries.util.image

import android.graphics.drawable.Drawable
import android.widget.ImageView
import coil.api.load
import coil.request.Request
import coil.transform.CircleCropTransformation
import com.veresz.countries.BuildConfig
import com.veresz.countries.util.image.ImageSize.small

private const val EXTENSION_PNG = ".png"

fun ImageView.loadImage(
    url: String,
    placeHolder: Drawable? = null,
    error: Drawable? = null,
    circular: Boolean = false,
    requestListener: Request.Listener? = null
) {
    this.load(url = url) {

        if (error != null) {
            error(error)
        }
        if (placeHolder != null) {
            placeholder(placeHolder)
        }
        if (circular) {
            transformations(CircleCropTransformation())
        }
        crossfade(true)
        if (requestListener != null) {
            listener(requestListener)
        }
    }
}

fun ImageView.loadFlag(alpha2CountryCode: String, imageSize: ImageSize = small, circular: Boolean = false, requestListener: Request.Listener? = null) {

    val url =
            BuildConfig.FLAGS_BASE_URL + resources.getString(imageSize.flagSize) + alpha2CountryCode.toLowerCase() + EXTENSION_PNG + "?raw=true"
    loadImage(
            url = url,
            circular = circular,
            requestListener = requestListener
    )
}
