package com.minerdev.exermate.view.activity

import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.minerdev.exermate.databinding.ActivitySignUpBinding
import com.minerdev.exermate.network.AuthService
import com.minerdev.exermate.utils.Constants
import org.json.JSONObject
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySignUpBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = "회원가입"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupButtons()
        setupEditTexts()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            else -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun tryRegister(
        userId: String,
        userPw: String,
        nickname: String
    ) {
        if (userId.isNotBlank() && userPw.isNotBlank() && nickname.isNotBlank()) {
            AuthService.register(userId, userPw, nickname,
                { _: Int, response: String ->
                    val data = JSONObject(response)
                    Log.d(Constants.TAG, "tryRegister response : " + data.getString("message"))
                },
                { code: Int, response: String ->
                    val data = JSONObject(response)
                    Log.d(Constants.TAG, "tryRegister response : " + data.getString("message"))
                    when (code) {
                        409 -> Toast.makeText(this, "이미 존재하는 계정입니다!", Toast.LENGTH_SHORT)
                            .show()
                        else -> {
                        }
                    }
                },
                { error: Throwable ->
                    Log.d(Constants.TAG, "tryRegister error : " + error.localizedMessage)
                }
            )

        } else {
            Toast.makeText(this, "필수 입력사항을 전부 기입해주세요!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupButtons() {
        binding.btnSignUp.setOnClickListener {
            val userId = binding.etId.text.toString()
            val userPw = binding.etPw.text.toString()
            val nickname = binding.etNickname.text.toString()
            tryRegister(userId, userPw, nickname)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupEditTexts() {
        binding.etId.filters = arrayOf(InputFilter { charSequence, _, _, _, _, _ ->
            val pattern = Pattern.compile("^[a-zA-Z0-9]*$")
            if (charSequence == "" || pattern.matcher(charSequence).matches()) {
                charSequence
            } else ""
        }, InputFilter.LengthFilter(8))

        binding.etPw.filters = arrayOf(InputFilter { charSequence, _, _, _, _, _ ->
            val pattern = Pattern.compile("^[a-zA-Z0-9]*$")
            if (charSequence == "" || pattern.matcher(charSequence).matches()) {
                charSequence
            } else ""
        }, InputFilter.LengthFilter(8))
    }
}