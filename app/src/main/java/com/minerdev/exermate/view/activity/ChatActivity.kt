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
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.minerdev.exermate.adapter.ChatAdapter
import com.minerdev.exermate.databinding.ActivityChatBinding
import com.minerdev.exermate.model.ChatLog
import com.minerdev.exermate.model.User
import com.minerdev.exermate.network.BaseCallBack
import com.minerdev.exermate.network.ChatWebSocketListener
import com.minerdev.exermate.network.service.ChatRoomService
import com.minerdev.exermate.utils.Constants
import com.minerdev.exermate.utils.DBHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import org.json.JSONObject
import java.io.File

class ChatActivity : AppCompatActivity() {
    private val adapter = ChatAdapter()
    private val binding by lazy { ActivityChatBinding.inflate(layoutInflater) }
    private val client = OkHttpClient()
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
                    val callBack = BaseCallBack(
                        { code, response ->
                            val jsonResponse = JSONObject(response)
                            val result_ = jsonResponse.getBoolean("success")
                            if (result_) {
                                val url = jsonResponse.getString("url")
                                val now = System.currentTimeMillis()
                                adapter.addChatLogs(
                                    ChatLog(
                                        roomId = roomId,
                                        fromId = Constants.USER_EMAIL,
                                        createdAt = now,
                                        url = url,
                                        type = ChatAdapter.MY_CHAT_PHOTO_ITEM.toByte()
                                    )
                                )
                                binding.recyclerView.scrollToPosition(adapter.itemCount - 1)

                                val contentValues = ContentValues().apply {
                                    put("roomId", roomId)
                                    put("fromId", Constants.USER_EMAIL)
                                    put("createdAt", now)
                                    put("url", url)
                                    put("type", ChatAdapter.MY_CHAT_PHOTO_ITEM)
                                }
                                sqlDB.insert("chatLogs", null, contentValues)

                            } else {
                                Toast.makeText(this, "사진 전송에 실패했습니다!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )

                    CoroutineScope(Dispatchers.IO).launch {
                        ChatRoomService.sendImage(roomId, path, callBack)
                    }
                }
            }
        }

    private lateinit var roomId: String
    private lateinit var dbHelper: DBHelper
    private lateinit var sqlDB: SQLiteDatabase
    private lateinit var webSocket: WebSocket

    private var preUserId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra("name") ?: ""

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

        roomId = intent.getStringExtra("roomId") ?: ""
        if (roomId.isNotBlank()) {
            val chatMembersFromDB = ArrayList<User>()
            var cursor =
                sqlDB.rawQuery("select * from chatMembers where roomId = \"$roomId\";", null)

            if (cursor.count == 0) {
                cursor.close()

                val callBack = BaseCallBack(
                    { code, response ->
                        val jsonObject = JSONObject(response)
                        val result = jsonObject.getString("result")
                        val format = Json { ignoreUnknownKeys = true }

                        val membersList = format.decodeFromString<List<User>>(result)

                        for (member in membersList) {
                            val contentValues = ContentValues().apply {
                                put("roomId", roomId)
                                put("userId", member.id)
                                put("email", member.email)
                                put("nickname", member.nickname)
                                put("profileUrl", member.profileUrl)
                            }

                            sqlDB.insert(
                                "chatMembers",
                                null,
                                contentValues
                            )
                        }

                        chatMembersFromDB.addAll(membersList)
                        adapter.initChatRoom(chatMembersFromDB)
                    }
                )

                CoroutineScope(Dispatchers.IO).launch {
                    ChatRoomService.readAllMembers(roomId, callBack)
                }

            } else {
                while (cursor.moveToNext()) {
                    chatMembersFromDB.add(
                        User(
                            id = cursor.getString(cursor.getColumnIndex("userId")),
                            nickname = cursor.getString(cursor.getColumnIndex("nickname")),
                            profileUrl = cursor.getString(cursor.getColumnIndex("profileUrl"))
                        )
                    )
                }

                cursor.close()
                adapter.initChatRoom(chatMembersFromDB)

                val chatLogsFromDB = ArrayList<ChatLog>()
                cursor = sqlDB.rawQuery("select * from chatLogs where roomId = \"$roomId\";", null)
                while (cursor.moveToNext()) {
                    chatLogsFromDB.add(
                        ChatLog(
                            roomId = roomId,
                            fromId = cursor.getString(cursor.getColumnIndex("fromId")),
                            createdAt = cursor.getLong(cursor.getColumnIndex("createdAt")),
                            text = cursor.getString(cursor.getColumnIndex("text")),
                            url = cursor.getString(cursor.getColumnIndex("url")),
                            type = cursor.getInt(cursor.getColumnIndex("type")).toByte()
                        )
                    )
                }
                cursor.close()
                adapter.initChatLogs(chatLogsFromDB)
            }
        }

        binding.btnSend.setOnClickListener {
            val text = binding.etChat.text.toString()
            if (text.isBlank()) {
                return@setOnClickListener
            }

            val now = System.currentTimeMillis()
            adapter.addChatLogs(
                ChatLog(
                    roomId = roomId,
                    fromId = Constants.USER_EMAIL,
                    createdAt = now,
                    text = text,
                    type = ChatAdapter.MY_CHAT_ITEM.toByte()
                )
            )
            binding.recyclerView.scrollToPosition(adapter.itemCount - 1)
            binding.etChat.text.clear()

            val contentValues = ContentValues().apply {
                put("roomId", roomId)
                put("fromId", Constants.USER_EMAIL)
                put("createdAt", now)
                put("text", text)
                put("type", ChatAdapter.MY_CHAT_ITEM)
            }
            sqlDB.insert("chatLogs", null, contentValues)

            val msg = buildJsonObject {
                put("bizType", "SEND_MSG")
                put("chatRoomID", roomId)
                put("time", now)
                put("text", text)
            }
            Log.d("Socket", "Sending : $msg")
            webSocket.send(msg.toString())
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

        val request = Request.Builder()
            .url("ws://${Constants.SERVER_IP_ADDRESS}:${Constants.SERVER_WEB_SOCKET_PORT}/ws")
            .build()
        val listener = object : ChatWebSocketListener(Constants.USER_EMAIL, "test") {
            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)

                val jsonResponse = JSONObject(text)
                val isMsg = jsonResponse.has("bizType")
                if (isMsg) {
                    val format = Json { ignoreUnknownKeys = true }
                    val chatLog = when (jsonResponse.getString("bizType")) {
                        "SEND_MSG", "SEND_IMG" -> format.decodeFromString(text)
                        else -> ChatLog(roomId = roomId)
                    }

                    when (jsonResponse.getString("bizType")) {
                        "SEND_MSG" -> {
                            chatLog.type = if (preUserId != chatLog.fromId) {
                                ChatAdapter.CHAT_START_ITEM.toByte()

                            } else {
                                ChatAdapter.CHAT_ITEM.toByte()
                            }

                            preUserId = chatLog.fromId
                        }
                        "SEND_IMG" -> {
                            chatLog.type = if (preUserId != chatLog.fromId) {
                                ChatAdapter.CHAT_START_PHOTO_ITEM.toByte()

                            } else {
                                ChatAdapter.CHAT_PHOTO_ITEM.toByte()
                            }

                            preUserId = chatLog.fromId
                        }
                        "NOTIFY_USER" -> {
                            val member = format.decodeFromString<User>(text)
                            val contentValues = ContentValues().apply {
                                put("roomId", roomId)
                                put("userId", member.id)
                                put("email", member.email)
                                put("nickname", member.nickname)
                                put("profileUrl", member.profileUrl)
                            }

                            sqlDB.insert(
                                "chatMembers",
                                null,
                                contentValues
                            )

                            chatLog.type = ChatAdapter.SYSTEM_CHAT_ITEM.toByte()
                            chatLog.fromId = member.email

                            CoroutineScope(Dispatchers.Main).launch {
                                adapter.addChatMembers(member)
                            }

                            preUserId = ""
                        }
                        else -> return
                    }

                    val contentValues = ContentValues().apply {
                        put("roomId", chatLog.roomId)
                        put("fromId", chatLog.fromId)
                        put("createdAt", chatLog.createdAt)
                        put("text", chatLog.text)
                        put("url", chatLog.url)
                        put("type", chatLog.type)
                    }

                    sqlDB.insert("chatLogs", null, contentValues)

                    CoroutineScope(Dispatchers.Main).launch {
                        adapter.addChatLogs(chatLog)
                        binding.recyclerView.scrollToPosition(adapter.itemCount - 1)
                        binding.etChat.text.clear()
                    }

                } else {
                    val result = jsonResponse.getBoolean("success")
                    if (!result) {
                        CoroutineScope(Dispatchers.Main).launch {
                            Toast.makeText(this@ChatActivity, "메시지 발송에 실패했습니다!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
        webSocket = client.newWebSocket(request, listener)
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocket.close(ChatWebSocketListener.NORMAL_CLOSURE_STATUS, null)
        webSocket.cancel()
        client.dispatcher.executorService.shutdown()
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