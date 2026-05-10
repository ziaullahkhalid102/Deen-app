package com.deenapp.data.model

data class Post(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userProfileImage: String = "",
    val content: String = "",
    val imageUrl: String = "",
    val videoUrl: String = "",
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val sharesCount: Int = 0,
    val isLiked: Boolean = false,
    val isBookmarked: Boolean = false,
    val visibility: PostVisibility = PostVisibility.PUBLIC,
    val feeling: String = "",
    val location: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val timeAgo: String = ""
)

enum class PostVisibility {
    PUBLIC,
    FRIENDS,
    PRIVATE
}
