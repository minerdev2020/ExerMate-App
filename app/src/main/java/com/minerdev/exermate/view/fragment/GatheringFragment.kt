package com.minerdev.exermate.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.minerdev.exermate.R
import com.minerdev.exermate.adapter.PostAdapter
import com.minerdev.exermate.databinding.FragmentGatheringBinding
import com.minerdev.exermate.model.Post
import com.minerdev.exermate.view.activity.EditPostActivity
import com.minerdev.exermate.view.activity.PostActivity
import com.minerdev.exermate.viewmodel.GatheringViewModel

class GatheringFragment : Fragment() {
    private val adapter = PostAdapter(PostAdapter.DiffCallBack())
    private val binding by lazy { FragmentGatheringBinding.inflate(layoutInflater) }
    private val viewModel: GatheringViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                manager.orientation
            )
        )
        binding.recyclerView.adapter = adapter

        adapter.clickListener = object : PostAdapter.OnItemClickListener {
            override fun onButtonClick(
                viewHolder: PostAdapter.ViewHolder,
                view: View,
                position: Int
            ) {
                val intent = Intent(requireContext(), PostActivity::class.java).apply {
                    putExtra("postId", adapter.currentList[position].id)
                }
                startActivity(intent)
            }
        }

        viewModel.postList.observe(viewLifecycleOwner, adapter::submitList)
        viewModel.loadPosts()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_toolbar_gathering, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.toolbar_create_post -> {
                startActivity(Intent(requireContext(), EditPostActivity::class.java))
            }
            else -> requireActivity().finish()
        }

        return super.onOptionsItemSelected(item)
    }
}