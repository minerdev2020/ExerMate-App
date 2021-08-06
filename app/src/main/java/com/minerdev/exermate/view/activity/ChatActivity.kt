package com.minerdev.exermate.view.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.minerdev.exermate.adapter.ChatAdapter
import com.minerdev.exermate.databinding.ActivityChatBinding
import com.minerdev.exermate.model.ChatLog

class ChatActivity : AppCompatActivity() {
    private val adapter = ChatAdapter()
    private val binding by lazy { ActivityChatBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "단체 채팅방"

        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter

        val chatLogs = ArrayList<ChatLog>(
            listOf(
                ChatLog(
                    text = "운동 싫어 님이 채팅방에 입장하셨으빈다.",
                    type = 0
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 2
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 2
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 2
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 2
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 2
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 2
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 2
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 2
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 2
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 2
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 2
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 1
                ),
                ChatLog(
                    roomId = 1,
                    fromId = 1,
                    createdAt = "오전 12:01",
                    text = "hihihihihihihihihi",
                    type = 2
                ),
            )
        )
        adapter.initChatLogs(chatLogs)
        binding.recyclerView.scrollToPosition(adapter.itemCount - 1)

        binding.btnSend.setOnClickListener {

            adapter.updateChatLogs(
                ChatLog(
                    createdAt = "오후 11:09",
                    text = binding.etChat.text.toString(),
                    type = 2
                )
            )
            binding.recyclerView.scrollToPosition(adapter.itemCount - 1)
        }
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