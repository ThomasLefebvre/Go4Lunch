package fr.thomas.lefebvre.go4lunch.ui.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.thomas.lefebvre.go4lunch.R
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.util.Log
import android.widget.Toast

import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import fr.thomas.lefebvre.go4lunch.ui.`object`.Common
import fr.thomas.lefebvre.go4lunch.ui.model.NearbyPlaces
import fr.thomas.lefebvre.go4lunch.ui.service.IGoogleAPIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


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

    internal lateinit var currentPlace: NearbyPlaces


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

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST
            )
            return
        }
        mGoogleMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
            if (location != null) {
                mLastLocation = location
                var mLatitude = mLastLocation.latitude
                var mLongitude = mLastLocation.longitude
                Log.d("POSITION_DEBUG", mLatitude.toString())
                Log.d("POSITION_DEBUG", mLongitude.toString())
                nearbyPlaces(mLatitude,mLongitude)
                val currentLatLng = LatLng(mLatitude, mLongitude)
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                placeMarkerOnMap(currentLatLng)
                mGoogleMap.uiSettings.isZoomControlsEnabled = true


            }
        }
    }

    //method for place marker in map in terms of LatLng
    private fun placeMarkerOnMap(location: LatLng) {
        val markerOptions = MarkerOptions()
            .position(location)
            .title(getString(R.string.your_position))
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))

        mGoogleMap.addMarker(markerOptions)
    }

    private fun nearbyPlaces(latitude: Double, longitude: Double) {

        //build url request base on location
        val url = getUrl(latitude, longitude)

        mService.getNearbyPlaces(url)
            .enqueue(object : Callback<NearbyPlaces> {
                override fun onFailure(call: Call<NearbyPlaces>, t: Throwable) {
                    Toast.makeText(requireContext(), "" + t.message, Toast.LENGTH_LONG).show()
                    Log.d("REQUEST_DEBUG",t.message!!)
                }

                override fun onResponse(call: Call<NearbyPlaces>, response: Response<NearbyPlaces>) {
                  currentPlace = response.body()!!

                    if (response!!.isSuccessful) {
                        Log.d("REQUEST_DEBUG",response.message())
                        for (i in 0 until response!!.body()!!.results!!.size) {

                            val googlePlace = response.body()!!.results[i]
                            val lat = googlePlace.geometry!!.location!!.lat
                            val lng = googlePlace.geometry!!.location!!.lng
                            val placeName = googlePlace.name
                            val latLng = LatLng(lat, lng)
                            Log.d("PLACE_DEBUG",placeName)
                            val markerOptions = MarkerOptions()
                                .position(latLng)
                                .title(placeName)
                            mGoogleMap.addMarker(markerOptions)
                            Toast.makeText(requireContext(),"The best is: "+currentPlace.results[0].name,Toast.LENGTH_LONG).show()
                        }
                    }
                }

            })

    }

    private fun getUrl(latitude: Double, longitude: Double): String {
        val googlePlaceUrl = StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
        googlePlaceUrl.append("?location=$latitude,$longitude")//set user position
        googlePlaceUrl.append("&radius=1000")//=1km
        googlePlaceUrl.append("&type=restaurant")//set type of search
        googlePlaceUrl.append("&key=${getString(R.string.api_browser_places)}")//api key

        Log.d("URL_DEBUG", googlePlaceUrl.toString())

        return googlePlaceUrl.toString()

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


}



