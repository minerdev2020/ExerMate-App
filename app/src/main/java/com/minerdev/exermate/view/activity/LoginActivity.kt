package com.minerdev.exermate.view.activity

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
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
import com.minerdev.exermate.model.WalkRecord
import com.minerdev.exermate.network.BaseCallBack
import com.minerdev.exermate.network.service.AuthService
import com.minerdev.exermate.network.service.UserService
import com.minerdev.exermate.utils.Constants
import com.minerdev.exermate.utils.DBHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    private lateinit var dbHelper: DBHelper
    private lateinit var sqlDB: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        dbHelper = DBHelper(this)
        sqlDB = dbHelper.writableDatabase

        if (Constants.APPLICATION_MODE == Constants.DEV_MODE && Constants.SERVER_IP_ADDRESS.isBlank() || Constants.SERVER_HTTP_PORT.isBlank() || Constants.SERVER_WEB_SOCKET_PORT.isBlank()) {
            startActivity(Intent(this, DevNetworkSettingActivity::class.java))
            finish()
            return
        }

        if (checkLoginStatus()) {
            val sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
            val userEmail = sharedPreferences.getString("userEmail", "") ?: ""

            Constants.USER_EMAIL = userEmail

            Log.d(Constants.TAG, "login : $userEmail")
            startActivity(Intent(this, MainActivity::class.java))

        setupButtons()
        setupEditTexts()
    }

    private fun tryLogin(userEmail: String, userPw: String) {
        Log.d(Constants.TAG, "try login : $userEmail")

        val readCallBack = BaseCallBack(
            { code, response ->
                val jsonObject = JSONObject(response)
                val result = jsonObject.getString("result")
                val format = Json { ignoreUnknownKeys = true }
                val walkRecords = format.decodeFromString<List<WalkRecord>>(result)

                for (walkRecord in walkRecords) {
                    val contentValues = ContentValues().apply {
                        put("createdAt", walkRecord.createdAt)
                        put("stepCount", walkRecord.stepCount)
                    }

                    sqlDB.insert("walkRecords", null, contentValues)
                }

                startActivity(Intent(this, MainActivity::class.java))
            }
        )

        val loginCallBack = BaseCallBack(
            { _: Int, response: String ->
                val jsonResponse = JSONObject(response)
                val result = jsonResponse.getBoolean("success")
                if (result) {
                    val sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("userEmail", userEmail)
                    editor.apply()

                    Constants.USER_EMAIL = userEmail

                    binding.etEmail.setText("")
                    binding.etPw.setText("")

                    CoroutineScope(Dispatchers.IO).launch {
                        UserService.readAllWalkRecords(readCallBack)
                    }

                } else {
                    binding.etPw.setText("")
                    Toast.makeText(this, "??????????????? ??????????????? ?????????????????????!", Toast.LENGTH_SHORT).show()
                }
            },
            { code: Int, response: String ->
                when (code) {
                    400 -> {
                        binding.etPw.setText("")
                        Toast.makeText(this, "??????????????? ??????????????? ?????????????????????!", Toast.LENGTH_SHORT).show()
                    }
                    401 -> Toast.makeText(this, "?????? ???????????? ?????? ????????? ??????????????????!", Toast.LENGTH_SHORT).show()
                    404 -> Toast.makeText(this, "????????? ???????????? ????????????!", Toast.LENGTH_SHORT).show()
                    else -> {
                        Toast.makeText(this, response, Toast.LENGTH_LONG).show()
                    }
                }
            }
        )

        CoroutineScope(Dispatchers.IO).launch {
            AuthService.login(userEmail, userPw, loginCallBack)
        }
    }

    private fun checkLoginStatus(): Boolean {
        val sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
        return sharedPreferences != null && sharedPreferences.contains("userEmail")
    }

    private fun setupButtons() {
        binding.btnLogin.setOnClickListener {
            val userEmail = binding.etEmail.text.toString()
            val userPw = binding.etPw.text.toString()

            when {
                userEmail.isBlank() -> {
                    Toast.makeText(this, "???????????? ??????????????????!", Toast.LENGTH_LONG).show()
                }
                userPw.isBlank() -> {
                    Toast.makeText(this, "??????????????? ??????????????????!", Toast.LENGTH_LONG).show()
                }
                else -> {
                    if (Constants.APPLICATION_MODE == Constants.DEV_MODE_WITHOUT_SERVER) {
                        startActivity(Intent(this, MainActivity::class.java))

                        val sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("userEmail", userEmail)
                        editor.apply()

                        binding.etEmail.setText("")
                        binding.etPw.setText("")

                        Constants.USER_EMAIL = userEmail

                    } else {
                        tryLogin(userEmail, userPw)
                    }
                }
            }
        }

        binding.btnSignUp.setOnClickListener {
            binding.etEmail.setText("")
            binding.etPw.setText("")
            startActivity(Intent(this, SignUpActivity::class.java))
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