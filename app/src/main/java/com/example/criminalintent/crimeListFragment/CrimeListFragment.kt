package com.example.criminalintent.crimeListFragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.criminalintent.database.Crime
import com.example.criminalintent.crimeFragment.CrimeFragment
import com.example.criminalintent.R

private const val TAG = "hhh"

const val KEY_ID = "myCrimeId"

class CrimeListFragment : Fragment() {

    private lateinit var crimeRecyclerView: RecyclerView

    private val crimeListViewModel
    by lazy { ViewModelProvider(this)
        .get(CrimeListViewModel::class.java) }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.fragment_list_menu,menu)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){

            R.id.new_crime -> {
//
//                val crime = Crime()
//                crimeListViewModel.addCrime(crime)
//
//                val args = Bundle()
//                args.putSerializable(KEY_ID,crime.id)
//
//                val fragment = CrimeFragment()
//
//                fragment.arguments = args
//
//                activity?.let {
//                    it.supportFragmentManager
//                        .beginTransaction()
//                        .replace(R.id.fragment_container,fragment)
//                        .addToBackStack(null)
//                        .commit()
//                }

                true
            }else -> return super.onOptionsItemSelected(item)
        }



    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list,container,false)

        crimeRecyclerView = view.findViewById(R.id.crime_recycler_View)
        val  linearLayoutManager = LinearLayoutManager(context)
        crimeRecyclerView.layoutManager = linearLayoutManager
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeListViewModel.liveDataCrimes.observe(
            viewLifecycleOwner, Observer {
                Log.d("hhh",it.size.toString())
                updateUI(it)
                Log.d("hhh",it.size.toString())
            }
        )

    }

    private fun updateUI(crimes: List<Crime>) {
        val crimeAdapter = CrimeAdapter(crimes)
        crimeRecyclerView.adapter = crimeAdapter
    }

    private inner class CrimeHolder(view:View)
        : RecyclerView.ViewHolder(view) , View.OnClickListener {

            private lateinit var crime: Crime

           private  val titleTextView:TextView = itemView.findViewById(R.id.crime_title_item)
           private val  dateTextView:TextView = itemView.findViewById(R.id.crime_date_item)
           private val isSolvedImageView:ImageView = itemView.findViewById(R.id.is_solved_iv)


           init {
               itemView.setOnClickListener(this)

           }
            fun bind(crime: Crime){
               this.crime = crime
                titleTextView.text = crime.title
                dateTextView.text = crime.date.toString()

                isSolvedImageView.visibility = if (crime.isSolved){
                    View.VISIBLE
                }else{
                    View.GONE
                }
            }

        override fun onClick(p0: View?) {
            if (p0 == itemView ){

                val navController = findNavController()

                val action = CrimeListFragmentDirections.actionCrimeListFragmentToCrimeFragment(crime.id)

                navController.navigate(action)

//                val args = Bundle()
//                    args.putSerializable(KEY_ID , crime.id)
//
//                val fragment = CrimeFragment()
//
//                fragment.arguments = args
//
//                activity?.let {
//                    it.supportFragmentManager
//                        .beginTransaction()
//                        .replace(R.id.fragment_container,fragment)
//                        .addToBackStack(null)
//                        .commit()
//                }

            }else if(p0 == dateTextView){
                Toast.makeText(context , "the title is ${crime.date}",Toast.LENGTH_LONG).show()
            }
        }
    }

    private inner class CrimeAdapter(var crimes:List<Crime>)
        :RecyclerView.Adapter<CrimeHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            val view = layoutInflater.inflate(R.layout.list_item_crime,parent,false)

            return CrimeHolder(view)
        }

        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
           val crime = crimes[position]
            holder.bind(crime)
        }

        override fun getItemCount(): Int = crimes.size

    }



}