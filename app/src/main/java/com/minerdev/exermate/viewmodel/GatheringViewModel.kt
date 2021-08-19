package com.minerdev.exermate.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.minerdev.exermate.model.Post

class GatheringViewModel : ViewModel() {
    val postList = MutableLiveData<List<Post>>()

    fun loadPosts() {
        postList.value
    }
}