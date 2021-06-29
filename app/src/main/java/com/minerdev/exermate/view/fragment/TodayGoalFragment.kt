package com.minerdev.exermate.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.minerdev.exermate.R
import com.minerdev.exermate.adapter.GoalListAdapter
import com.minerdev.exermate.databinding.FragmentTodayGoalBinding
import com.minerdev.exermate.model.Goal
import com.minerdev.exermate.view.activity.GoalModifyActivity

class TodayGoalFragment : Fragment() {
    private val adapter = GoalListAdapter(GoalListAdapter.DiffCallback())
    private val binding by lazy { FragmentTodayGoalBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter

        adapter.clickListener = object : GoalListAdapter.OnItemClickListener {
            override fun onButtonClick(
                viewHolder: GoalListAdapter.ViewHolder,
                view: View,
                position: Int
            ) {
                showPopupMenu(view, position)
            }
        }

        adapter.submitList(
            listOf(
                Goal(id = 1),
                Goal(id = 2),
                Goal(id = 3),
                Goal(id = 4),
                Goal(id = 5),
                Goal(id = 6),
                Goal(id = 7),
                Goal(id = 8),
                Goal(id = 9),
                Goal(id = 10)
            )
        )

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val item = menu.findItem(R.id.toolbar_add_goal)
        item.isVisible = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toolbar_add_goal -> {
                val intent = Intent(context, GoalModifyActivity::class.java)
                startActivity(intent)
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showPopupMenu(view: View, position: Int) {
        val popupMenu = PopupMenu(requireContext(), view, Gravity.END)
        requireActivity().menuInflater.inflate(R.menu.menu_popup_goal, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            val intent: Intent
            when (menuItem.itemId) {
                R.id.popup_goal_modify -> {
                    intent = Intent(context, GoalModifyActivity::class.java)
                    intent.putExtra("id", adapter[position].id)
                    startActivity(intent)
                }

                R.id.popup_goal_delete -> {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("경고")
                    builder.setIcon(R.drawable.ic_round_warning_24)
                    builder.setMessage("정말 삭제 하시겠습니까?")
                    builder.setPositiveButton("네") { _, _ ->
//                        viewModel.deleteItem(adapter[position].id)
                    }
                    builder.setNegativeButton("아니요") { _, _ ->
                        return@setNegativeButton
                    }

                    val alertDialog = builder.create()
                    alertDialog.show()
                }

                else -> Toast.makeText(context, "popup menu error.", Toast.LENGTH_SHORT).show()
            }
            true
        }

        popupMenu.show()
    }
}