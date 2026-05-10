package com.deenapp.data.repository

import com.deenapp.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeenRepository @Inject constructor() {

    private val _currentUser = MutableStateFlow(SampleData.currentUser)

    private val _posts = MutableStateFlow<List<Post>>(SampleData.posts)
    val postsFlow: StateFlow<List<Post>> = _posts.asStateFlow()

    private val _savedPosts = MutableStateFlow<List<String>>(emptyList())

    private val _followedUsers = MutableStateFlow<Set<String>>(emptySet())
    val followedUsers: StateFlow<Set<String>> = _followedUsers.asStateFlow()

    fun updateCurrentUser(userId: String, email: String, name: String, photoUrl: String) {
        _currentUser.value = User(
            id = userId,
            email = email,
            displayName = name,
            username = "@${name.replace(" ", ".").lowercase()}",
            profileImageUrl = photoUrl
        )
    }

    fun getCurrentUser(): Flow<User> = _currentUser

    fun getStories(): Flow<List<Story>> = flow {
        val user = _currentUser.value
        val ownStory = Story(
            id = "s1",
            userId = user.id.ifEmpty { "current_user" },
            userName = "Your Story",
            userProfileImage = user.profileImageUrl,
            isOwnStory = true
        )
        emit(listOf(ownStory))
    }

    fun getPosts(): Flow<List<Post>> = _posts

    fun getUserPosts(): Flow<List<Post>> = _posts.map { posts ->
        posts.filter { it.userId == _currentUser.value.id || it.userId == "current_user" }
    }

    fun getShortVideos(): Flow<List<ShortVideo>> = flow {
        emit(SampleData.shortVideos)
    }

    fun getChatConversations(): Flow<List<ChatConversation>> = flow {
        emit(SampleData.chatConversations)
    }

    fun getNotifications(): Flow<List<Notification>> = flow {
        emit(SampleData.notifications)
    }

    fun getSearchUsers(): Flow<List<User>> = flow {
        emit(SampleData.searchUsers)
    }

    fun getSearchPosts(): Flow<List<Post>> = _posts.map { it.take(3) }

    suspend fun toggleLike(postId: String): Boolean {
        _posts.value = _posts.value.map { post ->
            if (post.id == postId) {
                post.copy(
                    isLiked = !post.isLiked,
                    likesCount = if (post.isLiked) post.likesCount - 1 else post.likesCount + 1
                )
            } else post
        }
        return true
    }

    fun addPost(content: String, mediaUris: List<String> = emptyList(), visibility: PostVisibility = PostVisibility.PUBLIC) {
        val user = _currentUser.value
        val newPost = Post(
            id = "post_${System.currentTimeMillis()}",
            userId = user.id.ifEmpty { "current_user" },
            userName = user.displayName.ifEmpty { "You" },
            userProfileImage = user.profileImageUrl,
            content = content,
            imageUrl = mediaUris.firstOrNull() ?: "",
            likesCount = 0,
            commentsCount = 0,
            sharesCount = 0,
            isLiked = false,
            isBookmarked = false,
            visibility = visibility,
            timeAgo = "Just now"
        )
        _posts.value = listOf(newPost) + _posts.value
        _currentUser.value = _currentUser.value.copy(
            postsCount = _currentUser.value.postsCount + 1
        )
    }

    suspend fun createPost(post: Post): Boolean {
        _posts.value = listOf(post) + _posts.value
        return true
    }

    fun toggleBookmark(postId: String) {
        _posts.value = _posts.value.map { post ->
            if (post.id == postId) {
                val newBookmarked = !post.isBookmarked
                if (newBookmarked) {
                    _savedPosts.value = _savedPosts.value + postId
                } else {
                    _savedPosts.value = _savedPosts.value - postId
                }
                post.copy(isBookmarked = newBookmarked)
            } else post
        }
    }

    fun deletePost(postId: String) {
        _posts.value = _posts.value.filter { it.id != postId }
        _currentUser.value = _currentUser.value.copy(
            postsCount = (_currentUser.value.postsCount - 1).coerceAtLeast(0)
        )
    }

    fun togglePostVisibility(postId: String) {
        _posts.value = _posts.value.map { post ->
            if (post.id == postId) {
                val newVisibility = when (post.visibility) {
                    PostVisibility.PUBLIC -> PostVisibility.PRIVATE
                    PostVisibility.PRIVATE -> PostVisibility.PUBLIC
                    PostVisibility.FRIENDS -> PostVisibility.PRIVATE
                }
                post.copy(visibility = newVisibility)
            } else post
        }
    }

    suspend fun followUser(userId: String): Boolean {
        val current = _followedUsers.value
        _followedUsers.value = if (userId in current) current - userId else current + userId
        val isFollowing = userId in _followedUsers.value
        if (isFollowing) {
            _currentUser.value = _currentUser.value.copy(
                followingCount = _currentUser.value.followingCount + 1
            )
        } else {
            _currentUser.value = _currentUser.value.copy(
                followingCount = (_currentUser.value.followingCount - 1).coerceAtLeast(0)
            )
        }
        return true
    }

    fun isFollowing(userId: String): Boolean = userId in _followedUsers.value

    fun addAiPost(post: Post) {
        _posts.value = _posts.value + post
    }

    suspend fun sendMessage(conversationId: String, message: ChatMessage): Boolean {
        return true
    }
}
