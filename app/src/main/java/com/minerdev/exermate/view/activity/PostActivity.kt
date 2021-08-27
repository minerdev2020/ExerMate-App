package com.minerdev.exermate.view.activity

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.minerdev.exermate.R
import com.minerdev.exermate.databinding.ActivityPostBinding
import com.minerdev.exermate.model.Post
import com.minerdev.exermate.model.User
import com.minerdev.exermate.network.BaseCallBack
import com.minerdev.exermate.network.service.ChatRoomService
import com.minerdev.exermate.network.service.PostService
import com.minerdev.exermate.network.service.UserService
import com.minerdev.exermate.utils.Constants
import com.minerdev.exermate.utils.DBHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject

class PostActivity : AppCompatActivity() {
    private val binding by lazy { ActivityPostBinding.inflate(layoutInflater) }
    private val postInfo = MutableLiveData<Post>()

    private lateinit var dbHelper: DBHelper
    private lateinit var sqlDB: SQLiteDatabase
    private lateinit var postId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dbHelper = DBHelper(this)
        sqlDB = dbHelper.writableDatabase

        postId = intent.getStringExtra("postId") ?: ""

        binding.btnJoin.setOnClickListener {
            if (postId.isBlank()) {
                return@setOnClickListener
            }

            val cursor =
                sqlDB.rawQuery("select * from joinedChatRooms where roomId = \"$postId\"", null)
            if (cursor.moveToNext()) {
                val cachedRoomId = cursor.getString(cursor.getColumnIndex("roomId"))
                val cachedName = cursor.getString(cursor.getColumnIndex("name"))
                cursor.close()

                val intent = Intent(this, ChatActivity::class.java).apply {
                    putExtra("roomId", cachedRoomId)
                    putExtra("name", cachedName)
                }
                startActivity(intent)
                return@setOnClickListener
            }
            cursor.close()

            if (Constants.APPLICATION_MODE != Constants.DEV_MODE_WITHOUT_SERVER) {
                val readCallBack = BaseCallBack(
                    { code, response ->
                        val jsonObject = JSONObject(response)
                        val result = jsonObject.getString("result")
                        val format = Json { ignoreUnknownKeys = true }

                        val membersList = format.decodeFromString<List<User>>(result)

                        for (member in membersList) {
                            val contentValues = ContentValues().apply {
                                put("roomId", postId)
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

                        val intent = Intent(this, ChatActivity::class.java).apply {
                            putExtra("roomId", postId)
                            putExtra("name", intent.getStringExtra("name") ?: "")
                        }

                        startActivity(intent)
                    }
                )

                val joinCallBack = BaseCallBack(
                    { code, response ->
                        val jsonResponse = JSONObject(response)
                        val result = jsonResponse.getBoolean("success")
                        if (result) {
                            ChatRoomService.readAllMembers(postId, readCallBack)

                        } else {
                            Toast.makeText(this, "채팅방에 참가 할 수 없습니다!", Toast.LENGTH_SHORT).show()
                        }
                    }
                )

                CoroutineScope(Dispatchers.IO).launch {
                    UserService.joinChatRoom(postId, joinCallBack)
                }
            }
        }

        postInfo.observe(this) {
            supportActionBar?.title = it.title
            binding.tvMemberCount.text = it.maxMemberNum.toString()
            binding.tvPlace.text = it.place
            binding.tvExerciseTime.text = it.exerciseTime
            binding.tvText.text = it.text
        }

        val callBack = BaseCallBack(
            { code, response ->
                val jsonResponse = JSONObject(response)
                val result = jsonResponse.getBoolean("success")
                if (result) {
                    val format = Json { ignoreUnknownKeys = true }
                    postInfo.postValue(format.decodeFromString<Post>(response))
                }
            }
        )

        if (postId.isNotBlank() && Constants.APPLICATION_MODE != Constants.DEV_MODE_WITHOUT_SERVER) {
            CoroutineScope(Dispatchers.IO).launch {
                PostService.read(postId, callBack)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_my_post, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.toolbar_modify_post -> {
                if (postId.isNotBlank()) {
                    val intent = Intent(this, EditPostActivity::class.java).apply {
                        putExtra("mode", EditPostActivity.MODIFY_MODE)
                        putExtra("postId", postId)
                    }
                    startActivity(intent)
                }
            }
            else -> finish()
        }

        return super.onOptionsItemSelected(item)
    }
}