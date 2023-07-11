package com.isayevapps.crimes

import android.app.Application
import com.isayevapps.crimes.db.CrimeRepository

class CrimeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        CrimeRepository.initialize(this)
    }
}