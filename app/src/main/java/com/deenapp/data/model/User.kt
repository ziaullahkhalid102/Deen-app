package com.deenapp.data.model

data class User(
    val id: String = "",
    val username: String = "",
    val displayName: String = "",
    val email: String = "",
    val bio: String = "",
    val profileImageUrl: String = "",
    val bannerImageUrl: String = "",
    val postsCount: Int = 0,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val isOnline: Boolean = false,
    val isVerified: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
