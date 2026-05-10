package com.deenapp.data.repository

import com.deenapp.data.model.*

object SampleData {

    val currentUser = User(
        id = "user_1",
        username = "@m.usman23",
        displayName = "Muhammad Usman",
        bio = "A believer trying to grow closer to Allah \uD83C\uDF3F",
        profileImageUrl = "",
        postsCount = 120,
        followersCount = 1200,
        followingCount = 340,
        isOnline = true
    )

    val stories = listOf(
        Story(id = "s1", userId = "user_1", userName = "Your Story", isOwnStory = true),
        Story(id = "s2", userId = "user_2", userName = "Ayesha"),
        Story(id = "s3", userId = "user_3", userName = "Muhammad"),
        Story(id = "s4", userId = "user_4", userName = "Fatima"),
        Story(id = "s5", userId = "user_5", userName = "Bilal"),
        Story(id = "s6", userId = "user_6", userName = "Omar"),
        Story(id = "s7", userId = "user_7", userName = "Khadijah")
    )

    val posts = listOf(
        Post(
            id = "p1",
            userId = "user_8",
            userName = "Abu Hurairah",
            content = "The best among you are those who learn the Quran and teach it.\n(Sahih Bukhari)",
            imageUrl = "mosque_1",
            likesCount = 128,
            commentsCount = 23,
            sharesCount = 12,
            timeAgo = "2h ago"
        ),
        Post(
            id = "p2",
            userId = "user_9",
            userName = "Islamic Reminders",
            content = "So remember Me; I will remember you.\n(Quran 2:152)",
            likesCount = 256,
            commentsCount = 45,
            sharesCount = 34,
            timeAgo = "5h ago"
        ),
        Post(
            id = "p3",
            userId = "user_10",
            userName = "Quran Daily",
            content = "Indeed, with hardship comes ease.\n(Quran 94:6)",
            imageUrl = "mosque_2",
            likesCount = 512,
            commentsCount = 67,
            sharesCount = 45,
            timeAgo = "8h ago"
        ),
        Post(
            id = "p4",
            userId = "user_11",
            userName = "Hadith Corner",
            content = "The strong man is not the one who wrestles, but the one who controls himself in anger.\n(Sahih Bukhari)",
            likesCount = 89,
            commentsCount = 15,
            sharesCount = 8,
            timeAgo = "12h ago"
        ),
        Post(
            id = "p5",
            userId = "user_12",
            userName = "Deen Wisdom",
            content = "And whoever puts their trust in Allah, then He is sufficient for them.\n(Quran 65:3)",
            imageUrl = "mosque_3",
            likesCount = 345,
            commentsCount = 56,
            sharesCount = 23,
            timeAgo = "1d ago"
        )
    )

    val shortVideos = listOf(
        ShortVideo(
            id = "v1",
            userId = "user_13",
            userName = "Deen Reminders",
            overlayText = "And Allah is\nwith the\npatient.\n(Quran 2:153)",
            description = "Beautiful reminder",
            hashtags = listOf("#Islam", "#Quran", "#Patience"),
            likesCount = 2400,
            commentsCount = 128,
            sharesCount = 342,
            viewsCount = 15000
        ),
        ShortVideo(
            id = "v2",
            userId = "user_14",
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
            userId = "user_15",
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

    val chatConversations = listOf(
        ChatConversation(
            id = "c1",
            participantName = "Ayesha Khan",
            lastMessage = "Assalamu Alaikum",
            lastMessageTime = "10:30 AM",
            unreadCount = 2,
            isOnline = true
        ),
        ChatConversation(
            id = "c2",
            participantName = "Study Circle",
            lastMessage = "Bilal: JazakAllah Khair",
            lastMessageTime = "9:45 AM",
            unreadCount = 5,
            isGroup = true,
            groupName = "Study Circle"
        ),
        ChatConversation(
            id = "c3",
            participantName = "Muhammad Ali",
            lastMessage = "Draft: Okay",
            lastMessageTime = "Yesterday",
            isOnline = false
        ),
        ChatConversation(
            id = "c4",
            participantName = "Quran Learners",
            lastMessage = "You: InshaAllah",
            lastMessageTime = "Yesterday",
            unreadCount = 1,
            isGroup = true,
            groupName = "Quran Learners"
        ),
        ChatConversation(
            id = "c5",
            participantName = "Sisterhood",
            lastMessage = "Fatima: BarakAllah feek",
            lastMessageTime = "Mon",
            isOnline = false
        ),
        ChatConversation(
            id = "c6",
            participantName = "Abu Hurairah",
            lastMessage = "Wa Alaikum Assalam",
            lastMessageTime = "Sun",
            isOnline = true
        )
    )

    val notifications = listOf(
        Notification(
            id = "n1",
            type = NotificationType.LIKE,
            fromUserName = "Ayesha Khan",
            message = "liked your post.",
            timeAgo = "2m ago"
        ),
        Notification(
            id = "n2",
            type = NotificationType.COMMENT,
            fromUserName = "Abu Hurairah",
            message = "commented on your post.",
            timeAgo = "15m ago"
        ),
        Notification(
            id = "n3",
            type = NotificationType.FOLLOW,
            fromUserName = "Bilal",
            message = "started following you.",
            timeAgo = "1h ago"
        ),
        Notification(
            id = "n4",
            type = NotificationType.SHARE,
            fromUserName = "Islamic Reminders",
            message = "shared your post.",
            timeAgo = "2h ago"
        ),
        Notification(
            id = "n5",
            type = NotificationType.LIKE,
            fromUserName = "Fatima",
            message = "liked your comment.",
            timeAgo = "3h ago"
        ),
        Notification(
            id = "n6",
            type = NotificationType.COMMENT,
            fromUserName = "Muhammad Ali",
            message = "replied to your comment.",
            timeAgo = "5h ago"
        )
    )

    val searchUsers = listOf(
        User(id = "su1", displayName = "Ayesha Khan", username = "@ayesha.khan"),
        User(id = "su2", displayName = "Abu Hurairah", username = "@abu.hurairah"),
        User(id = "su3", displayName = "Muhammad Ali", username = "@m.ali.official"),
        User(id = "su4", displayName = "Fatima Zahra", username = "@fatima.z"),
        User(id = "su5", displayName = "Bilal Ahmad", username = "@bilal.ahmad")
    )
}
