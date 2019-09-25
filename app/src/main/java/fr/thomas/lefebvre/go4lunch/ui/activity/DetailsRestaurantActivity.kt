package fr.thomas.lefebvre.go4lunch.ui.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.squareup.picasso.Picasso
import fr.thomas.lefebvre.go4lunch.R
import fr.thomas.lefebvre.go4lunch.model.details.DetailsPlace
import fr.thomas.lefebvre.go4lunch.model.details.Photo
import fr.thomas.lefebvre.go4lunch.ui.`object`.Common
import fr.thomas.lefebvre.go4lunch.ui.service.IGoogleAPIService
import kotlinx.android.synthetic.main.activity_details_restaurant.*
import retrofit2.Call
import retrofit2.Response

class DetailsRestaurantActivity : AppCompatActivity() {


    lateinit var placeId: String
    lateinit var webSiteUrl: String
    lateinit var phoneNumberIntent: String

    //API service
    lateinit var mService: IGoogleAPIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_restaurant)
        mService = Common.googleAPIService
        initPlace()
        googlePlacesDetailsApi()
        startWebViewActivity()
        startCallActivity()


    }


    private fun initPlace() {
        placeId = intent.getStringExtra("PlaceId")!!
    }

    private fun googlePlacesDetailsApi() {
        val url = getUrl(placeId)
        mService.getDetailsPlace(url)
            .enqueue(object : retrofit2.Callback<DetailsPlace> {
                override fun onFailure(call: Call<DetailsPlace>, t: Throwable) {//is request is failure
                    Toast.makeText(this@DetailsRestaurantActivity, "" + t.message, Toast.LENGTH_LONG).show()
                    Log.d("REQUEST_DEBUG", t.message!!)
                }

                override fun onResponse(
                    call: Call<DetailsPlace>,
                    response: Response<DetailsPlace>
                ) {//is request is success

                    if (response.isSuccessful) {
                        initDetailsRestaurant(response)


                    }
                }

            })

    }

    private fun getUrl(placeId: String): String {
        val googlePlaceUrl = StringBuilder("https://maps.googleapis.com/maps/api/place/details/")
        googlePlaceUrl.append("json?place_id=$placeId")//set user position
        googlePlaceUrl.append(
            "&fields=name,photo,opening_hours,website,rating,formatted_phone_number,formatted_address&key=${getString(
                R.string.api_browser_places
            )}"
        )//set parameters and api key
        Log.d("URL_DEBUG", googlePlaceUrl.toString())
        return googlePlaceUrl.toString()//return the string url
    }

    private fun initDetailsRestaurant(response: Response<DetailsPlace>) {
        initStars(response.body()!!.result.rating)
        initWebSite(response.body()!!.result.website)
        initCall(response.body()!!.result.formatted_phone_number)
        initPhotoRestaurant(response.body()!!.result.photos)
        initDetailsTextRestaurant(response.body()!!.result.name, response.body()!!.result.formatted_address)


    }

    private fun initStars(rating: Double) {
        if (rating > 1) {//if rating superior 1.5
            imageStar1.visibility = View.VISIBLE
            Log.i("DEBUG_STAR", rating.toString() + (rating < 1))

        } else if (rating > 3) {//if rating superior 3
            imageStar1.visibility = View.VISIBLE
            imageStar2.visibility = View.VISIBLE
            Log.i("DEBUG_STAR", rating.toString() + (rating < 3))
        } else if (rating > 4) {//if rating superior 4.5
            imageStar1.visibility = View.VISIBLE
            imageStar2.visibility = View.VISIBLE
            imageStar3.visibility = View.VISIBLE
            Log.i("DEBUG_STAR", rating.toString() + (rating < 4))
        } else {
            imageStar1.visibility = View.GONE
            imageStar2.visibility = View.GONE
            imageStar3.visibility = View.GONE
        }
    }

    private fun initWebSite(webSite: String) {
        if (webSite != null) {//if website is not null
            imageButtonSite.visibility = View.VISIBLE//display the button
            textViewSite.visibility = View.VISIBLE//display the text
            webSiteUrl = webSite///set the website url in variable
        }

    }

    private fun initCall(phoneNumber: String) {
        if (phoneNumber != null) {//if phone number is not null
            imageButtonCall.visibility = View.VISIBLE//display the button
            textViewCall.visibility = View.VISIBLE//display the text
            phoneNumberIntent = phoneNumber
        }
    }

    private fun initPhotoRestaurant(photo: List<Photo>) {
        if (photo != null) {//if photo is not null
            val photoUrl =
                "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photo[0].photo_reference + "&key=" + getString(
                    R.string.api_browser_places
                )//set the photo url
            Picasso.get().load(photoUrl).into(photoDetailsRestaurant)//display the photo
        }
    }

    private fun initDetailsTextRestaurant(name: String, adress: String) {
        textViewNameRestaurantDetail.text = name//display the restaurant name
        textViewAdressRestaurantDetail.text = adress//display the restaurant address
    }

    private fun startWebViewActivity() {
        imageButtonSite.setOnClickListener {
            val webViewIntent = Intent(this, WebViewActivity::class.java)
            webViewIntent.putExtra(Intent.EXTRA_TEXT, webSiteUrl)
            startActivity(webViewIntent)
        }
    }

    private fun startCallActivity() {
        imageButtonCall.setOnClickListener {
            val intentCall = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumberIntent"))
            startActivity(intentCall)
        }
    }


}
