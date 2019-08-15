package com.veresz.countries.ui

sealed class ViewState {

    object Data : ViewState()
    object Error : ViewState()
    object Loading : ViewState()
}
