package com.minerdev.exermate.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.minerdev.exermate.R
import com.minerdev.exermate.databinding.ActivityPostBinding
import com.minerdev.exermate.model.Post
import com.minerdev.exermate.network.BaseCallBack
import com.minerdev.exermate.network.service.PostService
import com.minerdev.exermate.network.service.UserService
import com.minerdev.exermate.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject

class PostActivity : AppCompatActivity() {
    private val binding by lazy { ActivityPostBinding.inflate(layoutInflater) }
    private val postInfo = MutableLiveData<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnJoin.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java).apply {
                putExtra("roomId", intent.getIntExtra("roomId", 0))
                putExtra("title", intent.getStringExtra("title") ?: "")
            }
            startActivity(intent)
        }

        postInfo.observe(this) {
            supportActionBar?.title = it.title
            binding.tvMemberCount.text = it.maxMemberNum.toString()
            binding.tvPlace.text = it.place
            binding.tvExerciseTime.text = it.exerciseTime
            binding.tvText.text = it.text
        }

        val postId = intent.getStringExtra("postId") ?: ""
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
                val intent = Intent(this, EditPostActivity::class.java).apply {
                    putExtra("mode", EditPostActivity.MODIFY_MODE)
                    putExtra("postId", postInfo.value?.id ?: "")
                }
                startActivity(intent)
            }
            else -> finish()
        }

        return super.onOptionsItemSelected(item)
    }
}