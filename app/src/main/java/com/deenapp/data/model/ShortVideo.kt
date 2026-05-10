package com.deenapp.data.model

data class ShortVideo(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userProfileImage: String = "",
    val videoUrl: String = "",
    val thumbnailUrl: String = "",
    val overlayText: String = "",
    val description: String = "",
    val hashtags: List<String> = emptyList(),
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val sharesCount: Int = 0,
    val viewsCount: Int = 0,
    val isLiked: Boolean = false,
    val isFollowing: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
