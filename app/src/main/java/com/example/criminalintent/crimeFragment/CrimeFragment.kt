package com.example.criminalintent.crimeFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.criminalintent.DatePickerDialogFragment
import com.example.criminalintent.database.Crime
import com.example.criminalintent.R
import com.example.criminalintent.crimeListFragment.KEY_ID
import java.util.*

const val CRIME_DATE_KEY = "crimeDate"

class CrimeFragment : Fragment() , DatePickerDialogFragment.DatePickerCallBack{

    private lateinit var titleEditText: EditText
    private lateinit var dateBtn:Button
    private lateinit var isSolvedCheckBox: CheckBox

    private lateinit var crime: Crime

    private val fragmentViewModel by lazy { ViewModelProvider(this).get(CrimeFragmentViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_crime,container,false)
        titleEditText = view.findViewById(R.id.crime_title)
        dateBtn = view.findViewById(R.id.crime_date)
        isSolvedCheckBox = view.findViewById(R.id.crime_solved)

        dateBtn.apply {

            text = crime.date.toString()

        }

        return view
    }


    override fun onStart() {
        super.onStart()

        dateBtn.setOnClickListener{

            // args is to send the current dat to the crime picker dialog fragment
            val args = Bundle()

            args.putSerializable(CRIME_DATE_KEY,crime.date)

            val datePicker = DatePickerDialogFragment()

            datePicker.arguments = args
            datePicker.setTargetFragment(this,0)

            datePicker.show(this.parentFragmentManager,"datePicker")

        }

        val textWatcher = object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // i will do nothing
            }

            override fun onTextChanged(sssss: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("ANAS", sssss.toString())
                crime.title = sssss.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
                // i will do nothing
            }

        }

        titleEditText.addTextChangedListener(textWatcher)

       isSolvedCheckBox.setOnCheckedChangeListener { _, isChecked ->

        crime.isSolved = isChecked

       }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crime = Crime()

        val crimeId = arguments?.getSerializable(KEY_ID) as UUID

        fragmentViewModel.loadCrime(crimeId)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentViewModel.crimeLiveData.observe(
            viewLifecycleOwner, androidx.lifecycle.Observer {
                it?.let {
                    crime = it
                    titleEditText.setText(it.title)
                    dateBtn.text = it.date.toString()
                    isSolvedCheckBox.isChecked = it.isSolved
                }
            }
        )

    }

    override fun onStop() {
        super.onStop()

        fragmentViewModel.saveUpdates(crime)
    }

    override fun onDateSelected(date: Date) {
        crime.date = date

        dateBtn.text = date.toString()
    }

}