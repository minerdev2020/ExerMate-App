package com.minerdev.exermate.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.minerdev.exermate.databinding.ActivityPostBinding

class PostActivity : AppCompatActivity() {
    private val binding by lazy { ActivityPostBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (intent == null) {
            finish()
        }

        supportActionBar?.title = intent.getStringExtra("title") ?: "제목입니다"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnJoin.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java).apply {
                putExtra("roomId", intent.getIntExtra("roomId", 0))
            }
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            else -> finish()
        }

        return super.onOptionsItemSelected(item)
    }
}