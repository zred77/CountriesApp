package com.veresz.countries.util.image

import androidx.annotation.StringRes
import com.veresz.countries.R

enum class ImageSize(
    @StringRes val flagSize: Int
) {

    small(R.string.path_flag_size_small),
    medium(R.string.path_flag_size_small),
    large(R.string.path_flag_size_large)
}
