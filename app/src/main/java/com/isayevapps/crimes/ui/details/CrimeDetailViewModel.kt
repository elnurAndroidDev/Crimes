package com.isayevapps.crimes.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isayevapps.crimes.db.CrimeRepository
import com.isayevapps.crimes.models.Crime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class CrimeDetailViewModel(private val crimeId: String) : ViewModel() {
    private val crimeRepository = CrimeRepository.get()

    private val _crime: MutableStateFlow<Crime?> = MutableStateFlow(null)
    val crime: StateFlow<Crime?> = _crime.asStateFlow()

    init {
        if (crimeId == CrimeFragment.NEW_CRIME) _crime.value = Crime()
        else viewModelScope.launch {
            _crime.value = crimeRepository.getCrime(UUID.fromString(crimeId))
        }
    }

    fun updateCrime(onUpdate: (Crime) -> Crime) {
        _crime.update { oldCrime ->
            oldCrime?.let { onUpdate(it) }
        }
    }

    fun deleteCrime() = viewModelScope.launch {
        _crime.value?.let {
            crimeRepository.deleteCrime(it)
        }
    }

    fun saveOrUpdate() {
        crime.value?.let {
            if (crimeId == CrimeFragment.NEW_CRIME)
                crimeRepository.addCrime(it)
            else
                crimeRepository.updateCrime(it)
        }
    }
}