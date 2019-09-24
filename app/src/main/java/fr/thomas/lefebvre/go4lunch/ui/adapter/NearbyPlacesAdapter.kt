package fr.thomas.lefebvre.go4lunch.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.thomas.lefebvre.go4lunch.R
import fr.thomas.lefebvre.go4lunch.model.RestaurantFormatted


class NearbyPlacesAdapter (val context:Context, private val listPlaces:List<RestaurantFormatted>, private val listener:(RestaurantFormatted)-> Unit): RecyclerView.Adapter<NearbyPlacesAdapter.ViewHolder>(){



    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v: View = LayoutInflater.from(p0.context).inflate(R.layout.recycler_view_item,p0,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return listPlaces.size
    }


    override fun onBindViewHolder(p0: ViewHolder, p1: Int) = p0.bind(context,listPlaces[p1],listener)

    class ViewHolder(elementList: View):RecyclerView.ViewHolder(elementList){





        fun bind(context: Context, restaurant:  RestaurantFormatted, listener:(RestaurantFormatted)->Unit){
            //set the graphics element for the recycler view
            val tvNamePlace: TextView =itemView.findViewById(R.id.textViewName)
            val tvAddress: TextView =itemView.findViewById(R.id.textViewAddress)
            val tvOpenHours: TextView =itemView.findViewById(R.id.textViewOpenHours)
            val tvDistance:TextView=itemView.findViewById(R.id.textViewDistance)
            val photoPlace: ImageView =itemView.findViewById(R.id.photoPlace)
            val starOne:ImageView=itemView.findViewById(R.id.imageStar1)
            val starTwo:ImageView=itemView.findViewById(R.id.imageStar2)
            val starThree:ImageView=itemView.findViewById(R.id.imageStar3)

            tvNamePlace.text=restaurant.name//set name of place
            tvAddress.text=restaurant.address//set adress of place

            //set photo of place
            if (restaurant.photoUrl!=null){
                val photoUrl="https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+restaurant.photoUrl+"&key="+context.resources.getString(R.string.api_browser_places)
                Picasso.get().load(photoUrl).into(photoPlace)
            }

            //set the rating star
            if(restaurant.rating!!>=4.0){
                starOne.visibility=View.VISIBLE
                starTwo.visibility=View.VISIBLE
                starThree.visibility=View.VISIBLE
            }



            //set open now of place
            if(restaurant.openNow==true){
                tvOpenHours.text=context.getString(R.string.open)
            }
            else if(restaurant.openNow==false){
                tvOpenHours.text=context.getString(R.string.closed)
            }

            //set distance
            tvDistance.text=restaurant.distance



            itemView.setOnClickListener {//set the click listener
                listener(restaurant) }
        }
    }
}

