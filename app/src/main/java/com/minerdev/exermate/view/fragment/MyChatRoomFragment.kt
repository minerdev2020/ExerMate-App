package com.minerdev.exermate.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.minerdev.exermate.adapter.ChatRoomAdapter
import com.minerdev.exermate.databinding.FragmentMyCharRoomBinding
import com.minerdev.exermate.view.activity.ChatActivity
import com.minerdev.exermate.viewmodel.MyChatRoomViewModel

class MyChatRoomFragment : Fragment() {
    private val adapter = ChatRoomAdapter(ChatRoomAdapter.DiffCallBack())
    private val binding by lazy { FragmentMyCharRoomBinding.inflate(layoutInflater) }
    private val viewModel: MyChatRoomViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(false)

        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                manager.orientation
            )
        )
        binding.recyclerView.adapter = adapter

        adapter.clickListener = object : ChatRoomAdapter.OnItemClickListener {
            override fun onButtonClick(
                viewHolder: ChatRoomAdapter.ViewHolder,
                view: View,
                position: Int
            ) {
                val intent = Intent(requireContext(), ChatActivity::class.java).apply {
                    putExtra("roomId", adapter.currentList[position].id)
                    putExtra("name", adapter.currentList[position].name)
                }
                startActivity(intent)
            }
        }

        viewModel.chatRoomList.observe(viewLifecycleOwner, adapter::submitList)
        viewModel.loadChatRooms()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadChatRooms()
    }
}