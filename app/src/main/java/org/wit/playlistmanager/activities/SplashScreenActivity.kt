package org.wit.playlistmanager.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import org.wit.playlistmanager.R
import org.wit.playlistmanager.databinding.ActivitySplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageViewSplashscreen.alpha = 0f
        binding.imageViewSplashscreen.animate().setDuration(1500).alpha(1f).withEndAction{
            val launcherIntent = Intent(this, LoginActivity::class.java)
            startActivity(launcherIntent)
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            finish()
        }
    }
}