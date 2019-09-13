package fr.thomas.lefebvre.go4lunch.ui.fragments


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.thomas.lefebvre.go4lunch.R
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.os.Build
import android.os.Looper

import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */

class MapsFragment : Fragment(),OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    lateinit var mGoogleMap: GoogleMap
    lateinit var mMapVIew:MapView
    lateinit var mView:View


    private lateinit var mLastLocation: Location
    private  val LOCATION_REQUEST = 123

    //location
    private lateinit var fusedLocationClient: FusedLocationProviderClient



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
        fusedLocationClient=LocationServices.getFusedLocationProviderClient(requireActivity())
    }


    private fun initMap(){
        mMapVIew=mView.findViewById(R.id.mapView)
        mMapVIew.onCreate(null)
        mMapVIew.onResume()
        mMapVIew.getMapAsync(this)

    }


    override fun onMapReady(googleMap: GoogleMap) {
        MapsInitializer.initialize(context)
        mGoogleMap=googleMap
        setUpMap()

    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun setUpMap() {

        if (ActivityCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST)
            return
        }
        mGoogleMap.isMyLocationEnabled=true
        fusedLocationClient.lastLocation.addOnSuccessListener (requireActivity()){ location ->
            if(location!=null){
                mLastLocation=location
                val currentLatLng = LatLng(location.latitude,location.longitude)
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,15f))
                placeMarkerOnMap(currentLatLng)
                mGoogleMap.uiSettings.isZoomControlsEnabled=true

            }
        }
    }

    private fun placeMarkerOnMap(location: LatLng) {
        // 1
        val markerOptions = MarkerOptions()
            .position(location)
            .title(getString(R.string.your_position))
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))

        mGoogleMap.addMarker(markerOptions)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_REQUEST -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED))
                {
                    setUpMap()
                } else
                {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
            else ->
            {
                // Ignore all other requests.
            }
        }
    }














    fun reloadFragment(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fragmentManager?.beginTransaction()?.detach(this)?.commitNow();
            fragmentManager?.beginTransaction()?.attach(this)?.commitNow();
        } else {
            fragmentManager?.beginTransaction()?.detach(this)?.attach(this)?.commit();
        }
    }







}
