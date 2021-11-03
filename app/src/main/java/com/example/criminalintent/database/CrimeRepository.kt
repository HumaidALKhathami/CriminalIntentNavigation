package com.example.criminalintent.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "crime-database"
class CrimeRepository private constructor(context: Context){

    private val  database:CrimeDatabase = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabase::class.java,
        DATABASE_NAME
    ).build()


    private val crimeDao = database.crimeDao()

    private val executer = Executors.newSingleThreadExecutor()

    fun getAllCrimes():LiveData< List<Crime>>  = crimeDao.getAllCrimes()

    fun getCrime(id: UUID):LiveData<Crime?> {

        return crimeDao.getCrime(id)
    }

    fun updateCrime(crime: Crime){
        executer.execute{
            crimeDao.updateCrime(crime)
        }
    }

    companion object{
        var INSTANCE:CrimeRepository? = null

        fun initialize(context: Context){
            if (INSTANCE == null){
                INSTANCE = CrimeRepository(context)
            }

        }



        fun get() :CrimeRepository{
            return INSTANCE ?:
            throw IllegalStateException("CrimeRepository must be initialized ")
        }
    }
}