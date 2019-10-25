package fr.thomas.lefebvre.go4lunch.ui.activity


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import fr.thomas.lefebvre.go4lunch.R
import fr.thomas.lefebvre.go4lunch.service.UserHelper
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    val currentUser = FirebaseAuth.getInstance().currentUser//get current user
    private val userHelper: UserHelper = UserHelper()

    var SWITCH: String = "switch1"
    var SHARED_PREFS: String = "sharedPrefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val toolbar: Toolbar = this.findViewById(R.id.toolbar_Activity_Settings)
        toolbar.title = getString(R.string.title_tool_bar_settings)
        setSupportActionBar(toolbar)
        loadSwitch()
        getCurrentUser()
        clickDeleteButton()
        switchListener()
    }

    /* ------------------------------
                                                UPDATE UI
                                                                                      ------------------------------------  */
    private fun getCurrentUser() {
        textView_Settings_Name.text = currentUser?.displayName//set the user name
        textView_Settings_Mail.text = currentUser?.email//set the user mail
        if (currentUser?.photoUrl != null) {
            Picasso.get().load(currentUser?.photoUrl).into(circleImageView_Setting)//set the user picture
        }

    }

    /* ------------------------------
                                                DELETE USER
                                                                                      ------------------------------------  */
    private fun deleteAccount() {
        if (currentUser != null) {
            userHelper.deleteUser(currentUser.uid)
                .addOnSuccessListener {

                    AuthUI.getInstance().delete(this)
                        .addOnCompleteListener {

                            val welcomeActivityIntent = Intent(this, WelcomeActivity::class.java)
                            startActivity(welcomeActivityIntent)
                            finish()

                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                            onBackPressed()

                        }


                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error delete account data base", Toast.LENGTH_LONG).show()
                }

        }
    }

    private fun alertDialogDeleteAccount() {
        AlertDialog.Builder(this)
            .setMessage(R.string.pop_pup_message_delete_account)
            .setPositiveButton(
                R.string.pop_pup_yes
            ) { _, _ ->
                deleteAccount()
            }
            .setNegativeButton(R.string.pop_pup_no, null)
            .show()
    }


    private fun clickDeleteButton() {
        imageButton_Delete_Account.setOnClickListener {
            alertDialogDeleteAccount()
        }
    }


    /* ------------------------------
                                                LOAD AND SAVE DATE FOR SWITCH
                                                                                      ------------------------------------  */
    fun saveSwitch() {//SAVE DATA METHOD IN SHARED PREF
        val sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putBoolean(SWITCH, switch_notifications.isChecked)
        editor.apply()
    }

    fun loadSwitch() {//LOAD DATA OF SHARED PREF AT LAUNCH
        val sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        switch_notifications.isChecked = sharedPreferences.getBoolean(SWITCH, true)

    }

    private fun switchListener() {
        switch_notifications.setOnClickListener(View.OnClickListener {
            if (switch_notifications.isChecked) {
                userHelper.updateUserNotificationState(true, currentUser!!.uid)
                saveSwitch()
            } else {
                userHelper.updateUserNotificationState(false, currentUser!!.uid)
                saveSwitch()
            }
        })
    }
}
