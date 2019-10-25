package fr.thomas.lefebvre.go4lunch.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import fr.thomas.lefebvre.go4lunch.R
import java.util.*
import fr.thomas.lefebvre.go4lunch.service.UserHelper as UserHelper
import com.google.android.gms.tasks.OnFailureListener


class WelcomeActivity : AppCompatActivity() {

    private val REQUEST_CODE: Int = 123
    lateinit var providers: List<AuthUI.IdpConfig>
    private var userHelper: UserHelper = UserHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        //init
        providers = Arrays.asList<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.TwitterBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build(),//facebook login
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        if (!checkCurrentUserIsConnected()) {
            showSignInOptions()
        } else {

            startActivity()
            finish()
        }

    }

    private fun showSignInOptions() {// login activity build by firebase UI
        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder()//set the sign in activity with firebaseUI
                .setAvailableProviders(providers)//set providers
                .setTheme(R.style.SignTheme)//set theme of activity
                .setIsSmartLockEnabled(false)//disabled smart lock
                .setLogo(R.drawable.logo_go_for_lunch)////set logo
                .build(), REQUEST_CODE//build
        )

    }

    private fun startActivity() {// start main activity after control of login
        val intentMainActivity = Intent(this, MainActivity::class.java)
        startActivity(intentMainActivity)
    }

    private fun checkCurrentUserIsConnected(): Boolean {// test if user is already connected
        val user = FirebaseAuth.getInstance().currentUser
        return user != null
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {//login activity result for action
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser//get current user
                Toast.makeText(this, " ${user?.displayName} is connected", Toast.LENGTH_LONG).show()
                createUserOnFirestoreDataBase()
                startActivity()
                finish()

            } else {
                finish()
            }
        }
    }

    private fun createUserOnFirestoreDataBase() {//create user on database if not exist
        val user = FirebaseAuth.getInstance().currentUser//get current user
        val name = user?.displayName//get name current user
        val email = user?.email//get email current user
        val photoUrl = user?.photoUrl.toString()//get
        val uidUser = user?.uid//get id current user
        userHelper.getUser(uidUser!!).addOnCompleteListener { doc ->
            //get the response
            if (!doc.result!!.exists()) {//check if user not exist
                userHelper.createUser(uidUser, name!!, email!!, photoUrl, null, null, null, true, arrayListOf())
                    .addOnFailureListener(onFailureListener())//create user in data base if not exist
            }
        }
    }

    private fun onFailureListener(): OnFailureListener {//failure listener
        return OnFailureListener {
            Toast.makeText(
                applicationContext,
                getString(R.string.error),
                Toast.LENGTH_LONG
            ).show()
        }
    }

}
