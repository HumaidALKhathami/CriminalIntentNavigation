package com.example.criminalintent.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import java.util.*

@Dao
interface CrimeDao {

    @Query("SELECT * FROM crime")
    fun getAllCrimes(): LiveData<List<Crime>>

    @Query("SELECT * FROM crime WHERE id = (:id) ")
    fun getCrime(id:UUID):LiveData< Crime?>

    @Update
    fun updateCrime(crime: Crime)

}