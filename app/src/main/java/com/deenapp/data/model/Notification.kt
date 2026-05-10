package com.deenapp.data.model

data class Notification(
    val id: String = "",
    val type: NotificationType = NotificationType.LIKE,
    val fromUserId: String = "",
    val fromUserName: String = "",
    val fromUserImage: String = "",
    val message: String = "",
    val postId: String = "",
    val isRead: Boolean = false,
    val timeAgo: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

enum class NotificationType {
    LIKE,
    COMMENT,
    FOLLOW,
    SHARE,
    MESSAGE
}
