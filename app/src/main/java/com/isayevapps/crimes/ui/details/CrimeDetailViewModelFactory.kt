package com.isayevapps.crimes.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CrimeDetailViewModelFactory(
    private val crimeID: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CrimeDetailViewModel(crimeID) as T
    }
}