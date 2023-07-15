package com.isayevapps.crimes.db

import android.content.Context
import androidx.room.Room
import com.isayevapps.crimes.models.Crime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

class CrimeRepository private constructor(
    context: Context,
    private val coroutineScope: CoroutineScope = GlobalScope
) {

    private val database: CrimeDatabase = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabase::class.java,
        "crime_database"
    ).build()

    private val crimeDao = database.crimeDao()

    fun getCrimes(): Flow<List<Crime>> = crimeDao.getCrimes()
    suspend fun getCrime(id: UUID): Crime = crimeDao.getCrime(id)

    fun updateCrime(crime: Crime) = coroutineScope.launch {
        crimeDao.updateCrime(crime)
    }

    suspend fun addCrime(crime: Crime) = crimeDao.addCrime(crime)

    suspend fun deleteCrime(crime: Crime) = crimeDao.deleteCrime(crime)


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