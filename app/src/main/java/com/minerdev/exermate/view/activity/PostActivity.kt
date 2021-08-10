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

        val intent = intent
        supportActionBar?.title = intent.getStringExtra("title") ?: "제목입니다"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnJoin.setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
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