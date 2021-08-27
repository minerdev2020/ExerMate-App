package com.minerdev.exermate.view.fragment

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.minerdev.exermate.R
import com.minerdev.exermate.databinding.FragmentSettingBinding
import com.minerdev.exermate.model.User
import com.minerdev.exermate.network.BaseCallBack
import com.minerdev.exermate.network.LoadImage
import com.minerdev.exermate.network.service.AuthService
import com.minerdev.exermate.network.service.UserService
import com.minerdev.exermate.utils.Constants
import com.minerdev.exermate.utils.DBHelper
import com.minerdev.exermate.view.activity.AccountInfoActivity
import com.minerdev.exermate.view.activity.UserInfoActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject

class SettingFragment : Fragment() {
    private val binding by lazy { FragmentSettingBinding.inflate(layoutInflater) }
    private val listMenu = arrayListOf("유저 정보 수정", "로그아웃")

    private lateinit var dbHelper: DBHelper
    private lateinit var sqlDB: SQLiteDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(false)

        binding.listView.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, listMenu)
        binding.listView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                when (position) {
                    0 -> {
                        startActivity(Intent(requireActivity(), AccountInfoActivity::class.java))
                    }
                    1 -> {
                        val builder = AlertDialog.Builder(requireContext()).apply {
                            setTitle("경고")
                            setIcon(R.drawable.ic_round_warning_24)
                            setMessage("정말 로그아웃 하시겠습니까?")
                            setPositiveButton("네") { _, _ ->
                                if (Constants.APPLICATION_MODE == Constants.DEV_MODE_WITHOUT_SERVER) {
                                    val sharedPreferences = requireContext().getSharedPreferences(
                                        "login",
                                        Context.MODE_PRIVATE
                                    )
                                    val editor = sharedPreferences.edit()
                                    editor.clear()
                                    editor.apply()
                                    requireActivity().finish()

                                } else {
                                    tryLogout()
                                }
                            }
                            setNegativeButton("아니요") { _, _ ->
                                return@setNegativeButton
                            }
                        }

                        val alertDialog = builder.create()
                        alertDialog.show()
                    }
                    else -> {
                    }
                }
            }

        binding.profileLayout.setOnClickListener {
            startActivity(Intent(requireActivity(), UserInfoActivity::class.java))
        }

        binding.tvUserEmail.text = Constants.USER_EMAIL

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        if (Constants.APPLICATION_MODE != Constants.DEV_MODE_WITHOUT_SERVER) {
            val callBack = BaseCallBack(
                { _: Int, response: String ->
                    val jsonResponse = JSONObject(response)
                    val result = jsonResponse.getBoolean("success")
                    if (result) {
                        val format = Json { ignoreUnknownKeys = true }
                        val userInfo = format.decodeFromString<User>(response)

                        binding.tvStatusMsg.text = userInfo.statusMsg

                        if (userInfo.profileUrl.isNotBlank()) {
                            CoroutineScope(Dispatchers.Main).launch {
                                val bitmap = withContext(Dispatchers.IO) {
                                    LoadImage.get(userInfo.profileUrl)
                                }
                                binding.ivProfile.setImageBitmap(bitmap)
                            }

                        } else {
                            binding.ivProfile.setImageResource(R.drawable.ic_round_account_circle_24)
                        }
                    }
                }
            )

            CoroutineScope(Dispatchers.IO).launch {
                UserService.read(callBack)
            }
        }
    }

    private fun tryLogout() {
        val sharedPreferences = requireContext().getSharedPreferences("login", Context.MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("userEmail", "") ?: ""
        Log.d(Constants.TAG, "try logout : $userEmail")

        if (userEmail.isNotEmpty()) {
            val callBack = BaseCallBack(
                { _: Int, response: String ->
                    val jsonResponse = JSONObject(response)
                    val result = jsonResponse.getBoolean("success")
                    if (result) {
                        sqlDB = dbHelper.writableDatabase
                        sqlDB.execSQL("delete from walkRecords;")
                        sqlDB.execSQL("delete from joinedChatRooms;")
                        sqlDB.execSQL("delete from chatUsers;")
                        sqlDB.execSQL("delete from chatLogs;")

                        val editor = sharedPreferences.edit()
                        editor.clear()
                        editor.apply()
                        requireActivity().finish()

                    } else {
                        Toast.makeText(requireContext(), "로그아웃에 실패했습니다!", Toast.LENGTH_SHORT).show()
                    }
                },
                { code: Int, response: String ->
                    when (code) {
                        401 -> {
                            Toast.makeText(requireContext(), "이미 로그아웃된 계정입니다!", Toast.LENGTH_SHORT)
                                .show()
                        }
                        404 -> {
                            Toast.makeText(requireContext(), "존재하지않는 계정입니다!", Toast.LENGTH_SHORT)
                                .show()
                        }
                        else -> {
                            Toast.makeText(requireContext(), response, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            )

            CoroutineScope(Dispatchers.IO).launch {
                AuthService.logout(callBack)
            }
        }
    }
}