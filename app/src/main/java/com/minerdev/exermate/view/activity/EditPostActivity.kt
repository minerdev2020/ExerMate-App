package com.minerdev.exermate.view.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.minerdev.exermate.R
import com.minerdev.exermate.databinding.ActivityEditPostBinding
import com.minerdev.exermate.model.Post
import com.minerdev.exermate.network.BaseCallBack
import com.minerdev.exermate.network.PostService
import com.minerdev.exermate.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject

class EditPostActivity : AppCompatActivity() {
    companion object {
        const val CREATE_MODE = 0
        const val MODIFY_MODE = 1
    }

    private val binding by lazy { ActivityEditPostBinding.inflate(layoutInflater) }
    private val postInfo = MutableLiveData<Post>()

    private var mode = CREATE_MODE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = "모집글 작성"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        postInfo.observe(this) {
            binding.etTitle.setText(it.title)
            binding.etMaxMemberCount.setText(it.maxMemberNum.toString())
            binding.etPlace.setText(it.place)
            binding.etExerciseTime.setText(it.exerciseTime)
            binding.etText.setText(it.text)
        }

        mode = intent.getIntExtra("mode", CREATE_MODE)

        if (mode == MODIFY_MODE) {
            val postId = intent.getIntExtra("postId", 0)
            val callBack = BaseCallBack(
                { code, response ->
                    val data = JSONObject(response)
                    val format = Json { encodeDefaults = true }
                    postInfo.postValue(format.decodeFromString<Post>(data.getString("data")))
                },
                { code, response -> super.finish() },
                { error -> super.finish() }
            )

            if (postId != 0 && Constants.APPLICATION_MODE != Constants.DEV_MODE_WITHOUT_SERVER) {
                CoroutineScope(Dispatchers.IO).launch {
                    PostService.read(postId, callBack)
                }
            }
        }
    }

    override fun finish() {
        val builder = AlertDialog.Builder(this).apply {
            setTitle("경고")
            setIcon(R.drawable.ic_round_warning_24)
            setMessage("작성하신 내용이 저장되지않습니다.\n정말 뒤로가시겠습니까?")
            setPositiveButton("네") { _, _ ->
                val callBack = BaseCallBack(
                    { code, response -> super.finish() },
                    { code, response -> super.finish() },
                    { error -> super.finish() }
                )

                CoroutineScope(Dispatchers.IO).launch {
                    val post = Post(
                        title = binding.etTitle.text.toString(),
                        text = binding.etText.text.toString()
                    )

                    when (mode) {
                        CREATE_MODE -> PostService.create(post, callBack)
                        MODIFY_MODE -> PostService.update(post, callBack)
                        else -> super.finish()
                    }
                }
            }
            setNegativeButton("아니요") { _, _ ->
                return@setNegativeButton
            }
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_edit_post, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.toolbar_send_post -> {
                super.finish()
            }
            else -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}