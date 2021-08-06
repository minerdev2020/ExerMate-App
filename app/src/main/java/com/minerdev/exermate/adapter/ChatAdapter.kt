package com.minerdev.exermate.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.minerdev.exermate.databinding.ItemChatBinding
import com.minerdev.exermate.databinding.ItemMyChatBinding
import com.minerdev.exermate.databinding.ItemSystemChatBinding
import com.minerdev.exermate.model.ChatLog

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    companion object {
        const val SYSTEM_CHAT_ITEM = 0
        const val CHAT_ITEM = 1
        const val MY_CHAT_ITEM = 2
    }

    private val chatLogs = ArrayList<ChatLog>()

    fun initChatLogs(chatLogs: ArrayList<ChatLog>) {
        this.chatLogs.addAll(chatLogs)
        notifyDataSetChanged()
    }

    fun updateChatLogs(chatLog: ChatLog) {
        chatLogs.add(chatLog)
        notifyItemInserted(chatLogs.size - 1)
    }

    override fun getItemViewType(position: Int): Int {
        return chatLogs[position].type.toInt()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            CHAT_ITEM -> {
                val binding = ItemChatBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                ItemChatViewHolder(binding)
            }

            MY_CHAT_ITEM -> {
                val binding = ItemMyChatBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                ItemMyChatViewHolder(binding)
            }

            else -> {
                val binding = ItemSystemChatBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                ItemSystemChatViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(chatLogs[position])
    }

    override fun getItemCount(): Int {
        return chatLogs.size
    }

    abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(chatLog: ChatLog)
    }

    class ItemSystemChatViewHolder(private val binding: ItemSystemChatBinding) :
        ViewHolder(binding.root) {
        override fun bind(chatLog: ChatLog) {
            binding.tvChat.text = chatLog.text
        }
    }

    class ItemChatViewHolder(private val binding: ItemChatBinding) : ViewHolder(binding.root) {
        override fun bind(chatLog: ChatLog) {
            binding.tvCreatedAt.text = chatLog.createdAt
            binding.tvChat.text = chatLog.text
        }
    }

    class ItemMyChatViewHolder(private val binding: ItemMyChatBinding) : ViewHolder(binding.root) {
        override fun bind(chatLog: ChatLog) {
            binding.tvCreatedAt.text = chatLog.createdAt
            binding.tvChat.text = chatLog.text
        }
    }
}

