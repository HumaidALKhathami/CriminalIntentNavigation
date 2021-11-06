package com.example.criminalintent

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.criminalintent.crimeFragment.CRIME_DATE_KEY
import java.util.*

class DatePickerDialogFragment : DialogFragment() {

    interface DatePickerCallBack{
        fun onDateSelected(date: Date)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // this is to recive the current date from crime fragment
       val date = arguments?.getSerializable(CRIME_DATE_KEY) as Date

        // this is how to make object from calendar class
        val calendar = Calendar.getInstance()
        calendar.time = date
        // this is how to make a year from the above value
        val year = calendar.get(Calendar.YEAR)

        val month = calendar.get(Calendar.MONTH)

        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->

            val resultDate = GregorianCalendar(year,month,day).time

            targetFragment?.let {

                (it as DatePickerCallBack).onDateSelected(resultDate)
            }
        }

        return DatePickerDialog(
            requireContext(),
            dateListener ,
            year,
            month,
            day
        )
    }
}