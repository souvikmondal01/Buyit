package com.buyit.buyit.start.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.buyit.buyit.R
import com.buyit.buyit.databinding.ActivityStartBinding
import com.buyit.buyit.utils.setStatusBarColor


class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setStatusBarColor(R.color.orange_500)

        binding.cvBackArrow.setOnClickListener { finish() }
        binding.cvSettings.setOnClickListener {}
        binding.lavWave.playAnimation()
    }

}
