package fr.thomas.lefebvre.go4lunch.ui.activity

import android.content.Intent
import android.os.Bundle
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
import fr.thomas.lefebvre.go4lunch.ui.fragments.ListFragment
import fr.thomas.lefebvre.go4lunch.ui.fragments.MapsFragment
import fr.thomas.lefebvre.go4lunch.ui.fragments.WorkmatesFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.maps.OnMapReadyCallback
import fr.thomas.lefebvre.go4lunch.R


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setTitle("I'm hungry")
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



        navView.setNavigationItemSelectedListener(this)

        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        getUserInformation()

    }




    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.menu_search -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_your_lunch -> {
                //TODO
                return true
            }
            R.id.nav_settings -> {
                //TODO
                return true
            }
            R.id.nav_logout -> {
                alertDialogLogOut()
                return true
            }

        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun alertDialogLogOut(){
        AlertDialog.Builder(this)
            .setMessage(R.string.pop_pup_message_log_out)
            .setPositiveButton(R.string.pop_pup_yes
            ) { _, _ ->
                logOutUser()
                finish()}
            .setNegativeButton(R.string.pop_pup_no, null)
            .show()
    }

    fun changeIconMenuDrawer(){
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_burger)
    }

    fun replaceFragment(fragment: Fragment){
        val fragmentTransaction= supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container,fragment)
        fragmentTransaction.commit()

    }

    fun getUserInformation(){
        val navHeaderView= nav_view_drawer.getHeaderView(0)
        val navUserName=navHeaderView.tv_user_name
        val navUserMail=navHeaderView.tv_user_mail
        val navUserPic=navHeaderView.profile_image
        val user = FirebaseAuth.getInstance().currentUser
        navUserName.text = user?.displayName
        navUserMail.text = user?.email
        Picasso.get().load(user?.photoUrl).into(navUserPic)

         }

    fun logOutUser(){
        AuthUI.getInstance().signOut(this@MainActivity)
            .addOnCompleteListener{

                val welcomeActivityIntent= Intent(this,WelcomeActivity::class.java)
                startActivity(welcomeActivityIntent)
            }
            .addOnFailureListener {
                    e-> Toast.makeText(this@MainActivity,e.message,Toast.LENGTH_LONG).show()
            }
    }

    private val mOnNavigationItemSelectedListener= BottomNavigationView.OnNavigationItemSelectedListener {item->
        when(item.itemId){
            R.id.navigation_maps->{
                replaceFragment(MapsFragment())
                return@OnNavigationItemSelectedListener  true
            }
            R.id.navigation_list->{
                replaceFragment(ListFragment())
                return@OnNavigationItemSelectedListener  true
            }
            R.id.navigation_workmates->{
                replaceFragment(WorkmatesFragment())
                return@OnNavigationItemSelectedListener  true
            }
        }
        false
    }







}
