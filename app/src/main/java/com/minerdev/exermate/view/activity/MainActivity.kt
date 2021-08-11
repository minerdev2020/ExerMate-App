package com.minerdev.exermate.view.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.viewpager2.widget.ViewPager2
import com.minerdev.exermate.R
import com.minerdev.exermate.adapter.SectionPageAdapter
import com.minerdev.exermate.databinding.ActivityMainBinding
import com.minerdev.exermate.utils.Constants
import com.minerdev.exermate.view.fragment.GatheringFragment
import com.minerdev.exermate.view.fragment.SettingFragment
import com.minerdev.exermate.view.fragment.TodayGoalFragment

class MainActivity : AppCompatActivity() {
    private var backPressedTime: Long = 0

    private val adapter = SectionPageAdapter(this)
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupViewPager()
        setupBottomNavigationView()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = adapter.getPageTitle(0)
    }

    override fun onBackPressed() {
        val tempTime = System.currentTimeMillis()
        val intervalTime = tempTime - backPressedTime

        if (intervalTime in 0..Constants.FINISH_INTERVAL_TIME) {
            ActivityCompat.finishAffinity(this)

        } else {
            backPressedTime = tempTime
            Toast.makeText(this, "한 번 더 누르시면 앱이 종료 됩니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupViewPager() {
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.bottomNav.menu.getItem(position).isChecked = true
                supportActionBar?.title = adapter.getPageTitle(position)
            }
        })

        adapter.addFragment(TodayGoalFragment(), "오늘의 목표")
        adapter.addFragment(GatheringFragment(), "운동 모임 찾기")
        adapter.addFragment(SettingFragment(), "설정")
        binding.viewPager.adapter = adapter

        setupBottomNavigationView()
    }

    private fun setupBottomNavigationView() {
        binding.bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tab_goal -> binding.viewPager.currentItem = 0
                R.id.tab_gathering -> binding.viewPager.currentItem = 1
                R.id.tab_settings -> binding.viewPager.currentItem = 2
                else -> {
                }
            }
            true
        }
    }
}