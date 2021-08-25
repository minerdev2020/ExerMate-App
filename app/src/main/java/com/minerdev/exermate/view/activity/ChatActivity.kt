package com.minerdev.exermate.view.activity

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.minerdev.exermate.adapter.ChatAdapter
import com.minerdev.exermate.databinding.ActivityChatBinding
import com.minerdev.exermate.model.ChatLog
import com.minerdev.exermate.model.ChatRoom
import com.minerdev.exermate.model.User
import com.minerdev.exermate.utils.Constants
import com.minerdev.exermate.utils.DBHelper
import java.io.File

class ChatActivity : AppCompatActivity() {
    private val adapter = ChatAdapter()
    private val binding by lazy { ActivityChatBinding.inflate(layoutInflater) }
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
//                    adapter.updateChatLogs(
//                        ChatLog(
//                            createdAt = "오후 11:09",
//                            text = path,
//                            type = 4
//                        )
//                    )
                    binding.recyclerView.scrollToPosition(adapter.itemCount - 1)
                }
            }
        }

    private lateinit var dbHelper: DBHelper
    private lateinit var sqlDB: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (intent == null) {
            finish()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "단체 채팅방"

        dbHelper = DBHelper(this)
        sqlDB = dbHelper.writableDatabase

        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false).apply {
            stackFromEnd = true
        }
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter

        adapter.clickListener = { urlStr ->
            val intent = Intent(this, FullScreenPhotoActivity::class.java).apply {
                putExtra("urlStr", urlStr)
            }
            startActivity(intent)
        }

        val roomId = intent.getIntExtra("roomId", 0)

        val chatUsersFromDB = ArrayList<User>()
        var cursor = sqlDB.rawQuery("select * from chatUsers where roomId = $roomId;", null)
        while (cursor.moveToNext()) {
            chatUsersFromDB.add(
                User(
                    id = cursor.getInt(cursor.getColumnIndex("userId")),
                    nickname = cursor.getString(cursor.getColumnIndex("nickname")),
                    profileUrl = cursor.getString(cursor.getColumnIndex("profileUrl"))
                )
            )
        }
        cursor.close()
        adapter.initChatRoom(
            ChatRoom(
                users = listOf(
                    User(
                        id = 1,
                        nickname = "홍길동",
                        profileUrl = "https://i.imgur.com/fCvxGeS.jpeg"
                    ),
                    User(
                        id = 2,
                        nickname = "아이언맨",
                        profileUrl = "https://i.imgur.com/q1jbHAu.jpeg"
                    ),
                    User(
                        id = 3,
                        nickname = "임꺽정",
                        profileUrl = "https://i.imgur.com/Lc5VA8b.png"
                    )
                )
            )
        )

        val chatLogsFromDB = ArrayList<ChatLog>()
        cursor = sqlDB.rawQuery("select * from chatLogs where roomId = $roomId;", null)
        while (cursor.moveToNext()) {
            chatLogsFromDB.add(
                ChatLog(
                    roomId = roomId,
                    fromId = cursor.getInt(cursor.getColumnIndex("fromId")),
                    createdAt = cursor.getLong(cursor.getColumnIndex("createdAt")),
                    text = cursor.getString(cursor.getColumnIndex("text")),
                    type = cursor.getInt(cursor.getColumnIndex("type")).toByte()
                )
            )
        }
        cursor.close()
        adapter.initChatLogs(chatLogsFromDB)

        binding.btnSend.setOnClickListener {
            val text = binding.etChat.text.toString()
            if (text.isBlank()) {
                return@setOnClickListener
            }

            val now = System.currentTimeMillis() / 1000
            adapter.updateChatLogs(
                ChatLog(
                    roomId = 1,
                    fromId = 2,
                    createdAt = now,
                    text = text,
                    type = ChatAdapter.MY_CHAT_ITEM.toByte()
                )
            )
            binding.recyclerView.scrollToPosition(adapter.itemCount - 1)
            binding.etChat.text.clear()

            val contentValues = ContentValues().apply {
                put("roomId", roomId)
                put("fromId", 2)
                put("text", text)
                put("type", ChatAdapter.MY_CHAT_ITEM)
                put("createdAt", now)
            }
            sqlDB.insert("chatLogs", null, contentValues)
        }

        binding.btnAddPhoto.setOnClickListener {
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
                    val builder = AlertDialog.Builder(this).apply {
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