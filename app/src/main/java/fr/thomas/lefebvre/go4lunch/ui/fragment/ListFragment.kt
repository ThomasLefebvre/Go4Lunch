package fr.thomas.lefebvre.go4lunch.ui.fragment


import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import fr.thomas.lefebvre.go4lunch.R
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.util.Log
import android.widget.Toast
import fr.thomas.lefebvre.go4lunch.ui.activity.MainActivity
import fr.thomas.lefebvre.go4lunch.ui.model.NearbyPlaces
import kotlinx.android.synthetic.main.fragment_list.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ListFragment : Fragment() {

    lateinit var nearbyPlaces: NearbyPlaces

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val bundle=arguments//recover nearby places from activty
        nearbyPlaces = bundle?.getParcelable<NearbyPlaces>("NEARBY_PLACES_TO_FRAGMENTS")!!
        Toast.makeText(requireContext(),nearbyPlaces!!.results[0].name,Toast.LENGTH_LONG).show()
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


}
