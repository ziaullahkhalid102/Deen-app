package com.deenapp.data.repository

import com.deenapp.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeenRepository @Inject constructor() {

    fun getCurrentUser(): Flow<User> = flow {
        emit(SampleData.currentUser)
    }

    fun getStories(): Flow<List<Story>> = flow {
        emit(SampleData.stories)
    }

    fun getPosts(): Flow<List<Post>> = flow {
        emit(SampleData.posts)
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

    fun getSearchPosts(): Flow<List<Post>> = flow {
        emit(SampleData.posts.take(3))
    }

    suspend fun toggleLike(postId: String): Boolean {
        return true
    }

    suspend fun createPost(post: Post): Boolean {
        return true
    }

    suspend fun followUser(userId: String): Boolean {
        return true
    }

    suspend fun sendMessage(conversationId: String, message: ChatMessage): Boolean {
        return true
    }
}
