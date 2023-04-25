package com.buyit.buyit.start.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.buyit.buyit.R
import com.buyit.buyit.databinding.ActivitySplashScreenBinding
import com.buyit.buyit.utils.CommonUtils.auth
import com.buyit.buyit.utils.setStatusBarColor

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setStatusBarColor(R.color.orange_500)

        Handler(Looper.getMainLooper()).postDelayed({
            val currentUser = auth.currentUser
            if (currentUser != null && currentUser.isEmailVerified) {
                startActivity(
                    Intent(
                        this,
                        GetLocationPermissionActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                )
            } else {
                startActivity(
                    Intent(
                        this,
                        StartActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                )
            }
            finish()
        }, 1000)
    }
}