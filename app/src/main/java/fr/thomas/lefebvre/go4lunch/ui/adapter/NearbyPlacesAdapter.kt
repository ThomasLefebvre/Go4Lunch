package fr.thomas.lefebvre.go4lunch.ui.adapter

import android.annotation.SuppressLint
import android.opengl.Visibility
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.thomas.lefebvre.go4lunch.R
import fr.thomas.lefebvre.go4lunch.ui.`object`.Common
import fr.thomas.lefebvre.go4lunch.ui.model.Result
import fr.thomas.lefebvre.go4lunch.ui.service.IGoogleAPIService
import java.lang.StringBuilder
import kotlin.math.round
import kotlin.math.truncate


class NearbyPlacesAdapter (private val listPlaces:List<Result>, private val listener:(Result)-> Unit): RecyclerView.Adapter<NearbyPlacesAdapter.ViewHolder>(){



    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v: View = LayoutInflater.from(p0.context).inflate(R.layout.recycler_view_item,p0,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return listPlaces.size
    }


    override fun onBindViewHolder(p0: ViewHolder, p1: Int) = p0.bind(listPlaces[p1],listener)

    class ViewHolder(elementList: View):RecyclerView.ViewHolder(elementList){





        fun bind(place:Result, listener:(Result)->Unit){
            //set the graphics element for the recycler view
            val tvNamePlace: TextView =itemView.findViewById(R.id.textViewName)
            val tvAddress: TextView =itemView.findViewById(R.id.textViewAddress)
            val tvOpenHours: TextView =itemView.findViewById(R.id.textViewOpenHours)
            val tvDistance:TextView=itemView.findViewById(R.id.textViewDistance)
            val photoPlace: ImageView =itemView.findViewById(R.id.photoPlace)
            val starOne:ImageView=itemView.findViewById(R.id.imageStar1)
            val starTwo:ImageView=itemView.findViewById(R.id.imageStar2)
            val starThree:ImageView=itemView.findViewById(R.id.imageStar3)



            //get the photo reference
            if(place.photos!=null){
                val photoUrl="https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+place.photos[0].photo_reference+"&key=AIzaSyCYCJ0NjpFaTmCMqRQRn2i20vzlYyd5kOc"
                Picasso.get().load(photoUrl).into(photoPlace)
            }


            tvNamePlace.text=place.name//set the name of place
            tvAddress.text = place.vicinity//set the adress of place
            tvDistance.text= place.distance+"m"//set the distance of current location

            //set the rating star
            if(place.rating<=2){
                starOne.visibility=View.GONE
                starTwo.visibility=View.GONE
            }
            if (place.rating<=4&&place.rating>2){
                starOne.visibility=View.GONE
            }


            itemView.setOnClickListener {//set the click listener
                listener(place) }
        }
    }
}