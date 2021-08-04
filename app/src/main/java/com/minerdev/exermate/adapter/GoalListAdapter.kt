package com.minerdev.exermate.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.minerdev.exermate.databinding.ItemGoalBinding
import com.minerdev.exermate.model.Goal

class GoalListAdapter(diffCallback: DiffCallback) :
    ListAdapter<Goal, GoalListAdapter.ViewHolder>(diffCallback) {
    lateinit var clickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGoalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    operator fun get(position: Int): Goal {
        return getItem(position)
    }

    interface OnItemClickListener {
        fun onButtonClick(viewHolder: ViewHolder, view: View, position: Int)
    }

    class ViewHolder(
        private val binding: ItemGoalBinding,
        listener: OnItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imageBtnMore.setOnClickListener {
                listener.onButtonClick(this, itemView, adapterPosition)
            }
        }

        fun bind(goal: Goal) {
            binding.tvCreatedAt.text = goal.createdAt
            binding.tvState.text = when {
                goal.state.toInt() == 0 -> "진행 중"
                goal.state.toInt() == 1 -> "달성"
                else -> "미달성"
            }
            binding.tvType.text = goal.type
            binding.tvProgress.text = "${goal.current} km / ${goal.goal} km"

            binding.progressBar.progress = 50 // (goal.current / goal.goal).toInt() * 100
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Goal>() {
        override fun areItemsTheSame(oldItem: Goal, newItem: Goal): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Goal, newItem: Goal): Boolean {
            return oldItem == newItem
        }
    }
}