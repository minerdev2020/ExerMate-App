package com.minerdev.exermate.view.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.minerdev.exermate.R
import com.minerdev.exermate.databinding.ActivityEditPostBinding

class EditPostActivity : AppCompatActivity() {
    private val binding by lazy { ActivityEditPostBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = "모집글 작성"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun finish() {
        val builder = AlertDialog.Builder(this).apply {
            setTitle("경고")
            setIcon(R.drawable.ic_round_warning_24)
            setMessage("작성하신 내용이 저장되지않습니다.\n정말 뒤로가시겠습니까?")
            setPositiveButton("네") { _, _ ->
                super.finish()
            }
            setNegativeButton("아니요") { _, _ ->
                return@setNegativeButton
            }
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_edit_post, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.toolbar_send_post -> {
                super.finish()
            }
            else -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}