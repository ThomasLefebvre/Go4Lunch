package fr.thomas.lefebvre.go4lunch.ui.fragment


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import fr.thomas.lefebvre.go4lunch.R
import fr.thomas.lefebvre.go4lunch.model.RestaurantFormatted
import fr.thomas.lefebvre.go4lunch.model.nearby.NearbyPlaces
import fr.thomas.lefebvre.go4lunch.ui.`object`.Common
import fr.thomas.lefebvre.go4lunch.ui.service.IGoogleAPIService
import kotlinx.android.synthetic.main.app_bar_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */

class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    //google maps
    lateinit var mGoogleMap: GoogleMap
    lateinit var mMapVIew: MapView
    lateinit var mView: View

    //location
    private lateinit var mLastLocation: Location
    private val LOCATION_REQUEST = 123

    //location user
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    //API service
    lateinit var mService: IGoogleAPIService

    //list of restaurant
    lateinit var listRestaurant:ArrayList<RestaurantFormatted>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the google map layout for this fragment
        mView = inflater.inflate(R.layout.fragment_maps, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.toolbar.title = getString(R.string.title_tool_bar_hungry)
        initMap()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        // init service
        mService = Common.googleAPIService

    }

    //init google map view
    private fun initMap() {
        mMapVIew = mView.findViewById(R.id.mapView)
        mMapVIew.onCreate(null)
        mMapVIew.onResume()
        mMapVIew.getMapAsync(this)

    }


    override fun onMapReady(googleMap: GoogleMap) {
        MapsInitializer.initialize(context)
        mGoogleMap = googleMap
        setUpMap()


    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun setUpMap() {

        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) //check the location permission
        {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST)//if location permission isn't request the permission
            return
        }
        mGoogleMap.isMyLocationEnabled = true //active the location
        mGoogleMap.uiSettings.isZoomControlsEnabled = true//active the button zoom
        fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity())//get the last location of user
        { location ->
            if (location != null) //if the last location isn't null
            {
                mLastLocation = location
                var mLatitude = mLastLocation.latitude//set the latitude of location
                var mLongitude = mLastLocation.longitude//set the longitude of location
                Log.d("POSITION_DEBUG", mLatitude.toString())
                Log.d("POSITION_DEBUG", mLongitude.toString())
                nearbyPlaces(mLatitude, mLongitude)//set the nearby place in terme of user location
                val currentLatLng = LatLng(mLatitude, mLongitude)////set the position of location
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14f))//move the camera on the user location


            }
        }
    }


    private fun nearbyPlaces(latitude: Double, longitude: Double) {

        //build url request base on location
        val url = getUrl(latitude, longitude)

        mService.getNearbyPlaces(url)//call the request
            .enqueue(object : Callback<NearbyPlaces> {
                override fun onFailure(call: Call<NearbyPlaces>, t: Throwable) {//is request is failure
                    Toast.makeText(requireContext(), "" + t.message, Toast.LENGTH_LONG).show()
                    Log.d("REQUEST_DEBUG", t.message!!)
                }

                override fun onResponse(call: Call<NearbyPlaces>, response: Response<NearbyPlaces>) {//is request is success

                    if (response!!.isSuccessful) {

                        Log.d("REQUEST_DEBUG", response.message())
                        addMarkerOnMap(response)
                        sendNearbyPlacesToActivity(listRestaurant!!)//send nearby places data on the main activity
                    }
                }

            })

    }

    private fun addMarkerOnMap(response:Response<NearbyPlaces>){
        listRestaurant=ArrayList()
        for (i in 0 until response!!.body()!!.results!!.size) {//boucle for add marker all places

            val listPlaces = response.body()//set the list of places
            val googlePlace = response.body()!!.results[i]//set the place in term of index
            val lat = googlePlace.geometry!!.location!!.lat//set the latitude of place
            val lng = googlePlace.geometry!!.location!!.lng//set the longitude of place
            val placeName = googlePlace.name//set the name of place
            val latLng = LatLng(lat, lng)//set the position of place
            Log.d("PLACE_DEBUG", placeName)
            val markerOptions = MarkerOptions()//add marker in the map
                .position(latLng)
                .title(placeName)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
            mGoogleMap.addMarker(markerOptions)
            val distance=calculDistance(mLastLocation.latitude,mLastLocation.longitude,lat,lng)
            Log.d("DISTANCE_DEBUG",distance)
            val openHours: Boolean?
            if(googlePlace.opening_hours!=null){
                openHours=googlePlace.opening_hours.open_now
            }
            else openHours=null
            val rating:Double?
            if(googlePlace.rating!=null){
                rating=googlePlace.rating
            }
            else rating=null
            val photoUrl:String?
            if(googlePlace.photos!=null){
                photoUrl=googlePlace.photos[0].photo_reference
            }
            else photoUrl=null
            val restaurantFormatted=RestaurantFormatted(googlePlace.place_id,placeName,googlePlace.vicinity,openHours,rating,photoUrl,distance)//set restaurant
            listRestaurant.add(restaurantFormatted)//add restaurant on list restaurant

        }
    }

    private fun calculDistance(currentLat:Double,currentLng:Double,placeLat:Double,placeLng:Double):String{
        // Set array of distance
        val distanceArray = floatArrayOf(0f)
        //calcul distance between current location and places location
        Location.distanceBetween(currentLat, currentLng,//current lat and lng
            placeLat,placeLng,//place lat and lng
            distanceArray)//array for the result
        val distance:String=distanceArray[0].toInt().toString()//add the distance in the model
        return distance
    }

    private fun getUrl(latitude: Double, longitude: Double): String {
        val googlePlaceUrl = StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
        googlePlaceUrl.append("?location=$latitude,$longitude")//set user position
        googlePlaceUrl.append("&rankby=distance")//=1km
        googlePlaceUrl.append("&type=restaurant")//set type of search
        googlePlaceUrl.append("&key=${getString(R.string.api_browser_places)}")//api key

        Log.d("URL_DEBUG", googlePlaceUrl.toString())
        return googlePlaceUrl.toString()//return the string url

    }






    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_REQUEST -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    setUpMap()
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }


    private fun sendNearbyPlacesToActivity(listRestaurant: ArrayList<RestaurantFormatted>) {
        val intent = Intent("DATA_ACTION")//init broadcast intent
        intent.putParcelableArrayListExtra("LIST_RESTAURANT_TO_ACTIVITY", listRestaurant)//init the data for send
        Log.d("SEND_DEBUG",listRestaurant[0].name)
        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)//send the data
    }




}



