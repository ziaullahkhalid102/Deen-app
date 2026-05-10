package com.deenapp.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.deenapp.ui.components.DeenBottomNavBar
import com.deenapp.ui.screens.chat.ChatScreen
import com.deenapp.ui.screens.createpost.CreatePostScreen
import com.deenapp.ui.screens.home.HomeScreen
import com.deenapp.ui.screens.notifications.NotificationsScreen
import com.deenapp.ui.screens.profile.ProfileScreen
import com.deenapp.ui.screens.search.SearchScreen
import com.deenapp.ui.screens.shorts.ShortsScreen

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Shorts : Screen("shorts")
    data object Create : Screen("create")
    data object Chat : Screen("chat")
    data object Profile : Screen("profile")
    data object Notifications : Screen("notifications")
    data object Search : Screen("search")
}

@Composable
fun DeenNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"

    val showBottomBar = currentRoute in listOf("home", "shorts", "chat", "profile")

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                DeenBottomNavBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        if (route == "create") {
                            navController.navigate(Screen.Create.route)
                        } else {
                            navController.navigate(route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToNotifications = {
                        navController.navigate(Screen.Notifications.route)
                    }
                )
            }

            composable(Screen.Shorts.route) {
                ShortsScreen()
            }

            composable(Screen.Chat.route) {
                ChatScreen()
            }

            composable(Screen.Profile.route) {
                ProfileScreen()
            }

            composable(Screen.Create.route) {
                CreatePostScreen(
                    onClose = { navController.popBackStack() }
                )
            }

            composable(Screen.Notifications.route) {
                NotificationsScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Search.route) {
                SearchScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
