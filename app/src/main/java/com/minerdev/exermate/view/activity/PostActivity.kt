package com.minerdev.exermate.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.minerdev.exermate.databinding.ActivityPostBinding
import com.minerdev.exermate.model.Post
import com.minerdev.exermate.network.BaseCallBack
import com.minerdev.exermate.network.service.PostService
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

        val postId = intent.getIntExtra("postId", 0)
        val callBack = BaseCallBack(
            { code, response ->
                val jsonResponse = JSONObject(response)
                val result = jsonResponse.getBoolean("success")
                if (result) {
                    val format = Json {
                        encodeDefaults = true
                        ignoreUnknownKeys = true
                    }
                    postInfo.postValue(format.decodeFromString<Post>(response))
                }
            }
        )

        if (postId != 0 && Constants.APPLICATION_MODE != Constants.DEV_MODE_WITHOUT_SERVER) {
            CoroutineScope(Dispatchers.IO).launch {
                PostService.read(postId, callBack)
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
}