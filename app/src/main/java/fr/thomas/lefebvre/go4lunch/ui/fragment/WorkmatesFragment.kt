package fr.thomas.lefebvre.go4lunch.ui.fragment


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import fr.thomas.lefebvre.go4lunch.R
import fr.thomas.lefebvre.go4lunch.model.database.User
import fr.thomas.lefebvre.go4lunch.ui.activity.DetailsRestaurantActivity
import fr.thomas.lefebvre.go4lunch.ui.adapter.WorkMatesAdapter
import fr.thomas.lefebvre.go4lunch.ui.service.UserHelper
import kotlinx.android.synthetic.main.activity_details_restaurant.*
import kotlinx.android.synthetic.main.activity_details_restaurant.recyclerView_Workmates_Details
import kotlinx.android.synthetic.main.activity_details_restaurant.view.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_workmates.*
import kotlinx.android.synthetic.main.fragment_workmates.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class WorkmatesFragment : Fragment() {
    lateinit var listAllUser: ArrayList<User>
    private val userHelper = UserHelper()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_workmates, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.toolbar.title = getString(R.string.title_tool_bar_workmate)
        getAllUser()
    }


    private fun getAllUser() {
        listAllUser = ArrayList()
        userHelper.getAllUser()
            .addOnSuccessListener { documents ->

                for (document in documents) {
                    val user = document.toObject(User::class.java)
                    addUserOnList(listAllUser, user)
                }
                if (listAllUser.size != 0) {
                    setRecyclerView(listAllUser)
                    Log.i(
                        "DEBUG",
                        "0:" + listAllUser[0].restaurantUid + "0:" + listAllUser[1].restaurantUid + "0:" + listAllUser[2].restaurantUid
                    )
                }


            }
            .addOnFailureListener { exception ->
                Log.w("DEBUG", "Error getting documents: ", exception)
            }
    }

    private fun addUserOnList(listUser: ArrayList<User>, user: User) {
        listUser.add(user)
    }

    private fun setRecyclerView(listUser: ArrayList<User>) {
        recycler_view_all_user.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                DividerItemDecoration(
                    recycler_view_all_user.context,
                    DividerItemDecoration.VERTICAL
                )
            )
            recycler_view_all_user.adapter = WorkMatesAdapter( context, listUser) { itemClick: User ->
                articleClick(itemClick)
            }
        }


        Log.i("DEBUG_RECYCLER_VIEW", "Set recycler view workmates")
        Log.i("DEBUG_RECYCLER_VIEW", listUser.size.toString())


    }

    private fun articleClick(itemClick: User) {
        if (itemClick.restaurantUid != null) {
            val intentDetails = Intent(requireContext(), DetailsRestaurantActivity::class.java)
            intentDetails.putExtra("PlaceId", itemClick.restaurantUid)
            startActivity(intentDetails)
        }
    }

    //TODO UPDATE LIST WITH FIRESTORE DATA AUTOMATICALLY
}
