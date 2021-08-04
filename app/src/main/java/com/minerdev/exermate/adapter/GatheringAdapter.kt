package com.minerdev.exermate.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.minerdev.exermate.databinding.ItemGatheringBinding
import com.minerdev.exermate.model.Gathering

class GatheringAdapter(diffCallBack: DiffCallBack) :
    ListAdapter<Gathering, GatheringAdapter.ViewHolder>(diffCallBack) {
    lateinit var clickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGatheringBinding.inflate(
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
        private val binding: ItemGatheringBinding,
        listener: OnItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.gatheringItemLayout.setOnClickListener {
                listener.onButtonClick(this, itemView, adapterPosition)
            }
        }

        fun bind(gathering: Gathering) {
            binding.tvCreatedAt.text = gathering.createdAt
            binding.tvTitle.text = gathering.title
        }
    }

    class DiffCallBack : DiffUtil.ItemCallback<Gathering>() {
        override fun areItemsTheSame(oldItem: Gathering, newItem: Gathering): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Gathering, newItem: Gathering): Boolean {
            return oldItem == newItem
        }
    }
}