package com.minerdev.exermate.view.fragment

import android.content.Context
import android.content.Intent
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
import com.minerdev.exermate.network.AuthService
import com.minerdev.exermate.utils.Constants
import com.minerdev.exermate.view.activity.UserInfoActivity
import org.json.JSONObject

class SettingFragment : Fragment() {
    private val binding by lazy { FragmentSettingBinding.inflate(layoutInflater) }
    private val listMenu = arrayListOf("유저 정보 수정", "로그아웃")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.listView.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, listMenu)
        binding.listView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                when (position) {
                    0 -> {
                        startActivity(Intent(requireActivity(), UserInfoActivity::class.java))
                    }
                    1 -> {
                        val builder = AlertDialog.Builder(requireContext()).apply {
                            setTitle("경고")
                            setIcon(R.drawable.ic_round_warning_24)
                            setMessage("정말 로그아웃 하시겠습니까?")
                            setPositiveButton("네") { _, _ ->
                                tryLogout()
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

        setHasOptionsMenu(false)

        return binding.root
    }

    private fun tryLogout() {
        val sharedPreferences = requireContext().getSharedPreferences("login", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("user_id", "") ?: ""
        Log.d(Constants.TAG, "logout : $userId")

        if (userId.isNotEmpty()) {
            AuthService.logout(userId,
                { _: Int, response: String ->
                    val data = JSONObject(response)
                    Log.d(Constants.TAG, "logout response : " + data.getString("message"))
                    val editor = sharedPreferences.edit()
                    editor.clear()
                    editor.apply()
                },
                { code: Int, response: String ->
                    val data = JSONObject(response)
                    Log.d(Constants.TAG, "logout response : " + data.getString("message"))
                    when (code) {
                        401 -> {
                            Toast.makeText(requireContext(), "이미 로그아웃된 계정입니다!", Toast.LENGTH_SHORT)
                                .show()
                            val editor = sharedPreferences.edit()
                            editor.clear()
                            editor.apply()
                        }
                        404 -> {
                            Toast.makeText(requireContext(), "존재하지않는 계정입니다!", Toast.LENGTH_SHORT)
                                .show()
                            val editor = sharedPreferences.edit()
                            editor.clear()
                            editor.apply()
                        }
                        else -> {
                        }
                    }

                },
                { error: Throwable ->
                    Log.d(Constants.TAG, "logout error : " + error.localizedMessage)
                }
            )
        }

        requireActivity().finish()
    }
}