package com.minerdev.exermate.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.minerdev.exermate.databinding.ItemChatBinding

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    private val chatLogs = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChatBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(chatLogs[position])
    }

    override fun getItemCount(): Int {
        return chatLogs.size
    }

    class ViewHolder(private val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chatLog: String) {
            binding.tvChat.text = chatLog
        }
    }
}