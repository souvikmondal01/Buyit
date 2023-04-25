package com.buyit.buyit.start.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.buyit.buyit.R
import com.buyit.buyit.databinding.ActivityGetLocationPermissionBinding
import com.buyit.buyit.home.ui.activities.HomeActivity
import com.buyit.buyit.start.models.Location
import com.buyit.buyit.utils.CommonUtils
import com.buyit.buyit.utils.CommonUtils.db
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

class GetLocationPermissionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGetLocationPermissionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetLocationPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onStart() {
        locationPermission()
        super.onStart()
    }


    private fun locationPermission() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                when {
                    permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                        startActivity(
                            Intent(
                                this,
                                HomeActivity::class.java
                            ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        )
                        setLocation()
                        finishAffinity()
                    }
                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                        // Only approximate location access granted.
                        dialogBox()
                    }
                    else -> {
                        // No location access granted.
                        dialogBox()
                    }
                }
            }
        }

        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 100
            )
        }
    }

    private fun dialogBox() {
        val builder = MaterialAlertDialogBuilder(this)
        builder.setMessage(R.string.dialogMessage)
            .setCancelable(true)
        builder.setPositiveButton("Ok") { dialog, id ->
            openSetting()
        }
        builder.setNegativeButton("Cancel") { dialog, id ->
            finish()
        }
        val alert = builder.create()
        alert.setOnCancelListener {
            finish()
        }
        alert.show()
    }


    private fun openSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    @SuppressLint("HardwareIds")
    private fun setLocation() {

        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {

            val androidId = Settings.Secure.getString(
                this.contentResolver,
                Settings.Secure.ANDROID_ID
            )

            if (it != null) {
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                val area = addresses?.get(0)?.featureName
                val locality = addresses?.get(0)?.subLocality
                val city = addresses?.get(0)?.locality
                val state = addresses?.get(0)?.adminArea
                val country = addresses?.get(0)?.countryName
                val pin = addresses?.get(0)?.postalCode
                val fullAddress = addresses?.get(0)?.getAddressLine(0)
                val latitude = addresses?.get(0)?.latitude.toString()
                val longitude = addresses?.get(0)?.longitude.toString()

                val location = Location(
                    area,
                    locality,
                    city,
                    state,
                    country,
                    pin,
                    fullAddress,
                    latitude,
                    longitude
                )
                db.collection("user").document("customer").collection("user").document(
                    CommonUtils.auth.currentUser!!.uid
                ).update("location", location)
                db.collection("user").document("customer").collection("user").document(
                    CommonUtils.auth.currentUser!!.uid
                ).update("androidID", androidId)
            }

        }


    }


}