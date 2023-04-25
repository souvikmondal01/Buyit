package com.buyit.buyit.profile.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.buyit.buyit.R
import com.buyit.buyit.databinding.ActivityProfileBinding
import com.buyit.buyit.start.ui.activities.StartActivity
import com.buyit.buyit.utils.CommonUtils.auth
import com.buyit.buyit.utils.CommonUtils.db
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

//        fetchProfileData()
        binding.tvName.text = auth.currentUser!!.displayName
        binding.tvEmail.text = auth.currentUser!!.email


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        binding.btnLogout.setOnClickListener {
            googleSignInClient.signOut()
            Firebase.auth.signOut()
            startActivity(
                Intent(
                    this,
                    StartActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            )
            finishAffinity()
        }

    }

    private fun fetchProfileData() {
        db.collection("user").document(auth.currentUser!!.uid).addSnapshotListener { value, _ ->
            binding.tvName.text = value!!["name"].toString()
            binding.tvEmail.text = value!!["email"].toString()
        }
    }
}