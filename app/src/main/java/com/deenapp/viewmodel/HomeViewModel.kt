package com.deenapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deenapp.data.model.Post
import com.deenapp.data.model.PostVisibility
import com.deenapp.data.model.Story
import com.deenapp.data.model.User
import com.deenapp.data.repository.DeenRepository
import com.deenapp.data.service.AiPostAgent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: DeenRepository,
    private val aiPostAgent: AiPostAgent
) : ViewModel() {

    private val _stories = MutableStateFlow<List<Story>>(emptyList())
    val stories: StateFlow<List<Story>> = _stories.asStateFlow()

    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    private val _currentUser = MutableStateFlow(User())
    val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private var storiesJob: Job? = null
    private var postsJob: Job? = null
    private var userJob: Job? = null

    init {
        loadData()
        generateAiContent()
    }

    private fun generateAiContent() {
        viewModelScope.launch {
            aiPostAgent.generatePosts(5)
        }
    }

    private fun loadData() {
        storiesJob?.cancel()
        postsJob?.cancel()
        userJob?.cancel()
        storiesJob = viewModelScope.launch {
            repository.getStories().collect { _stories.value = it }
        }
        postsJob = viewModelScope.launch {
            repository.getPosts().collect { _posts.value = it }
        }
        userJob = viewModelScope.launch {
            repository.getCurrentUser().collect { _currentUser.value = it }
        }
    }

    fun toggleLike(postId: String) {
        viewModelScope.launch {
            repository.toggleLike(postId)
        }
    }

    fun toggleBookmark(postId: String) {
        repository.toggleBookmark(postId)
    }

    fun addPost(content: String, mediaUris: List<String> = emptyList(), visibility: PostVisibility = PostVisibility.PUBLIC) {
        repository.addPost(content, mediaUris, visibility)
    }

    fun deletePost(postId: String) {
        repository.deletePost(postId)
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            loadData()
            kotlinx.coroutines.delay(300)
            _isRefreshing.value = false
        }
    }
}
