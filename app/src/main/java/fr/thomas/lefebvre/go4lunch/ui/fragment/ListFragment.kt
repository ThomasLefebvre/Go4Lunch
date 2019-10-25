package fr.thomas.lefebvre.go4lunch.ui.fragment


import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.thomas.lefebvre.go4lunch.R
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import fr.thomas.lefebvre.go4lunch.model.RestaurantFormatted
import fr.thomas.lefebvre.go4lunch.ui.activity.DetailsRestaurantActivity
import fr.thomas.lefebvre.go4lunch.ui.adapter.NearbyPlacesAdapter
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_list.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ListFragment : Fragment() {

    lateinit var listRestaurant: ArrayList<RestaurantFormatted>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && isLocationEnabled(requireContext())
        ) {
            initBundleWithNearbyRestaurant(bundle)
            textView_List_Empty_Location_No_Permission.visibility = View.GONE
        }
        activity!!.toolbar.title = getString(R.string.title_tool_bar_hungry)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

    }

    /* ------------------------------
                                            GET THE LIST OF RESTAURANT FROM MAIN ACTIVITY
                                                                                  ------------------------------------  */
    private fun initBundleWithNearbyRestaurant(bundle: Bundle?) {
        listRestaurant = bundle!!.getParcelableArrayList("LIST_RESTAURANT_TO_FRAGMENTS")!!
        if (listRestaurant.size <= 1) {
            textView_List_Empty.visibility = View.VISIBLE

        } else {
            setRecyclerView(listRestaurant)
        }
    }

    /* ------------------------------
                                            SET RECYCLER VIEW
                                                                                  ------------------------------------  */
    private fun setRecyclerView(listPlaces: List<RestaurantFormatted>) {
        view!!.recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
            adapter = NearbyPlacesAdapter(context, listPlaces) { itemClick: RestaurantFormatted ->
                articleClick(itemClick)
            }
        }
    }

    private fun articleClick(itemClick: RestaurantFormatted) {//start details activity if click on article

        val intentDetails = Intent(requireContext(), DetailsRestaurantActivity::class.java)
        intentDetails.putExtra("PlaceId", itemClick.id)
        startActivity(intentDetails)


    }

    /* ------------------------------
                                            CHECK THE LOCATION ENABLED
                                                                                  ------------------------------------  */

    private fun isLocationEnabled(mContext: Context): Boolean {
        val lm = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || lm.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


}
