package fr.thomas.lefebvre.go4lunch.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import fr.thomas.lefebvre.go4lunch.R
import fr.thomas.lefebvre.go4lunch.ui.`object`.Common
import fr.thomas.lefebvre.go4lunch.ui.service.IGoogleAPIService
import kotlinx.android.synthetic.main.activity_details_restaurant.*
import retrofit2.Call
import retrofit2.Response

class DetailsRestaurantActivity : AppCompatActivity() {


    lateinit var placeId:String

    //API service
    lateinit var mService: IGoogleAPIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_restaurant)
        mService = Common.googleAPIService
//        initPlace()
//        googlePlacesDetailsApi()

    }


//
//    private fun initPlace(){
//        placeId=intent.getParcelableExtra("Place")!!
//    }
//
//    private fun googlePlacesDetailsApi(){
//        val url = getUrl(place.place_id)
//        mService.getDetailsPlace(url)
//            .enqueue(object : retrofit2.Callback<DetailsPLace> {
//                override fun onFailure(call: Call<DetailsPLace>, t: Throwable) {//is request is failure
//                    Toast.makeText(this@DetailsRestaurantActivity, "" + t.message, Toast.LENGTH_LONG).show()
//                    Log.d("REQUEST_DEBUG", t.message!!)
//                }
//
//                override fun onResponse(call: Call<DetailsPLace>, response: Response<DetailsPLace>) {//is request is success
//
//                    if (response.isSuccessful) {
//
//                        textViewNameRestaurantDetail.text=response.body()!!.result.name
//                        Log.d("REQUEST_DEBUG", response.message())
//
//                    }
//                }
//
//            })
//
//    }

    private fun getUrl(placeId:String):String{
        val googlePlaceUrl = StringBuilder("https://maps.googleapis.com/maps/api/place/details/")
        googlePlaceUrl.append("json?place_id=$placeId")//set user position
        googlePlaceUrl.append("&fields=name,photo,rating,formatted_phone_number&key=${getString(R.string.api_browser_places)}")//set parameters and api key
        Log.d("URL_DEBUG", googlePlaceUrl.toString())
        return googlePlaceUrl.toString()//return the string url
    }


}
