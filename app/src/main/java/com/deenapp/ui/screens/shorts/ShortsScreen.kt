package com.deenapp.ui.screens.shorts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.deenapp.data.model.ShortVideo
import com.deenapp.ui.components.ProfileAvatar
import com.deenapp.ui.theme.DeenGreenPrimary
import com.deenapp.ui.theme.DeenLikeRed
import com.deenapp.viewmodel.ShortsViewModel

@Composable
fun ShortsScreen(
    viewModel: ShortsViewModel = hiltViewModel()
) {
    val videos by viewModel.videos.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()

    if (videos.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { videos.size })

    Box(modifier = Modifier.fillMaxSize()) {
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            ShortVideoItem(
                video = videos[page],
                onLikeClick = { viewModel.toggleLike(videos[page].id) },
                onFollowClick = { viewModel.toggleFollow(videos[page].id) }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 16.dp, end = 16.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                Text(
                    text = "Following",
                    color = if (selectedTab == 0) Color.White else Color.White.copy(alpha = 0.6f),
                    fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 16.sp,
                    modifier = Modifier.clickable { viewModel.selectTab(0) }
                )
                Text(
                    text = "For You",
                    color = if (selectedTab == 1) Color.White else Color.White.copy(alpha = 0.6f),
                    fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 16.sp,
                    modifier = Modifier.clickable { viewModel.selectTab(1) }
                )
            }

            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun ShortVideoItem(
    video: ShortVideo,
    onLikeClick: () -> Unit,
    onFollowClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DeenGreenPrimary.copy(alpha = 0.8f),
                        Color.Black.copy(alpha = 0.9f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = video.overlayText,
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 38.sp
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 12.dp, bottom = 120.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                ProfileAvatar(
                    imageUrl = video.userProfileImage,
                    size = 48.dp,
                    borderColor = Color.White,
                    borderWidth = 2.dp
                )
                if (!video.isFollowing) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(DeenGreenPrimary, CircleShape)
                            .border(1.dp, Color.White, CircleShape)
                            .clickable { onFollowClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            ShortsActionButton(
                icon = if (video.isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                count = formatCount(video.likesCount),
                tint = if (video.isLiked) DeenLikeRed else Color.White,
                onClick = onLikeClick
            )

            ShortsActionButton(
                icon = Icons.Default.ChatBubble,
                count = formatCount(video.commentsCount),
                onClick = { }
            )

            ShortsActionButton(
                icon = Icons.Default.Share,
                count = formatCount(video.sharesCount),
                onClick = { }
            )

            Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = "Music",
                tint = Color.White,
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.2f))
                    .padding(6.dp)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 100.dp, end = 80.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = video.userName,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                if (!video.isFollowing) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Follow",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .border(1.dp, Color.White, RoundedCornerShape(4.dp))
                            .padding(horizontal = 10.dp, vertical = 3.dp)
                            .clickable { onFollowClick() }
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = video.description,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = video.hashtags.joinToString(" "),
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun ShortsActionButton(
    icon: ImageVector,
    count: String,
    tint: Color = Color.White,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = count,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

private fun formatCount(count: Int): String {
    return when {
        count >= 1_000_000 -> "${count / 1_000_000}M"
        count >= 1_000 -> String.format("%.1fK", count / 1000.0)
        else -> count.toString()
    }
}
