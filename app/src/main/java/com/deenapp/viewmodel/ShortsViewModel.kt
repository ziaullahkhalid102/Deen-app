package com.deenapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deenapp.data.model.ShortVideo
import com.deenapp.data.repository.DeenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShortsViewModel @Inject constructor(
    private val repository: DeenRepository
) : ViewModel() {

    private val _videos = MutableStateFlow<List<ShortVideo>>(emptyList())
    val videos: StateFlow<List<ShortVideo>> = _videos.asStateFlow()

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    init {
        loadVideos()
    }

    private fun loadVideos() {
        viewModelScope.launch {
            repository.getShortVideos().collect { _videos.value = it }
        }
    }

    fun selectTab(index: Int) {
        _selectedTab.value = index
    }

    fun toggleLike(videoId: String) {
        _videos.value = _videos.value.map { video ->
            if (video.id == videoId) {
                video.copy(
                    isLiked = !video.isLiked,
                    likesCount = if (video.isLiked) video.likesCount - 1 else video.likesCount + 1
                )
            } else video
        }
    }

    fun toggleFollow(videoId: String) {
        _videos.value = _videos.value.map { video ->
            if (video.id == videoId) {
                video.copy(isFollowing = !video.isFollowing)
            } else video
        }
    }
}
