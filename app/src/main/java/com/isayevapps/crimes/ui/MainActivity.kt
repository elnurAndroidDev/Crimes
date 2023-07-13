package com.isayevapps.crimes.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.isayevapps.crimes.R
import com.isayevapps.crimes.adapters.OnCrimeClicked
import com.isayevapps.crimes.ui.details.CrimeFragment

class MainActivity : AppCompatActivity(), OnCrimeClicked {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCrimeClick(id: Int) {
        val fragment = CrimeFragment.newInstance(id)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}