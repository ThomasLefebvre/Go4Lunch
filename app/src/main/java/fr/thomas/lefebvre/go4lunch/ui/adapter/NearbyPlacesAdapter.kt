package fr.thomas.lefebvre.go4lunch.ui.adapter

import android.content.Context
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
import kotlinx.android.synthetic.main.activity_details_restaurant.*
import java.lang.StringBuilder


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


        val tvNamePlace: TextView = itemView.findViewById(R.id.textViewName)
        val tvAddress: TextView = itemView.findViewById(R.id.textViewAddress)
        val tvOpenHours: TextView = itemView.findViewById(R.id.textViewOpenHours)
        val tvDistance: TextView = itemView.findViewById(R.id.textViewDistance)
        val photoPlace: ImageView = itemView.findViewById(R.id.photoPlace)
        val ratingBarRv: RatingBar = itemView.findViewById(R.id.ratingBarRv)


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
                }
                else{
                    photoPlace.setImageDrawable(resources.getDrawable(R.drawable.restaurant))
                }
                //set the rating star
                val ratingBarFloat = (restaurant.rating!!*((3.0f / 5.0f)))//convert rating to 3.0
                val ratingBarFloatRound = Math.round(ratingBarFloat * 10.0f) / 10.0f//round rating to 1 decimal
                if (ratingBarFloatRound != 0.0f) {//if rating not null = set the rating on rating bar (minimum rating google is 1 of 5, if rating =0.0 = not rating)
                    ratingBarRv.rating = ratingBarFloatRound
                } else {
                    ratingBarRv.visibility = View.GONE//if rating null = gone the rating bar
                }


                //set open now of place
                if (restaurant.openNow == true) {
                    tvOpenHours.text = context.getString(R.string.open)
                } else if (restaurant.openNow == false) {
                    tvOpenHours.text = context.getString(R.string.closed)
                }

                //set distance
                val distance = StringBuilder(restaurant.distance + "m")
                tvDistance.text = distance.toString()

                itemView.setOnClickListener {
                    //set the click listener
                    listener(restaurant)
                }
            }
    }
}

