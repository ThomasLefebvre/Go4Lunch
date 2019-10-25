package fr.thomas.lefebvre.go4lunch.ui.fragment


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore

import fr.thomas.lefebvre.go4lunch.R
import fr.thomas.lefebvre.go4lunch.model.database.User
import fr.thomas.lefebvre.go4lunch.ui.activity.DetailsRestaurantActivity
import fr.thomas.lefebvre.go4lunch.ui.adapter.WorkMatesAdapter
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_workmates.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class WorkmatesFragment : Fragment() {
    lateinit var adapter: WorkMatesAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_workmates, container, false)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.toolbar.title = getString(R.string.title_tool_bar_workmate)
        getUserWithFireStore()
    }

    /* ------------------------------
                                    GET USER TO FIREBASE DATA
                                                                          ------------------------------------  */

    private fun getUserWithFireStore() {
        val query = FirebaseFirestore.getInstance()//set query for request
            .collection("users")
            .orderBy("name")


        val options = FirestoreRecyclerOptions.Builder<User>()// set option for request
            .setQuery(query, User::class.java)
            .build()

        adapter = WorkMatesAdapter(options) { itemClick: User ->
            //set recycler view
            articleClick(itemClick)
        }

        recycler_view_all_user.setHasFixedSize(false)
        recycler_view_all_user.layoutManager = LinearLayoutManager(requireContext())
        recycler_view_all_user.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recycler_view_all_user.adapter = adapter


    }

    private fun articleClick(itemClick: User) {//start details activity if click on article
        if (itemClick.restaurantUid != null) {
            val intentDetails = Intent(requireContext(), DetailsRestaurantActivity::class.java)
            intentDetails.putExtra("PlaceId", itemClick.restaurantUid)
            startActivity(intentDetails)
        }

    }


}






