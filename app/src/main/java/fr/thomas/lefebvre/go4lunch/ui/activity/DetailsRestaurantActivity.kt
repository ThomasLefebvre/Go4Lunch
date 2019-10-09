package fr.thomas.lefebvre.go4lunch.ui.activity


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import fr.thomas.lefebvre.go4lunch.R
import fr.thomas.lefebvre.go4lunch.model.database.User
import fr.thomas.lefebvre.go4lunch.model.details.DetailsPlace
import fr.thomas.lefebvre.go4lunch.model.details.Photo
import fr.thomas.lefebvre.go4lunch.ui.`object`.Common
import fr.thomas.lefebvre.go4lunch.ui.adapter.WorkMatesAdapter
import fr.thomas.lefebvre.go4lunch.ui.service.IGoogleAPIService
import fr.thomas.lefebvre.go4lunch.ui.service.UserHelper
import kotlinx.android.synthetic.main.activity_details_restaurant.*
import retrofit2.Call
import retrofit2.Response


class DetailsRestaurantActivity : AppCompatActivity() {


    lateinit var placeId: String
    lateinit var webSiteUrl: String
    lateinit var phoneNumberIntent: String
    private val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private val userHelper: UserHelper = UserHelper()

    lateinit var adapter: WorkMatesAdapter


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
        rejoinButtonClick()
        getUserJoinRestaurant()
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

    private fun initStars(rating: Double) {//set the rating bar with the rating google map
        val ratingBarFloat = (rating * (3.0f / 5.0f))//convert rating to 3.0
        val ratingBarFloatRound = Math.round(ratingBarFloat * 10.0f) / 10.0f//round rating to 1 decimal
        if (ratingBarFloatRound != 0.0f) {//if rating not null = set the rating on rating bar (minimum rating google is 1 of 5, if rating =0.0 = not rating)
            ratingBar.rating = ratingBarFloatRound
        } else ratingBar.visibility = View.GONE//if rating null = gone the rating bar
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

    private fun rejoinButtonClick() {//set the rejoin button click for choice restaurant
        rejoinActionButton.setOnClickListener {
            if (currentUser != null) {

                AlertDialog.Builder(this)
                    .setMessage(R.string.pop_pup_message_choice_restaurant)
                    .setPositiveButton(
                        R.string.pop_pup_yes
                    ) { _, _ ->
                        userHelper.updateUserRestaurantUid(placeId, currentUser.uid)
                        val restaurantName = textViewNameRestaurantDetail.text.toString()
                        userHelper.updateUserRestaurantName(restaurantName, currentUser.uid)
                        userHelper.updateUserAdress(textViewAdressRestaurantDetail.text.toString() ,currentUser.uid)
                        Toast.makeText(this, getString(R.string.toast_message_choice_restaurant), Toast.LENGTH_LONG)
                            .show()

                    }
                    .setNegativeButton(R.string.pop_pup_no, null)
                    .show()

            }
        }
    }


    private fun getUserJoinRestaurant() {
        val query = FirebaseFirestore.getInstance()
            .collection("users")
            .whereEqualTo("restaurantUid", placeId)

        val options = FirestoreRecyclerOptions.Builder<User>()
            .setQuery(query, User::class.java)
            .build()

        adapter = WorkMatesAdapter(options){ itemClick: User ->
            articleClick(itemClick)}
        recyclerView_Workmates_Details.setHasFixedSize(true)
        recyclerView_Workmates_Details.layoutManager = LinearLayoutManager(this)
        recyclerView_Workmates_Details.addItemDecoration(
            DividerItemDecoration(
                recyclerView_Workmates_Details.context,
                DividerItemDecoration.VERTICAL
            )
        )
        recyclerView_Workmates_Details.adapter = adapter

    }

    private fun articleClick(itemClick: User) {//start details activity if click on article

    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
}
