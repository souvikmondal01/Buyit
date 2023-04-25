package com.buyit.buyit.home.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.buyit.buyit.databinding.FragmentCartBinding
import com.google.android.gms.location.LocationServices
import java.util.*

class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCartBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        getLocation()

//        GlobalScope.launch(Dispatchers.IO) {
//            val userCollection =
//                db.collection("user").document("customer")
//                    .collection("user").document("QGQOYPvEhMY8pA5slhD9ZpKW3DD2").get().await()
//            withContext(Dispatchers.Main){
//                Toast.makeText(requireContext(), "hi", Toast.LENGTH_SHORT).show()
//            }
//            val user = userCollection.toObject(UserModel::class.java)
//            withContext(Dispatchers.Main) {
//                binding.tv.text = user.toString()
//            }
//        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    private fun getLocation() {

        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {

            if (it != null) {
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
//                binding.tvLocation.text = addresses!![0].getAddressLine(0)
//                binding.tvLocation.text = addresses!![0].countryCode
                binding.tv.text =
                    addresses!![0].subLocality.trim() + " \n" +//Gandhi Nagar
                            addresses[0].locale + " \n" +//en_US
                            addresses[0].locality + " \n" +//Haldia
                            addresses[0].adminArea + " \n" +//West Bengal
                            addresses[0].countryCode + " \n" +//IN
                            addresses[0].countryName + " \n" +//India
                            addresses[0].extras + " \n" +
                            addresses[0].subAdminArea + " \n" +//Medinipur Division
                            addresses[0].featureName + " \n" +//Unnamed Road
                            addresses[0].maxAddressLineIndex + " \n" +//0
                            addresses[0].phone + " \n" +
                            addresses[0].postalCode + " \n" +//721631
                            addresses[0].premises + " \n" +
                            addresses[0].subThoroughfare + " \n" +
                            addresses[0].thoroughfare + " \n" +//Unnamed Road
                            addresses[0].url + " \n" +
                            addresses[0].latitude + " \n" +
                            addresses[0].longitude + " \n" +
                            addresses[0].getAddressLine(0)
            }

        }


    }

}