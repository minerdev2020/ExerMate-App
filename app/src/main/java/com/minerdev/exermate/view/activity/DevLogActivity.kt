package com.minerdev.exermate.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.minerdev.exermate.databinding.ActivityDevLogBinding
import com.minerdev.exermate.utils.Constants

class DevLogActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDevLogBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (Constants.APPLICATION_MODE != Constants.DEV_MODE) {
            finishAffinity()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}