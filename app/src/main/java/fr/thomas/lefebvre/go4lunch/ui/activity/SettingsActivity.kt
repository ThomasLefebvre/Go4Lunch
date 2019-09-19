package fr.thomas.lefebvre.go4lunch.ui.activity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import fr.thomas.lefebvre.go4lunch.R
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    val user = FirebaseAuth.getInstance().currentUser//get current user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val toolbar: Toolbar = findViewById(R.id.toolbar_Activity_Settings)
        toolbar.title = getString(R.string.title_tool_bar_settings)
        setSupportActionBar(toolbar)
        getCurrentUser()
        clickDeleteButton()
    }

    private fun getCurrentUser() {
        textView_Settings_Name.text = user?.displayName//set the user name
        textView_Settings_Mail.text = user?.email//set the user mail
        Picasso.get().load(user?.photoUrl).into(circleImageView_Setting)//set the user picture
    }

    private fun deleteAccount() {
        if (user != null) {
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

    override fun onBackPressed() {
        finish()
        val intentMainActivity = Intent(this, MainActivity::class.java)
        startActivity(intentMainActivity)
        super.onBackPressed()
    }

    private fun clickDeleteButton(){
    imageButton_Delete_Account.setOnClickListener {
        alertDialogDeleteAccount()
    }
    }
}
