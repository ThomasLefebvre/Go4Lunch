package fr.thomas.lefebvre.go4lunch.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.thomas.lefebvre.go4lunch.R
import fr.thomas.lefebvre.go4lunch.model.RestaurantFormatted
import fr.thomas.lefebvre.go4lunch.model.database.User
import fr.thomas.lefebvre.go4lunch.service.UserHelper
import fr.thomas.lefebvre.go4lunch.utils.ConverterHelper
import java.lang.StringBuilder


@Suppress("DEPRECATION")
class NearbyPlacesAdapter(

    val context: Context,
    private val listRestaurant: List<RestaurantFormatted>,
    private val listener: (RestaurantFormatted) -> Unit
) : RecyclerView.Adapter<NearbyPlacesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view: View = LayoutInflater.from(p0.context).inflate(R.layout.recycler_view_item, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listRestaurant.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, listRestaurant[position], listener)
    }

    class ViewHolder(elementList: View) : RecyclerView.ViewHolder(elementList) {
        private val userHelper: UserHelper = UserHelper()
        private val converterHelper = ConverterHelper()


        val tvNamePlace: TextView = itemView.findViewById(R.id.textViewName)
        val tvAddress: TextView = itemView.findViewById(R.id.textViewAddress)
        val tvOpenHours: TextView = itemView.findViewById(R.id.textViewOpenHours)
        val tvDistance: TextView = itemView.findViewById(R.id.textViewDistance)
        val photoPlace: ImageView = itemView.findViewById(R.id.photoPlace)
        val ratingBarRv: RatingBar = itemView.findViewById(R.id.ratingBarRv)
        val tvNumbers: TextView = itemView.findViewById(R.id.textView_Number_Workmates)
        val imageWorkmate: ImageView = itemView.findViewById(R.id.imageViewWorkmates)


        fun bind(context: Context, restaurant: RestaurantFormatted, listener: (RestaurantFormatted) -> Unit) =
            with(itemView) {

                //set the graphics element for the recycler view
                tvNamePlace.text = restaurant.name//set name of place
                tvAddress.text = restaurant.address//set adress of place

                //set photo of place
                if (restaurant.photoUrl != null) {
                    val photoUrl =
                        "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + restaurant.photoUrl + "&key=" + context.resources.getString(
                            R.string.api_browser_places
                        )
                    Picasso.get().load(photoUrl).into(photoPlace)
                } else {
                    photoPlace.setImageDrawable(resources.getDrawable(R.drawable.restaurant))
                }
                //set rating bar
                if (restaurant.rating != 0.0) {//if rating not null = set the rating on rating bar (minimum rating google is 1 of 5, if rating =0.0 = not rating)
                    ratingBarRv.rating = converterHelper.getRatingBarFloatRound(restaurant.rating!!)
                } else {
                    ratingBarRv.visibility = View.GONE//if rating null = gone the rating bar
                }

                //set open now of place
                if (restaurant.openNow != null) {
                    tvOpenHours.text = converterHelper.getStringOpening(
                        restaurant.openNow,
                        context.getString(R.string.open),
                        context.getString(R.string.closed)
                    )
                }

                //set the number of workmates joined this restaurant
                val listUserChoiceThisRestaurant = ArrayList<User>()
                userHelper.getUserByPlaceId(restaurant.id!!)//call the user list who choice this restaurant
                    .addOnSuccessListener { documents ->

                        for (document in documents) {
                            val user = document.toObject(User::class.java)
                            listUserChoiceThisRestaurant.add(user)
                        }
                        if (listUserChoiceThisRestaurant.size != 0) {//if the list isn't null display the info
                            tvNumbers.text = "(${listUserChoiceThisRestaurant.size})"
                            imageWorkmate.visibility = View.VISIBLE
                        } else {// if the list is null gone the info
                            tvNumbers.text = ""
                            imageWorkmate.visibility = View.GONE

                        }

                    }
                    .addOnFailureListener { exception ->
                        Log.w("DEBUG", "Error getting documents: ", exception)
                    }

                //set distance
                val distance = StringBuilder(restaurant.distance!!)
                tvDistance.text = distance.toString()

                itemView.setOnClickListener {
                    //set the click listener
                    listener(restaurant)
                }
            }
    }
}

