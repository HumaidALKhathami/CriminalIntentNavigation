package com.example.criminalintent.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.criminalintent.Crime
import java.lang.IllegalStateException
import java.util.*

private const val DATABASE_NAME = "crime-database"
class CrimeRepository private constructor(context: Context){

    private val  database:CrimeDatabase = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabase::class.java,
        DATABASE_NAME
    ).build()


    private val crimeDao = database.crimeDao()

    fun getAllCrimes():LiveData< List<Crime>>  = crimeDao.getAllCrimes()

//    fun getCrime(id:UUID):LiveData<Crime?> {
//
//        crimeDao.getCrime(id)
//    }

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