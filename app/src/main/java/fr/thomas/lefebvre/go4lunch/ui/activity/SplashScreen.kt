package fr.thomas.lefebvre.go4lunch.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import fr.thomas.lefebvre.go4lunch.R
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long = 3000
    lateinit var animationRotate: Animation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        animationRotate()


        Handler().postDelayed(
            {

                startActivity(Intent(this, MainActivity::class.java))//start main activity
                finish()//finish splace screen
            }, SPLASH_TIME_OUT//time wait
        )
    }

    private fun animationRotate() {
        animationRotate = AnimationUtils.loadAnimation(this, R.anim.rotate)
        imageViewBurger.startAnimation(animationRotate)
    }
}
