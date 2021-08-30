package com.minerdev.exermate.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.minerdev.exermate.R
import com.minerdev.exermate.databinding.*
import com.minerdev.exermate.model.ChatLog
import com.minerdev.exermate.model.User
import com.minerdev.exermate.network.LoadImage
import com.minerdev.exermate.utils.Time
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    companion object {
        const val SYSTEM_CHAT_ITEM = 0
        const val MY_CHAT_ITEM = 1
        const val MY_CHAT_PHOTO_ITEM = 2
        const val CHAT_START_ITEM = 3
        const val CHAT_ITEM = 4
        const val CHAT_START_PHOTO_ITEM = 5
        const val CHAT_PHOTO_ITEM = 6
    }

    lateinit var clickListener: (urlStr: String) -> Unit

    private val memberInfo = HashMap<String, User>()
    private val chatLogs = ArrayList<ChatLog>()

    fun initChatRoom(members: ArrayList<User>) {
        for (member in members) {
            memberInfo[member.email] = member
        }
    }

    fun initChatLogs(chatLogs: ArrayList<ChatLog>) {
        this.chatLogs.addAll(chatLogs)
        notifyDataSetChanged()
    }

    fun addChatMembers(member: User) {
        memberInfo[member.email] = member
    }

    fun addChatLogs(chatLog: ChatLog) {
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

            CHAT_PHOTO_ITEM -> {
                val binding = ItemChatPhotoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                ItemChatPhotoViewHolder(binding, clickListener)
            }

            MY_CHAT_PHOTO_ITEM -> {
                val binding = ItemMyChatPhotoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                ItemMyChatPhotoViewHolder(binding, clickListener)
            }

            CHAT_START_ITEM -> {
                val binding = ItemChatStartBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                ItemChatStartViewHolder(binding, clickListener)
            }

            CHAT_START_PHOTO_ITEM -> {
                val binding = ItemChatStartPhotoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )

                ItemChatStartPhotoViewHolder(binding, clickListener)
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
        val chatLog = chatLogs[position]

        if (holder is ItemChatStartViewHolder || holder is ItemChatStartPhotoViewHolder) {
            val nickname = memberInfo[chatLog.fromId]?.nickname ?: "(알 수 없음)"
            val profileUrl = memberInfo[chatLog.fromId]?.profileUrl ?: ""
            holder.bind(chatLog, nickname, profileUrl)

        } else if (holder is ItemSystemChatViewHolder) {
            chatLog.text = "${memberInfo[chatLog.fromId]?.nickname ?: "(알 수 없음)"}님이 채팅방에 입장하셨습니다."
            holder.bind(chatLog)

        } else {
            holder.bind(chatLog)
        }
    }

    override fun getItemCount(): Int {
        return chatLogs.size
    }

    abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        open fun bind(chatLog: ChatLog) {}
        open fun bind(chatLog: ChatLog, nickname: String, profileUrl: String) {}
    }

    class ItemSystemChatViewHolder(private val binding: ItemSystemChatBinding) :
        ViewHolder(binding.root) {
        override fun bind(chatLog: ChatLog) {
            binding.tvChat.text = chatLog.text
        }
    }

    class ItemChatViewHolder(private val binding: ItemChatBinding) : ViewHolder(binding.root) {
        override fun bind(chatLog: ChatLog) {
            binding.tvCreatedAt.text = Time.convertTimestampToHMS(chatLog.createdAt)
            binding.tvChat.text = chatLog.text
        }
    }

    class ItemMyChatViewHolder(private val binding: ItemMyChatBinding) : ViewHolder(binding.root) {
        override fun bind(chatLog: ChatLog) {
            binding.tvCreatedAt.text = Time.convertTimestampToHMS(chatLog.createdAt)
            binding.tvChat.text = chatLog.text
        }
    }

    class ItemChatPhotoViewHolder(
        private val binding: ItemChatPhotoBinding,
        private val listener: (urlStr: String) -> Unit
    ) :
        ViewHolder(binding.root) {
        override fun bind(chatLog: ChatLog) {
            binding.tvCreatedAt.text = Time.convertTimestampToHMS(chatLog.createdAt)

            CoroutineScope(Dispatchers.Main).launch {
                val bitmap = withContext(Dispatchers.IO) {
                    LoadImage.get(chatLog.url)
                }
                binding.ivPhoto.setImageBitmap(bitmap)
            }

            binding.ivPhoto.setOnClickListener {
                listener(chatLog.url)
            }
        }
    }

    class ItemMyChatPhotoViewHolder(
        private val binding: ItemMyChatPhotoBinding,
        private val listener: (urlStr: String) -> Unit
    ) :
        ViewHolder(binding.root) {
        override fun bind(chatLog: ChatLog) {
            binding.tvCreatedAt.text = Time.convertTimestampToHMS(chatLog.createdAt)

            CoroutineScope(Dispatchers.Main).launch {
                val bitmap = withContext(Dispatchers.IO) {
                    LoadImage.get(chatLog.url)
                }
                binding.ivPhoto.setImageBitmap(bitmap)
            }

            binding.ivPhoto.setOnClickListener {
                listener(chatLog.url)
            }
        }
    }

    class ItemChatStartViewHolder(
        private val binding: ItemChatStartBinding,
        private val listener: (urlStr: String) -> Unit
    ) :
        ViewHolder(binding.root) {
        override fun bind(chatLog: ChatLog, nickname: String, profileUrl: String) {
            binding.tvNickname.text = nickname
            binding.tvCreatedAt.text = Time.convertTimestampToHMS(chatLog.createdAt)
            binding.tvChat.text = chatLog.text

            if (profileUrl.isBlank()) {
                binding.ivProfile.setImageResource(R.drawable.ic_round_account_circle_24)

            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    val bitmap = withContext(Dispatchers.IO) {
                        LoadImage.get(profileUrl)
                    }
                    binding.ivProfile.setImageBitmap(bitmap)
                }
            }
        }
    }

    class ItemChatStartPhotoViewHolder(
        private val binding: ItemChatStartPhotoBinding,
        private val listener: (urlStr: String) -> Unit
    ) :
        ViewHolder(binding.root) {
        override fun bind(chatLog: ChatLog, nickname: String, profileUrl: String) {
            binding.tvNickname.text = nickname
            binding.tvCreatedAt.text = Time.convertTimestampToHMS(chatLog.createdAt)

            if (profileUrl.isBlank()) {
                binding.ivProfile.setImageResource(R.drawable.ic_round_account_circle_24)

            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    val bitmap = withContext(Dispatchers.IO) {
                        LoadImage.get(profileUrl)
                    }
                    binding.ivProfile.setImageBitmap(bitmap)
                }
            }

            CoroutineScope(Dispatchers.Main).launch {
                val bitmap = withContext(Dispatchers.IO) {
                    LoadImage.get(chatLog.url)
                }
                binding.ivPhoto.setImageBitmap(bitmap)
            }

            binding.ivPhoto.setOnClickListener {
                listener(chatLog.url)
            }
        }
    }
}

