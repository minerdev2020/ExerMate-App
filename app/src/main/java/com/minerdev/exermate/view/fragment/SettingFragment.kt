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
import com.minerdev.exermate.network.service.AuthService
import com.minerdev.exermate.network.BaseCallBack
import com.minerdev.exermate.network.LoadImage
import com.minerdev.exermate.utils.Constants
import com.minerdev.exermate.view.activity.AccountInfoActivity
import com.minerdev.exermate.view.activity.UserInfoActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class SettingFragment : Fragment() {
    private val binding by lazy { FragmentSettingBinding.inflate(layoutInflater) }
    private val listMenu = arrayListOf("유저 정보 수정", "로그아웃")

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

        if (Constants.USER_PROFILE_URL.isNotBlank()) {
            CoroutineScope(Dispatchers.Main).launch {
                binding.tvStateMsg.text = "이제 취업하자..."
                val bitmap = withContext(Dispatchers.IO) {
                    LoadImage.get(Constants.USER_PROFILE_URL)
                }
                binding.ivProfile.setImageBitmap(bitmap)
            }

        } else {
            binding.ivProfile.setImageResource(R.drawable.ic_round_account_circle_24)
        }

        return binding.root
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
                            Toast.makeText(requireContext(), response, Toast.LENGTH_LONG).show()
                            val editor = sharedPreferences.edit()
                            editor.clear()
                            editor.apply()
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