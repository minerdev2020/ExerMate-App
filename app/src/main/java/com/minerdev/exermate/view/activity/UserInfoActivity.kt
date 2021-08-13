package com.minerdev.exermate.view.activity

import android.os.Bundle
import android.text.InputFilter
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.minerdev.exermate.R
import com.minerdev.exermate.databinding.ActivityUserInfoBinding
import java.util.regex.Pattern

class UserInfoActivity : AppCompatActivity() {
    private val binding by lazy { ActivityUserInfoBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = "유저정보 수정"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnDone.setOnClickListener {
            val builder = AlertDialog.Builder(this).apply {
                setTitle("확인")
                setIcon(R.drawable.ic_round_help_24)
                setMessage("작성하신 내용으로 수정하시겠습니까?")
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

        setupEditTexts()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            else -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupEditTexts() {
        binding.etId.filters = arrayOf(InputFilter { charSequence, _, _, _, _, _ ->
            val pattern = Pattern.compile("^[a-zA-Z0-9@._]*$")
            if (charSequence == "" || pattern.matcher(charSequence).matches()) {
                charSequence
            } else ""
        }, InputFilter.LengthFilter(30))

        binding.etPw.filters = arrayOf(InputFilter { charSequence, _, _, _, _, _ ->
            val pattern = Pattern.compile("^[a-zA-Z0-9!@#$%^&*+~-]*$")
            if (charSequence == "" || pattern.matcher(charSequence).matches()) {
                charSequence
            } else ""
        }, InputFilter.LengthFilter(20))

        binding.etNickname.filters = arrayOf(InputFilter { charSequence, _, _, _, _, _ ->
            val pattern = Pattern.compile("^[a-zA-Z0-9]*$")
            if (charSequence == "" || pattern.matcher(charSequence).matches()) {
                charSequence
            } else ""
        }, InputFilter.LengthFilter(10))
    }
}