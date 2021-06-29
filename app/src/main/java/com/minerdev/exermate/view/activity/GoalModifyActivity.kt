package com.minerdev.exermate.view.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.minerdev.exermate.databinding.ActivityGoalModifyBinding

class GoalModifyActivity : AppCompatActivity() {
    private val binding by lazy { ActivityGoalModifyBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            else -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}