package fr.thomas.lefebvre.go4lunch.ui.fragment



import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.thomas.lefebvre.go4lunch.R
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import fr.thomas.lefebvre.go4lunch.ui.adapter.NearbyPlacesAdapter
import fr.thomas.lefebvre.go4lunch.ui.model.NearbyPlaces
import fr.thomas.lefebvre.go4lunch.ui.model.Result
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

    lateinit var nearbyPlaces: NearbyPlaces


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle=arguments
        nearbyPlaces = bundle?.getParcelable<NearbyPlaces>("NEARBY_PLACES_TO_FRAGMENTS")!!
        Toast.makeText(requireContext(),nearbyPlaces.results[0].name,Toast.LENGTH_LONG).show()
        val listPlaces=nearbyPlaces.results

        view.recyclerView.apply {
            layoutManager= LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
            adapter= NearbyPlacesAdapter(listPlaces) { itemClick: Result ->
                articleClick(itemClick)
            }
        }



    }

    fun articleClick(itemClick: Result){//

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance=true

    }


}
