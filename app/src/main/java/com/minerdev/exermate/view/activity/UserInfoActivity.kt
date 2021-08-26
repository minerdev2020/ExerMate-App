package com.minerdev.exermate.view.activity

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.minerdev.exermate.R
import com.minerdev.exermate.databinding.ActivityUserInfoBinding
import com.minerdev.exermate.model.User
import com.minerdev.exermate.network.BaseCallBack
import com.minerdev.exermate.network.LoadImage
import com.minerdev.exermate.network.service.UserService
import com.minerdev.exermate.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import java.io.File

class UserInfoActivity : AppCompatActivity() {
    private val binding by lazy { ActivityUserInfoBinding.inflate(layoutInflater) }
    private val userInfo = MutableLiveData<User>()
    private val requestActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val selectedImageUri = result.data?.data ?: return@registerForActivityResult
                val path: String = getPathFromUri(this, selectedImageUri)
                val file = File(path)

                if (!file.exists() || !file.canRead()) {
                    Toast.makeText(this, "사진이 존재 하지않거나 읽을 수 없습니다!", Toast.LENGTH_SHORT).show()

                } else if (file.length() > Constants.FILE_MAX_SIZE) {
                    Toast.makeText(this, "사진의 크기는 10MB 보다 작아야 합니다!", Toast.LENGTH_SHORT).show()

                } else {
                    binding.ivProfile.setImageURI(selectedImageUri)
                    profilePath = path
                }
            }
        }

    private var profilePath = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = "유저정보 수정"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        userInfo.observe(this) {
            binding.etStatusMsg.setText(it.email)
            if (it.profileUrl.isNotBlank()) {
                CoroutineScope(Dispatchers.Main).launch {
                    val bitmap = withContext(Dispatchers.IO) {
                        LoadImage.get(it.profileUrl)
                    }
                    binding.ivProfile.setImageBitmap(bitmap)
                }

            } else {
                binding.ivProfile.setImageResource(R.drawable.ic_round_account_circle_24)
            }
        }

        binding.ivProfile.setOnClickListener {
            val permissionChecked = ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            )
            if (permissionChecked != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 102)

            } else {
                val intent = Intent().apply {
                    setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                    action = Intent.ACTION_PICK
                }
                requestActivity.launch(intent)
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnDone.setOnClickListener {
            val builder = AlertDialog.Builder(this).apply {
                setTitle("확인")
                setIcon(R.drawable.ic_round_help_24)
                setMessage("작성하신 내용으로 수정하시겠습니까?")
                setPositiveButton("네") { _, _ ->
                    val callBack = BaseCallBack({ code, response -> super.finish() })

                    CoroutineScope(Dispatchers.IO).launch {
                        UserService.updateStatusMsg(
                            binding.etStatusMsg.text.toString(),
                            callBack
                        )

                        UserService.updateProfile(profilePath, callBack)
                    }
                }
                setNegativeButton("아니요") { _, _ ->
                    return@setNegativeButton
                }
            }

            val alertDialog = builder.create()
            alertDialog.show()
        }

        val callBack = BaseCallBack(
            { code, response ->
                val jsonResponse = JSONObject(response)
                val result = jsonResponse.getBoolean("success")
                if (result) {
                    val format = Json { ignoreUnknownKeys = true }
                    userInfo.postValue(format.decodeFromString<User>(response))
                }
            }
        )

        if (Constants.APPLICATION_MODE != Constants.DEV_MODE_WITHOUT_SERVER) {
            CoroutineScope(Dispatchers.IO).launch {
                UserService.read(callBack)
            }
        }
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            102 ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val intent = Intent().apply {
                        setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                        action = Intent.ACTION_PICK
                    }
                    requestActivity.launch(intent)

                } else {
                    val builder = android.app.AlertDialog.Builder(this).apply {
                        setTitle("앱 권한")
                        setMessage("해당 앱의 원활한 기능을 이용하시려면 애플리케이션 정보>권한에서 '저장공간' 권한을 허용해 주십시오.")
                        setPositiveButton("권한설정") { dialog: DialogInterface, _ ->
                            val intent =
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(
                                    Uri.parse("package:$packageName")
                                )
                            startActivity(intent)
                            dialog.dismiss()
                        }
                        setNegativeButton("취소") { _, _ ->
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

    private fun getPathFromUri(context: Context, uri: Uri): String {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.let {
            it.moveToNext()
            val path = it.getString(it.getColumnIndex("_data"))
            it.close()
            return path
        }

        return ""
    }
}