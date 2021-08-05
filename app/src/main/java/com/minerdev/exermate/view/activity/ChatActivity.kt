package com.minerdev.exermate.view.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.minerdev.exermate.adapter.ChatAdapter
import com.minerdev.exermate.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    private val adapter = ChatAdapter()
    private val binding by lazy { ActivityChatBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            else -> {
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}