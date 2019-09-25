package fr.thomas.lefebvre.go4lunch.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import fr.thomas.lefebvre.go4lunch.R
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        //set the web view
        val urlWebView= intent.getStringExtra(Intent.EXTRA_TEXT)
        web_view.loadUrl(urlWebView)

    }
}
