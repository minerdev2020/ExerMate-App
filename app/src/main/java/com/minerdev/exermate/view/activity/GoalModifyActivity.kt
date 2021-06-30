package com.minerdev.exermate.view.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.minerdev.exermate.R
import com.minerdev.exermate.databinding.ActivityGoalModifyBinding
import com.minerdev.exermate.view.fragment.TodayGoalFragment

class GoalModifyActivity : AppCompatActivity() {
    private val binding by lazy { ActivityGoalModifyBinding.inflate(layoutInflater) }
    private val items = listOf("걷기", "달리기", "사이클")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.spinnerType.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)

        binding.btnOk.setOnClickListener {
            super.finish()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        val intent = intent
        val mode = intent.getIntExtra("mode", 0)
        if (mode == TodayGoalFragment.ADD_MODE) {
            // 추가 코드
            supportActionBar?.title = "목표 추가"

        } else {
            // 수정 코드
            supportActionBar?.title = "목표 수정"
            val id = intent.getIntExtra("id", 0)
        }
    }

    override fun finish() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("경고")
        builder.setIcon(R.drawable.ic_round_warning_24)
        builder.setMessage("작성하신 내용이 저장되지않습니다.\n정말 뒤로가시겠습니까?")
        builder.setPositiveButton("네") { _, _ ->
            super.finish()
        }
        builder.setNegativeButton("아니요") { _, _ ->
            return@setNegativeButton
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            else -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}