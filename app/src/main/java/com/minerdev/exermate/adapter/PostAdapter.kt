package com.minerdev.exermate.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.minerdev.exermate.databinding.ItemPostBinding
import com.minerdev.exermate.model.Post
import com.minerdev.exermate.utils.Time

class PostAdapter(diffCallBack: DiffCallBack) :
    ListAdapter<Post, PostAdapter.ViewHolder>(diffCallBack) {
    lateinit var clickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPostBinding.inflate(
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
        private val binding: ItemPostBinding,
        listener: OnItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.gatheringItemLayout.setOnClickListener {
                listener.onButtonClick(this, itemView, adapterPosition)
            }
        }

        fun bind(post: Post) {
            binding.tvCreatedAt.text = Time.convertTimestampToString(post.createdAt)
            binding.tvTitle.text = post.title
        }
    }

    class DiffCallBack : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }
}