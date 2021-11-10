package com.example.criminalintent.crimeFragment

import android.app.Activity
import android.content.Intent
import android.icu.text.MessageFormat.format
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat.format
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
import java.lang.String.format
import java.text.DateFormat
import java.text.MessageFormat.format
import java.util.*

const val CRIME_DATE_KEY = "crimeDate"
const val REQUEST_CONTACT = 1

class CrimeFragment : Fragment() , DatePickerDialogFragment.DatePickerCallBack{

    private lateinit var titleEditText: EditText
    private lateinit var dateBtn:Button
    private lateinit var isSolvedCheckBox: CheckBox
    private lateinit var reportBtn: Button
    private lateinit var suspectBtn: Button

    private lateinit var crime: Crime

    private val fragmentViewModel by lazy { ViewModelProvider(this).get(CrimeFragmentViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_crime,container,false)
        initialize(view)

        dateBtn.apply {

            text = crime.date.toString()

        }

        return view
    }

    private fun initialize(view: View) {
        titleEditText = view.findViewById(R.id.crime_title)
        dateBtn = view.findViewById(R.id.crime_date)
        isSolvedCheckBox = view.findViewById(R.id.crime_solved)
        reportBtn = view.findViewById(R.id.crime_report)
        suspectBtn = view.findViewById(R.id.crime_suspect)
    }


    override fun onStart() {
        super.onStart()

        dateBtn.setOnClickListener{

            // args is to send the current date to the crime picker dialog fragment
            val args = Bundle()

            args.putSerializable(CRIME_DATE_KEY,crime.date)

            val datePicker = DatePickerDialogFragment()

            datePicker.arguments = args
            datePicker.setTargetFragment(this,0)

            datePicker.show(this.parentFragmentManager,"datePicker")

        }

        suspectBtn.setOnClickListener{
        val pickContactIntent =
            Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)

            startActivityForResult(pickContactIntent , REQUEST_CONTACT)
        }

        reportBtn.setOnClickListener{
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getCrimeReport())
                putExtra(Intent.EXTRA_SUBJECT, " CriminalIntent Crime Report")
            }.also {
                val chooserIntent =
                    Intent.createChooser(it,"send_report")
                startActivity(chooserIntent)
            }
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

    private val dateFormat = "yyyy / MM / dd"

    private fun getCrimeReport (): String {

        val solvedString = if (crime.isSolved){
            "the crime has been solved"
        }else{
            "the crime has not been solved"
        }

        val dateString = android.text.format.DateFormat.format(dateFormat, crime.date)

        val suspectString = if (crime.suspect.isBlank()){
            "there is no suspect"
        }else{
            "the suspect is ${crime.suspect}"
        }

        return "$solvedString and the date of the crime is $dateString and $suspectString"

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK){
            return
        }

        if (requestCode == REQUEST_CONTACT && data != null){
            val contactURI = data.data

            val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)

            val cursor = contactURI?.let {
                requireActivity().contentResolver.query(
                    it,queryFields,null,null,null
                )
            }

            cursor?.let { cursor ->
                cursor.use {

                    if ( it.count == 0 ){ return}

                    it.moveToFirst()
                    val suspect = it.getString(0)
                    crime.suspect = suspect

                    val crimeId = arguments?.getSerializable(KEY_ID) as UUID
                    fragmentViewModel.loadCrime(crimeId)
//                    crime.id = crimeId

                    fragmentViewModel.saveUpdates(crime)
                    suspectBtn.text = suspect
                }
            }
        }
    }

}