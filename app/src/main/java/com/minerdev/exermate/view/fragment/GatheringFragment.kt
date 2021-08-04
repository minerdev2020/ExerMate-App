package com.minerdev.exermate.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.minerdev.exermate.R
import com.minerdev.exermate.adapter.GatheringAdapter
import com.minerdev.exermate.databinding.FragmentGatheringBinding
import com.minerdev.exermate.model.Gathering
import com.minerdev.exermate.view.activity.PostActivity

class GatheringFragment : Fragment() {
    private val adapter = GatheringAdapter(GatheringAdapter.DiffCallBack())
    private val binding by lazy { FragmentGatheringBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val manager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter

        adapter.clickListener = object : GatheringAdapter.OnItemClickListener {
            override fun onButtonClick(
                viewHolder: GatheringAdapter.ViewHolder,
                view: View,
                position: Int
            ) {
                startActivity(Intent(requireContext(), PostActivity::class.java))
            }
        }

        adapter.submitList(
            listOf(
                Gathering(id = 1, createdAt = "21.06.30", title = "한국 여자 배구 팀의 도쿄 올림픽 경기를 보고 전세계가 극찬을 아끼지 않은 이유"),
                Gathering(id = 2, createdAt = "21.06.30", title = "한국 여자 배구 팀의 도쿄 올림픽 경기를 보고 전세계가 극찬을 아끼지 않은 이유"),
                Gathering(id = 3, createdAt = "21.06.30", title = "한국 여자 배구 팀의 도쿄 올림픽 경기를 보고 전세계가 극찬을 아끼지 않은 이유"),
                Gathering(id = 4, createdAt = "21.06.30", title = "한국 여자 배구 팀의 도쿄 올림픽 경기를 보고 전세계가 극찬을 아끼지 않은 이유"),
                Gathering(id = 5, createdAt = "21.06.30", title = "한국 여자 배구 팀의 도쿄 올림픽 경기를 보고 전세계가 극찬을 아끼지 않은 이유"),
                Gathering(id = 6, createdAt = "21.06.30", title = "한국 여자 배구 팀의 도쿄 올림픽 경기를 보고 전세계가 극찬을 아끼지 않은 이유"),
                Gathering(id = 7, createdAt = "21.06.30", title = "한국 여자 배구 팀의 도쿄 올림픽 경기를 보고 전세계가 극찬을 아끼지 않은 이유"),
                Gathering(id = 8, createdAt = "21.06.30", title = "한국 여자 배구 팀의 도쿄 올림픽 경기를 보고 전세계가 극찬을 아끼지 않은 이유"),
                Gathering(id = 9, createdAt = "21.06.30", title = "한국 여자 배구 팀의 도쿄 올림픽 경기를 보고 전세계가 극찬을 아끼지 않은 이유"),
                Gathering(id = 10, createdAt = "21.06.30", title = "한국 여자 배구 팀의 도쿄 올림픽 경기를 보고 전세계가 극찬을 아끼지 않은 이유"),
                Gathering(id = 11, createdAt = "21.06.30", title = "한국 여자 배구 팀의 도쿄 올림픽 경기를 보고 전세계가 극찬을 아끼지 않은 이유"),
                Gathering(id = 12, createdAt = "21.06.30", title = "한국 여자 배구 팀의 도쿄 올림픽 경기를 보고 전세계가 극찬을 아끼지 않은 이유")
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
        return super.onOptionsItemSelected(item)
    }
}