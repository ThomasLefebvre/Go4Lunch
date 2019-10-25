package fr.thomas.lefebvre.go4lunch.ui.activity


import android.content.*
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import fr.thomas.lefebvre.go4lunch.ui.fragment.ListFragment
import fr.thomas.lefebvre.go4lunch.ui.fragment.MapsFragment
import fr.thomas.lefebvre.go4lunch.ui.fragment.WorkmatesFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import fr.thomas.lefebvre.go4lunch.R
import fr.thomas.lefebvre.go4lunch.model.RestaurantFormatted
import fr.thomas.lefebvre.go4lunch.model.database.User
import fr.thomas.lefebvre.go4lunch.service.UserHelper
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    lateinit var mListRestaurant: ArrayList<RestaurantFormatted>
    private var userHelper: UserHelper = UserHelper()
    val user = FirebaseAuth.getInstance().currentUser//get current user

    //current position
    var mCurrentLat: Double = 0.0
    var mCurrentLng: Double = 0.0


    //request check setting code
    companion object {
        private const val AUTOCOMPLETE_REQUEST = 2
    }

    /* ------------------------------
                                                LIFE CYCLE APK
                                                                                      ------------------------------------  */
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view_drawer)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        changeIconMenuDrawer()
        replaceFragment(MapsFragment())

        // Initialize Places.
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.api_maps_key), Locale.US)
        }

        navView.setNavigationItemSelectedListener(this)

        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        getUserInformation()

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadcastReceiver, IntentFilter("DATA_ACTION"))//register local broadcast manager
    }


    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadcastReceiver, IntentFilter("DATA_ACTION"))//register local broadcast manager
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(broadcastReceiver)//unregister local broadcast manager
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(broadcastReceiver)//unregister local broadcast manager
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            alertDialogForCloseApp()
        }
    }

    /* ------------------------------
                                                SET USER INTERFACE
                                                                                      ------------------------------------  */


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //create option menu on main activity
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //set action on click of search menu
        when (item.itemId) {
            R.id.menu_search -> {
                initAutoCompleteIntent()// launch the autocomplete intent in the click button search
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // set action on click menu drawer
        when (item.itemId) {
            R.id.nav_your_lunch -> {
                getUserRestaurantChoiceFirestore()//launch the details activity of user restaurant
                return true
            }
            R.id.nav_settings -> {
                val intentSettingsActivity =
                    Intent(this, SettingsActivity::class.java)//init the setting activity intent
                startActivity(intentSettingsActivity)//launch the settings activity
                return true
            }
            R.id.nav_logout -> {
                alertDialogLogOut()//launch the dialog for confirm log out
                return true
            }

        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    fun changeIconMenuDrawer() {//change the icon of menu drawer (burger)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_burger)
    }

    fun replaceFragment(fragment: Fragment) {//method for manage the fragment change
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        // manage click of bottom menu
        when (item.itemId) {
            R.id.navigation_maps -> {
                replaceFragment(MapsFragment())//launch maps fragment
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_list -> {
                if (ActivityCompat.checkSelfPermission(
                        this@MainActivity,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED && isLocationEnabled(this)
                )//check the location permission
                {
                    sendNearbyPlaces()//if permission is ok send nearby place
                } else {
                    replaceFragment(ListFragment())//if permission is denied list fragment is empty
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_workmates -> {
                replaceFragment(WorkmatesFragment())//launch workmates fragment
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun startDetailsActivityChoiceRestaurant(placeId: String?) {//set up the details activity
        val intentDetailsActivty = Intent(this, DetailsRestaurantActivity::class.java)
        intentDetailsActivty.putExtra("PlaceId", placeId)
        startActivity(intentDetailsActivty)
    }

    private fun isLocationEnabled(mContext: Context): Boolean {//check if location is enabled
        val lm = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || lm.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    /* ------------------------------
                                                LOAD/SEND DATA
                                                                                      ------------------------------------  */
    fun getUserInformation() {
        val navHeaderView = nav_view_drawer.getHeaderView(0)
        val navUserName = navHeaderView.tv_user_name
        val navUserMail = navHeaderView.tv_user_mail
        val navUserPic = navHeaderView.profile_image_Work_Mates
        val user = FirebaseAuth.getInstance().currentUser
        navUserName.text = user?.displayName
        navUserMail.text = user?.email
        if (user?.photoUrl != null) {
            Picasso.get().load(user.photoUrl).into(navUserPic)
        }


    }


    //Get nearbyPlaces and current location from mapsFragment
    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if ("DATA_ACTION" == intent.action) {
                if (ActivityCompat.checkSelfPermission(
                        this@MainActivity,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    mListRestaurant = intent.getParcelableArrayListExtra("LIST_RESTAURANT_TO_ACTIVITY")!!
                    mCurrentLat = intent.getDoubleExtra("CURRENT_LAT", 0.0)
                    mCurrentLng = intent.getDoubleExtra("CURRENT_LNG", 0.0)

                }

            }
        }
    }

    //Send nearbyPlaces to listFragment
    private fun sendNearbyPlaces() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val listFragment = ListFragment()
        val bundleListFragment = Bundle()
        bundleListFragment.putParcelableArrayList("LIST_RESTAURANT_TO_FRAGMENTS", mListRestaurant)
        listFragment.arguments = bundleListFragment
        fragmentTransaction.replace(R.id.fragment_container, listFragment)
        fragmentTransaction.commit()
    }

    private fun getUserRestaurantChoiceFirestore() { //load current user informations
        userHelper.getUser(user!!.uid).addOnSuccessListener { documentSnapshot ->
            val user = documentSnapshot.toObject(User::class.java)
            if (user!!.restaurantUid != null) {
                startDetailsActivityChoiceRestaurant(user.restaurantUid)

            } else {
                Toast.makeText(this, getString(R.string.toast_message_dont_make_choice), Toast.LENGTH_LONG).show()
            }
        }
            .addOnFailureListener { exception ->
                Log.d("DEBUG_FIRESTORE", exception.toString())
            }
    }

    /* ------------------------------
                                                LOG OUT USER FIREBASE
                                                                                      ------------------------------------  */
    private fun logOutUser() {

        AuthUI.getInstance().signOut(this@MainActivity)
            .addOnCompleteListener {

                val welcomeActivityIntent = Intent(this, WelcomeActivity::class.java)
                startActivity(welcomeActivityIntent)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()

            }
    }


/* ------------------------------
                                            MENU SEARCH WITH AUTOCOMPLETE SDK
                                                                                  ------------------------------------  */

    private fun initAutoCompleteIntent() {

        val fields = Arrays.asList(Place.Field.ID, Place.Field.NAME)//init the fields return from autocomplete
        val intentAutocomplete = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .setLocationRestriction(
                RectangularBounds.newInstance(//set the restrict rectangular bounds for search
                    com.google.android.gms.maps.model.LatLng(mCurrentLat - 0.05, mCurrentLng - 0.05),
                    com.google.android.gms.maps.model.LatLng(mCurrentLat + 0.05, mCurrentLng + 0.05)
                )
            )
            .setTypeFilter(TypeFilter.ESTABLISHMENT)//set the filter type
            .build(this)
        startActivityForResult(intentAutocomplete, AUTOCOMPLETE_REQUEST)
    }


/* ------------------------------
                                            MANAGE RESULT OF REQUESTS
                                                                                  ------------------------------------  */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTOCOMPLETE_REQUEST) {//result of autocomplete request
            when (resultCode) {
                RESULT_OK -> {// if result is ok
                    val place = Autocomplete.getPlaceFromIntent(data!!)
                    startDetailsActivityChoiceRestaurant(place.id)
                }
                AutocompleteActivity.RESULT_ERROR -> {//if error in request
                    val status = Autocomplete.getStatusFromIntent(data!!)
                    Log.i("DEBUG_AUTOCOMPLETE", status.statusMessage!!)
                }
                RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
        }
    }

    /* ------------------------------
                                               ALERT DIALOGS
                                                                                      ------------------------------------  */
    private fun alertDialogLogOut() {
        AlertDialog.Builder(this)
            .setMessage(R.string.pop_pup_message_log_out)
            .setPositiveButton(
                R.string.pop_pup_yes
            ) { _, _ ->
                logOutUser()
                finish()
            }
            .setNegativeButton(R.string.pop_pup_no, null)
            .show()
    }

    private fun alertDialogForCloseApp() {
        AlertDialog.Builder(this)
            .setMessage(R.string.pop_pup_message_leave)
            .setPositiveButton(R.string.pop_pup_yes) { _, _ ->
                super.onBackPressed()
            }
            .setNegativeButton(R.string.pop_pup_no, null)
            .show()
    }


}
