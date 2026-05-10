package com.deenapp.data.model

data class ChatConversation(
    val id: String = "",
    val participantId: String = "",
    val participantName: String = "",
    val participantImage: String = "",
    val lastMessage: String = "",
    val lastMessageTime: String = "",
    val unreadCount: Int = 0,
    val isOnline: Boolean = false,
    val isGroup: Boolean = false,
    val groupName: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)

data class ChatMessage(
    val id: String = "",
    val conversationId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val content: String = "",
    val imageUrl: String = "",
    val isRead: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
