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
                    putExtra("roomId", adapter.currentList[position].id)
                }
                startActivity(intent)
            }
        }

        viewModel.postList.observe(viewLifecycleOwner, adapter::submitList)
        viewModel.loadPosts()

        adapter.submitList(
            listOf(
                Post(
                    id = 1,
                    createdAt = "21.06.30",
                    title = "한국 여자 배구 팀의 도쿄 올림픽 경기를 보고 전세계가 극찬을 아끼지 않은 이유"
                ),
                Post(
                    id = 2,
                    createdAt = "21.06.30",
                    title = "한국 여자 배구 팀의 도쿄 올림픽 경기를 보고 전세계가 극찬을 아끼지 않은 이유"
                ),
                Post(
                    id = 3,
                    createdAt = "21.06.30",
                    title = "한국 여자 배구 팀의 도쿄 올림픽 경기를 보고 전세계가 극찬을 아끼지 않은 이유"
                ),
                Post(
                    id = 4,
                    createdAt = "21.06.30",
                    title = "한국 여자 배구 팀의 도쿄 올림픽 경기를 보고 전세계가 극찬을 아끼지 않은 이유"
                ),
                Post(
                    id = 5,
                    createdAt = "21.06.30",
                    title = "한국 여자 배구 팀의 도쿄 올림픽 경기를 보고 전세계가 극찬을 아끼지 않은 이유"
                ),
                Post(
                    id = 6,
                    createdAt = "21.06.30",
                    title = "한국 여자 배구 팀의 도쿄 올림픽 경기를 보고 전세계가 극찬을 아끼지 않은 이유"
                ),
                Post(
                    id = 7,
                    createdAt = "21.06.30",
                    title = "한국 여자 배구 팀의 도쿄 올림픽 경기를 보고 전세계가 극찬을 아끼지 않은 이유"
                ),
                Post(
                    id = 8,
                    createdAt = "21.06.30",
                    title = "한국 여자 배구 팀의 도쿄 올림픽 경기를 보고 전세계가 극찬을 아끼지 않은 이유"
                ),
                Post(
                    id = 9,
                    createdAt = "21.06.30",
                    title = "한국 여자 배구 팀의 도쿄 올림픽 경기를 보고 전세계가 극찬을 아끼지 않은 이유"
                ),
                Post(
                    id = 10,
                    createdAt = "21.06.30",
                    title = "한국 여자 배구 팀의 도쿄 올림픽 경기를 보고 전세계가 극찬을 아끼지 않은 이유"
                ),
                Post(
                    id = 11,
                    createdAt = "21.06.30",
                    title = "한국 여자 배구 팀의 도쿄 올림픽 경기를 보고 전세계가 극찬을 아끼지 않은 이유"
                ),
                Post(
                    id = 12,
                    createdAt = "21.06.30",
                    title = "한국 여자 배구 팀의 도쿄 올림픽 경기를 보고 전세계가 극찬을 아끼지 않은 이유"
                )
            )
        )

        setHasOptionsMenu(true)

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