package com.minerdev.exermate.view.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.minerdev.exermate.R
import com.minerdev.exermate.databinding.FragmentTodayGoalBinding

class TodayGoalFragment : Fragment() {
    private val binding by lazy { FragmentTodayGoalBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_toolbar_today_goal, menu)
    }
}