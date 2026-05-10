package com.deenapp.ui.screens.shorts

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            beyondViewportPageCount = 1
        ) { page ->
            ShortVideoItem(
                video = videos[page],
                onLikeClick = { viewModel.toggleLike(videos[page].id) },
                onFollowClick = { viewModel.toggleFollow(videos[page].id) }
            )
        }

        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.5f),
                            Color.Transparent
                        )
                    )
                )
                .padding(top = 44.dp, start = 8.dp, end = 8.dp, bottom = 16.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Camera",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Following",
                        color = if (selectedTab == 0) Color.White else Color.White.copy(alpha = 0.5f),
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 16.sp,
                        modifier = Modifier.clickable { viewModel.selectTab(0) }
                    )
                    if (selectedTab == 0) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .width(24.dp)
                                .height(2.dp)
                                .background(Color.White, RoundedCornerShape(1.dp))
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "For You",
                        color = if (selectedTab == 1) Color.White else Color.White.copy(alpha = 0.5f),
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 16.sp,
                        modifier = Modifier.clickable { viewModel.selectTab(1) }
                    )
                    if (selectedTab == 1) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .width(24.dp)
                                .height(2.dp)
                                .background(Color.White, RoundedCornerShape(1.dp))
                        )
                    }
                }
            }

            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
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
    var isPaused by remember { mutableStateOf(false) }
    var isBookmarked by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "musicDisc")
    val discRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "discRotation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable { isPaused = !isPaused }
    ) {
        // Video background gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0D1B0E),
                            Color(0xFF1A3A1C),
                            Color(0xFF0D1B0E),
                            Color.Black
                        )
                    )
                )
        )

        // Islamic text overlay in center
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = video.overlayText,
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 36.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "— ${video.userName}",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }

        // Pause icon
        if (isPaused) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play",
                tint = Color.White.copy(alpha = 0.7f),
                modifier = Modifier
                    .size(72.dp)
                    .align(Alignment.Center)
            )
        }

        // Right side action buttons (TikTok style)
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 10.dp, bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Profile avatar with follow button
            Box(contentAlignment = Alignment.BottomCenter) {
                ProfileAvatar(
                    imageUrl = video.userProfileImage,
                    size = 50.dp,
                    borderColor = Color.White,
                    borderWidth = 2.dp
                )
                if (!video.isFollowing) {
                    Box(
                        modifier = Modifier
                            .offset(y = 10.dp)
                            .size(22.dp)
                            .background(DeenGreenPrimary, CircleShape)
                            .border(1.5.dp, Color.White, CircleShape)
                            .clickable { onFollowClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("+", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Like
            ShortsActionButton(
                icon = if (video.isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                count = formatCount(video.likesCount),
                tint = if (video.isLiked) DeenLikeRed else Color.White,
                onClick = onLikeClick
            )

            // Comment
            ShortsActionButton(
                icon = Icons.Default.ChatBubble,
                count = formatCount(video.commentsCount),
                onClick = { }
            )

            // Bookmark
            ShortsActionButton(
                icon = if (isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                count = if (isBookmarked) "Saved" else "Save",
                tint = if (isBookmarked) DeenGreenPrimary else Color.White,
                onClick = { isBookmarked = !isBookmarked }
            )

            // Share
            ShortsActionButton(
                icon = Icons.Default.Share,
                count = formatCount(video.sharesCount),
                onClick = { }
            )

            // Rotating music disc
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .rotate(discRotation)
                    .clip(CircleShape)
                    .background(Color.DarkGray)
                    .border(2.dp, Color.Gray.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = "Music",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Bottom info section
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 14.dp, bottom = 96.dp, end = 80.dp)
        ) {
            // Username + follow
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "@${video.userName.lowercase().replace(" ", "")}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                if (!video.isFollowing) {
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .border(1.dp, Color.White, RoundedCornerShape(4.dp))
                            .clickable { onFollowClick() }
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Follow",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Text(
                text = video.description,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Hashtags
            Text(
                text = video.hashtags.joinToString(" "),
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Music ticker
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.White.copy(alpha = 0.1f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Original Sound — ${video.userName}",
                    color = Color.White,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Bottom progress bar
        LinearProgressIndicator(
            progress = { 0.6f },
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .align(Alignment.BottomCenter),
            color = Color.White,
            trackColor = Color.White.copy(alpha = 0.2f)
        )
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
            modifier = Modifier.size(30.dp)
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
