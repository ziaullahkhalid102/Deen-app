package com.deenapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deenapp.ui.theme.DeenGreenPrimary

data class BottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem("home", "Home", Icons.Filled.Home, Icons.Outlined.Home),
    BottomNavItem("shorts", "Shorts", Icons.Filled.PlayCircle, Icons.Outlined.PlayCircleOutline),
    BottomNavItem("create", "Create", Icons.Filled.Add, Icons.Filled.Add),
    BottomNavItem("chat", "Chat", Icons.Filled.ChatBubble, Icons.Outlined.ChatBubbleOutline),
    BottomNavItem("profile", "Profile", Icons.Filled.Person, Icons.Outlined.Person)
)

@Composable
fun DeenBottomNavBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp,
            modifier = Modifier
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .height(68.dp)
        ) {
            bottomNavItems.forEach { item ->
                if (item.route == "create") {
                    NavigationBarItem(
                        selected = false,
                        onClick = { onNavigate(item.route) },
                        icon = {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                DeenGreenPrimary,
                                                DeenGreenPrimary.copy(alpha = 0.8f)
                                            )
                                        ),
                                        shape = CircleShape
                                    )
                                    .shadow(4.dp, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "Create",
                                    tint = Color.White,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        },
                        label = { },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.surface
                        )
                    )
                } else {
                    val isSelected = currentRoute == item.route
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { onNavigate(item.route) },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.label,
                                modifier = Modifier.size(if (isSelected) 26.dp else 24.dp)
                            )
                        },
                        label = {
                            Text(
                                text = item.label,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeenGreenPrimary,
                            selectedTextColor = DeenGreenPrimary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            indicatorColor = DeenGreenPrimary.copy(alpha = 0.08f)
                        )
                    )
                }
            }
        }
    }
}
