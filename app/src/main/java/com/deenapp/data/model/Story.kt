package com.deenapp.data.model

data class Story(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userProfileImage: String = "",
    val imageUrl: String = "",
    val isViewed: Boolean = false,
    val isOwnStory: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
