package com.example.criminalintent.crimeFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.criminalintent.database.Crime
import com.example.criminalintent.database.CrimeRepository
import java.util.*

class CrimeFragmentViewModel : ViewModel() {

    private val crimeRepository = CrimeRepository.get()
    private val crimeIdLiveData = MutableLiveData<UUID>()

    var crimeLiveData:LiveData<Crime?> =
        Transformations.switchMap(crimeIdLiveData){
            crimeRepository.getCrime(it)
        }

    fun loadCrime(crimeId:UUID){
        crimeIdLiveData.value = crimeId
    }

    fun saveUpdates (crime: Crime){
        crimeRepository.updateCrime(crime)
    }

}