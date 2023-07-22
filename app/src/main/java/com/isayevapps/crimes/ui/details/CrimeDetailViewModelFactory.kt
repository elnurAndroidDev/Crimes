package com.isayevapps.crimes.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.UUID

class CrimeDetailViewModelFactory(
    private val crimeID: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CrimeDetailViewModel(crimeID) as T
    }
}