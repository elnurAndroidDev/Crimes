package com.isayevapps.crimes.db

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.isayevapps.crimes.models.Crime
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.Executors

class CrimeRepository private constructor(context: Context) {

    private val database: CrimeDatabase = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabase::class.java,
        "crime_database"
    ).build()

    private val crimeDao = database.crimeDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getCrimes(): Flow<List<Crime>> = crimeDao.getCrimes()
    suspend fun getCrime(id: Int): Flow<Crime?> = crimeDao.getCrime(id)


    companion object {
        private var INSTANCE: CrimeRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get(): CrimeRepository {
            return INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}