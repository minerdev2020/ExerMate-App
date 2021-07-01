package com.minerdev.exermate.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.minerdev.exermate.databinding.ActivityLoginBinding
import com.minerdev.exermate.utils.Constants
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (checkLoginStatus()) {
            val sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
            val id = sharedPreferences.getString("id", "") ?: ""
            val userId = sharedPreferences.getString("user_id", "") ?: ""

            Constants.ID = id
            Constants.USER_ID = userId

            Log.d(Constants.TAG, "login : $userId")
            startActivity(Intent(this, MainActivity::class.java))
        }

        setupButtons()
        setupEditTexts()
    }

    private fun tryLogin(userId: String, userPw: String) {
        AuthService.login(userId, userPw,
            { _: Int, response: String ->
                val jsonResponse = JSONObject(response)
                Log.d(Constants.TAG, "tryLogin response : " + jsonResponse.getString("message"))

                val data = JSONObject(jsonResponse.getString("data"))
                Log.d(Constants.TAG, "tryLogin response : " + jsonResponse.getString("data"))

                val sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("id", data.getInt("id").toString())
                editor.putString("user_id", data.getString("user_id"))
                editor.putString("type_id", data.getString("type_id"))
                editor.apply()

                Constants.ID = data.getInt("id").toString()
                Constants.USER_ID = data.getString("user_id")

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            },
            { code: Int, response: String ->
                val data = JSONObject(response)
                Log.d(Constants.TAG, "tryLogin response : " + data.getString("message"))
                when (code) {
                    400 -> Toast.makeText(this, "이메일이나 비밀번호가 잘못되었습니다!", Toast.LENGTH_LONG).show()
                    401 -> Toast.makeText(this, "다른 기기에서 이미 로그인 되어있습니다!", Toast.LENGTH_LONG).show()
                    404 -> Toast.makeText(this, "계정이 존재하지 않습니다!", Toast.LENGTH_LONG).show()
                    else -> {
                    }
                }
            },
            { error: Throwable ->
                Log.d(Constants.TAG, "tryLogin error : " + error.localizedMessage)
            }
        )
    }

    private fun checkLoginStatus(): Boolean {
        val sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
        return sharedPreferences != null && sharedPreferences.contains("user_id")
    }

    private fun setupButtons() {
        binding.btnLogin.setOnClickListener {
            val userId = binding.etId.text.toString()
            val userPw = binding.etPw.text.toString()

            when {
                userId.isEmpty() -> {
                    Toast.makeText(this, "이메일을 입력해주세요!", Toast.LENGTH_LONG).show()
                }
                userPw.isEmpty() -> {
                    Toast.makeText(this, "비밀번호를 입력해주세요!", Toast.LENGTH_LONG).show()
                }
                else -> {
                    tryLogin(userId, userPw)
                }
            }
        }

        binding.btnSignIn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
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

        binding.etPw.setOnEditorActionListener(TextView.OnEditorActionListener { _, i, keyEvent ->
            if (keyEvent != null && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER || i == EditorInfo.IME_ACTION_DONE) {
                val manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(
                    currentFocus?.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )

                binding.btnLogin.performClick()
                return@OnEditorActionListener true
            }
            false
        })
    }
}