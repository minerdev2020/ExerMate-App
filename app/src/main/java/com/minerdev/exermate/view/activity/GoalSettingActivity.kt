package com.minerdev.exermate.view.activity

import android.app.AlertDialog
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.minerdev.exermate.R
import com.minerdev.exermate.databinding.ActivityGoalSettingBinding

class GoalSettingActivity : AppCompatActivity() {
    private val binding by lazy { ActivityGoalSettingBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = "목표 설정"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun finish() {
        val builder = AlertDialog.Builder(this).apply {
            setTitle("경고")
            setIcon(R.drawable.ic_round_warning_24)
            setMessage("작성하신 내용이 저장되지않습니다.\n정말 뒤로가시겠습니까?")
            setPositiveButton("예") { _, _ ->
                super.finish()
            }
            setNegativeButton("아니요") { _, _ ->
                return@setNegativeButton
            }
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