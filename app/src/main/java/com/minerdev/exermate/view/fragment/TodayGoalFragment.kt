package com.minerdev.exermate.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.minerdev.exermate.R
import com.minerdev.exermate.adapter.GoalListAdapter
import com.minerdev.exermate.databinding.FragmentTodayGoalBinding
import com.minerdev.exermate.model.Goal
import com.minerdev.exermate.view.activity.GoalModifyActivity
import com.minerdev.exermate.view.dialog.DropdownDialog

class TodayGoalFragment : Fragment() {
    companion object {
        const val ADD_MODE = 0
        const val MODIFY_MODE = 1
    }

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
                val dialog = DropdownDialog(listOf("목표 수정", "목표 삭제")) { _, _, position, _ ->
                    when (position) {
                        0 -> {
                            val intent = Intent(context, GoalModifyActivity::class.java)
                            intent.putExtra("mode", MODIFY_MODE)
                            intent.putExtra("id", adapter[position].id)
                            startActivity(intent)
                        }

                        1 -> {
                            val builder = AlertDialog.Builder(requireContext())
                            builder.setTitle("경고")
                            builder.setIcon(R.drawable.ic_round_warning_24)
                            builder.setMessage("정말 삭제 하시겠습니까?")
                            builder.setPositiveButton("네") { _, _ ->
//                                viewModel.deleteItem(adapter[position].id)
                            }
                            builder.setNegativeButton("아니요") { _, _ ->
                                return@setNegativeButton
                            }

                            val alertDialog = builder.create()
                            alertDialog.show()
                        }

                        else -> Toast.makeText(context, "Unknown item!", Toast.LENGTH_LONG).show()
                    }
                }
                dialog.show(requireActivity().supportFragmentManager, "GoalModifyDropdownDialog")
            }
        }

        adapter.submitList(
            listOf(
                Goal(id = 1, createdAt = "21.06.30", type = "달리기"),
                Goal(id = 2, createdAt = "21.06.30", type = "달리기"),
                Goal(id = 3, createdAt = "21.06.30", type = "달리기"),
                Goal(id = 4, createdAt = "21.06.30", type = "달리기"),
                Goal(id = 5, createdAt = "21.06.30", type = "달리기"),
                Goal(id = 6, createdAt = "21.06.30", type = "달리기"),
                Goal(id = 7, createdAt = "21.06.30", type = "달리기"),
                Goal(id = 8, createdAt = "21.06.30", type = "달리기"),
                Goal(id = 9, createdAt = "21.06.30", type = "달리기"),
                Goal(id = 10, createdAt = "21.06.30", type = "달리기")
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
                intent.putExtra("mode", ADD_MODE)
                startActivity(intent)
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }
}