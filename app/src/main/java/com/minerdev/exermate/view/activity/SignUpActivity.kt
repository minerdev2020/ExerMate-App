package com.minerdev.exermate.view.activity

import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.minerdev.exermate.databinding.ActivitySignUpBinding
import com.minerdev.exermate.network.BaseCallBack
import com.minerdev.exermate.network.UserService
import com.minerdev.exermate.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    private fun trySignUp(
        userEmail: String,
        userPw: String,
        nickname: String
    ) {
        val callBack = BaseCallBack(
            { _: Int, response: String ->
                val data = JSONObject(response)
                Log.d(Constants.TAG, "tryRegister response : " + data.getString("message"))
                finish()
            },
            { code: Int, response: String ->
                val data = JSONObject(response)
                Log.d(Constants.TAG, "tryRegister response : " + data.getString("message"))
                when (code) {
                    409 -> Toast.makeText(this, "이미 존재하는 계정입니다!", Toast.LENGTH_SHORT).show()
                    else -> {
                        Toast.makeText(this, response, Toast.LENGTH_LONG).show()
                    }
                }
                finish()
            },
            { error: Throwable ->
                Log.d(Constants.TAG, "tryRegister error : " + error.localizedMessage)
                finish()
            }
        )

        CoroutineScope(Dispatchers.IO).launch {
            UserService.signUp(userEmail, userPw, nickname, callBack)
        }
    }

    private fun setupButtons() {
        binding.btnSignUp.setOnClickListener {
            val userEmail = binding.etEmail.text.toString()
            val userPw = binding.etPw.text.toString()
            val nickname = binding.etNickname.text.toString()

            if (userEmail.isNotBlank() && userPw.isNotBlank() && nickname.isNotBlank()) {
                if (Constants.APPLICATION_MODE == Constants.DEV_MODE_WITHOUT_SERVER) {
                    finish()

                } else {
                    trySignUp(userEmail, userPw, nickname)
                }

            } else {
                Toast.makeText(this, "필수 입력사항을 전부 기입해주세요!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupEditTexts() {
        binding.etEmail.filters = arrayOf(InputFilter { charSequence, _, _, _, _, _ ->
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