package com.minerdev.exermate.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.minerdev.exermate.databinding.ItemChatRoomBinding
import com.minerdev.exermate.model.ChatRoom
import com.minerdev.exermate.utils.Time

class ChatRoomAdapter(diffCallBack: DiffCallBack) :
    ListAdapter<ChatRoom, ChatRoomAdapter.ViewHolder>(diffCallBack) {
    lateinit var clickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChatRoomBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface OnItemClickListener {
        fun onButtonClick(viewHolder: ViewHolder, view: View, position: Int)
    }

    class ViewHolder(
        private val binding: ItemChatRoomBinding,
        listener: OnItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.chatRoomItemLayout.setOnClickListener {
                listener.onButtonClick(this, itemView, adapterPosition)
            }
        }

        fun bind(chatRoom: ChatRoom) {
            binding.tvCreatedAt.text = Time.convertTimestampToHMS(chatRoom.createdAt)
            binding.tvName.text = chatRoom.name
        }
    }

    class DiffCallBack : DiffUtil.ItemCallback<ChatRoom>() {
        override fun areItemsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
            return oldItem == newItem
        }
    }
}