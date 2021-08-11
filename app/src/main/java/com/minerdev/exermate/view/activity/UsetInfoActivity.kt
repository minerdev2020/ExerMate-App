package com.minerdev.exermate.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.minerdev.exermate.databinding.ActivityUsetInfoBinding

class UsetInfoActivity : AppCompatActivity() {
    private val binding by lazy { ActivityUsetInfoBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}