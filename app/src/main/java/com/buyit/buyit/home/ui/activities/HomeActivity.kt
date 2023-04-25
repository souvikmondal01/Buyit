package com.buyit.buyit.home.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.TooltipCompat
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.buyit.buyit.R
import com.buyit.buyit.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.orange_700)

//        bind bottom_navigation_view with fragments
        val navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNavigation.setupWithNavController(navController)

        binding.bottomNavigation
            .menu.forEach {
                TooltipCompat.setTooltipText(binding.bottomNavigation.findViewById(it.itemId), null)
            }


    }


}