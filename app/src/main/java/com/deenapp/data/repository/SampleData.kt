package com.deenapp.data.repository

import com.deenapp.data.model.*

object SampleData {

    val currentUser = User(
        id = "",
        username = "",
        displayName = "",
        bio = "",
        profileImageUrl = "",
        postsCount = 0,
        followersCount = 0,
        followingCount = 0,
        isOnline = true
    )

    val stories = listOf(
        Story(id = "s1", userId = "current_user", userName = "Your Story", isOwnStory = true)
    )

    val posts = emptyList<Post>()

    val shortVideos = listOf(
        ShortVideo(
            id = "v1",
            userId = "deen_ai",
            userName = "Deen Reminders",
            overlayText = "And Allah is\nwith the\npatient.\n(Quran 2:153)",
            description = "Beautiful reminder about patience",
            hashtags = listOf("#Islam", "#Quran", "#Patience"),
            likesCount = 2400,
            commentsCount = 128,
            sharesCount = 342,
            viewsCount = 15000
        ),
        ShortVideo(
            id = "v2",
            userId = "deen_ai",
            userName = "Islamic Wisdom",
            overlayText = "Verily, in the\nremembrance of\nAllah do hearts\nfind rest.\n(Quran 13:28)",
            description = "Find peace in dhikr",
            hashtags = listOf("#Dhikr", "#Peace", "#Islam"),
            likesCount = 3200,
            commentsCount = 215,
            sharesCount = 456,
            viewsCount = 22000
        ),
        ShortVideo(
            id = "v3",
            userId = "deen_ai",
            userName = "Quran Recitation",
            overlayText = "He is Allah,\nthe One.\nAllah, the\nEternal Refuge.\n(Quran 112:1-2)",
            description = "Surah Al-Ikhlas",
            hashtags = listOf("#Quran", "#Recitation", "#SurahIkhlas"),
            likesCount = 5100,
            commentsCount = 312,
            sharesCount = 678,
            viewsCount = 45000
        )
    )

    val chatConversations = emptyList<ChatConversation>()

    val notifications = emptyList<Notification>()

    val searchUsers = emptyList<User>()
}
