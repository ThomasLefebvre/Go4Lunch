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
import fr.thomas.lefebvre.go4lunch.ui.service.UserHelper as UserHelper
import com.google.android.gms.tasks.OnFailureListener


class WelcomeActivity : AppCompatActivity() {

    private val REQUEST_CODE: Int = 123
    lateinit var providers: List<AuthUI.IdpConfig>
     private var userHelper: UserHelper= UserHelper()

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
            finish()
            startMainActivity()
        }

    }

    private fun showSignInOptions() {
        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.SignTheme)
                .setIsSmartLockEnabled(false)
                .setLogo(R.drawable.restaurant)
                .build(), REQUEST_CODE
        )

    }

    private fun startMainActivity() {
        val intentMainActivity = Intent(this, MainActivity::class.java)
        startActivity(intentMainActivity)
    }

    private fun checkCurrentUserIsConnected(): Boolean {
        val user = FirebaseAuth.getInstance().currentUser
        return user != null
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser//get current user
                Toast.makeText(this, " ${user?.displayName} is connected", Toast.LENGTH_LONG).show()
                createUserOnFirestoreDataBase()
                val intentMainActivity = Intent(this, MainActivity::class.java)
                finish()
                startActivity(intentMainActivity)

            } else {
                finish()
            }
        }
    }

    private fun createUserOnFirestoreDataBase(){
        val user=FirebaseAuth.getInstance().currentUser//get current user
        val name=user?.displayName//get name current user
        val email=user?.email//get email current user
        val photoUrl=user?.photoUrl.toString()//get
        val uidUser=user?.uid//get id current user
        userHelper.getUser(uidUser!!).addOnCompleteListener { doc ->//get the response
            if(!doc.result!!.exists()){//check if user not exist
                userHelper.createUser(uidUser,name!!,email!!,photoUrl,"","").addOnFailureListener(onFailureListener())//create user in data base if not exist
            }
        }
    }

    protected fun onFailureListener(): OnFailureListener {
        return OnFailureListener {
            Toast.makeText(
                applicationContext,
                getString(fr.thomas.lefebvre.go4lunch.R.string.error),
                Toast.LENGTH_LONG
            ).show()
        }
    }

}
