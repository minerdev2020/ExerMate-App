package com.minerdev.exermate.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.minerdev.exermate.databinding.ActivityDevNetworkSettingBinding
import com.minerdev.exermate.utils.Constants

class DevNetworkSettingActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDevNetworkSettingBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (Constants.APPLICATION_MODE != Constants.DEV_MODE) {
            finishAffinity()
        }

        binding.btnExit.setOnClickListener {
            finishAffinity()
        }

        binding.btnOk.setOnClickListener {
            Constants.SERVER_IP_ADDRESS = binding.etIp.text.toString()
            Constants.SERVER_HTTP_PORT = binding.etPort.text.toString()
            Constants.SERVER_WEB_SOCKET_PORT = "520"

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}