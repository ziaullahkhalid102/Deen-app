package com.deenapp.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.deenapp.ui.components.DeenBottomNavBar
import com.deenapp.ui.screens.auth.WelcomeScreen
import com.deenapp.ui.screens.chat.ChatDetailScreen
import com.deenapp.ui.screens.chat.ChatScreen
import com.deenapp.ui.screens.createpost.CreatePostScreen
import com.deenapp.ui.screens.home.HomeScreen
import com.deenapp.ui.screens.notifications.NotificationsScreen
import com.deenapp.ui.screens.onboarding.ProfileSetupScreen
import com.deenapp.ui.screens.profile.ProfileScreen
import com.deenapp.ui.screens.profile.UserProfileScreen
import com.deenapp.ui.screens.search.SearchScreen
import com.deenapp.ui.screens.settings.SettingsScreen
import com.deenapp.ui.screens.shorts.ShortsScreen
import com.deenapp.ui.screens.splash.SplashScreen
import com.deenapp.viewmodel.AuthViewModel
import com.deenapp.viewmodel.HomeViewModel

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Welcome : Screen("welcome")
    data object ProfileSetup : Screen("profile_setup")
    data object Home : Screen("home")
    data object Shorts : Screen("shorts")
    data object Create : Screen("create")
    data object Chat : Screen("chat")
    data object Profile : Screen("profile")
    data object Notifications : Screen("notifications")
    data object Search : Screen("search")
    data object Settings : Screen("settings")
    data object ChatDetail : Screen("chat_detail/{contactName}") {
        fun createRoute(contactName: String) = "chat_detail/${android.net.Uri.encode(contactName)}"
    }
    data object UserProfile : Screen("user_profile/{userName}") {
        fun createRoute(userName: String) = "user_profile/${android.net.Uri.encode(userName)}"
    }
}

@Composable
fun DeenNavigation(
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "splash"
    val authState by authViewModel.authState.collectAsState()
    val homeViewModel: HomeViewModel = hiltViewModel()

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
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(
                    onSplashComplete = {
                        val destination = if (authState.isLoggedIn) {
                            if (authState.isProfileSetupComplete) Screen.Home.route
                            else Screen.ProfileSetup.route
                        } else {
                            Screen.Welcome.route
                        }
                        navController.navigate(destination) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(
                Screen.Welcome.route,
                enterTransition = { fadeIn(animationSpec = tween(500)) }
            ) {
                WelcomeScreen(
                    onGoogleSignIn = {
                        authViewModel.simulateGoogleSignIn()
                        navController.navigate(Screen.ProfileSetup.route) {
                            popUpTo(Screen.Welcome.route) { inclusive = true }
                        }
                    },
                    onGoogleSignInWithToken = { idToken ->
                        authViewModel.signInWithGoogle(idToken)
                        navController.navigate(Screen.ProfileSetup.route) {
                            popUpTo(Screen.Welcome.route) { inclusive = true }
                        }
                    },
                    onGoogleAccountSignIn = { id, email, name, photoUrl ->
                        authViewModel.signInWithGoogleAccount(id, email, name, photoUrl)
                        navController.navigate(Screen.ProfileSetup.route) {
                            popUpTo(Screen.Welcome.route) { inclusive = true }
                        }
                    },
                    onSkipLogin = {
                        authViewModel.skipLogin()
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Welcome.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(
                Screen.ProfileSetup.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }
            ) {
                ProfileSetupScreen(
                    onComplete = {
                        authViewModel.completeProfileSetup()
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.ProfileSetup.route) { inclusive = true }
                        }
                    },
                    onSkip = {
                        authViewModel.completeProfileSetup()
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.ProfileSetup.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = homeViewModel,
                    onNavigateToNotifications = {
                        navController.navigate(Screen.Notifications.route)
                    },
                    onNavigateToSearch = {
                        navController.navigate(Screen.Search.route)
                    },
                    onNavigateToUserProfile = { userName ->
                        navController.navigate(Screen.UserProfile.createRoute(userName))
                    }
                )
            }

            composable(Screen.Shorts.route) {
                ShortsScreen()
            }

            composable(Screen.Chat.route) {
                ChatScreen(
                    onChatClick = { contactName ->
                        navController.navigate(Screen.ChatDetail.createRoute(contactName))
                    }
                )
            }

            composable(
                Screen.ChatDetail.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                exitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
            ) { backStackEntry ->
                val contactName = backStackEntry.arguments?.getString("contactName") ?: "Chat"
                ChatDetailScreen(
                    contactName = contactName,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    onNavigateToSettings = {
                        navController.navigate(Screen.Settings.route)
                    }
                )
            }

            composable(
                Screen.Create.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                exitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
            ) {
                val currentUser by homeViewModel.currentUser.collectAsState()
                CreatePostScreen(
                    onClose = { navController.popBackStack() },
                    userName = currentUser.displayName,
                    userPhotoUrl = currentUser.profileImageUrl,
                    onPost = { content, mediaUris ->
                        homeViewModel.addPost(
                            content = content,
                            mediaUris = mediaUris.map { it.toString() }
                        )
                    }
                )
            }

            composable(
                Screen.Notifications.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                exitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
            ) {
                NotificationsScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                Screen.Search.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                exitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
            ) {
                SearchScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                Screen.Settings.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                exitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
            ) {
                SettingsScreen(
                    onBack = { navController.popBackStack() },
                    onSignOut = {
                        authViewModel.signOut()
                        navController.navigate(Screen.Welcome.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable(
                Screen.UserProfile.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                exitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
            ) { backStackEntry ->
                val userName = backStackEntry.arguments?.getString("userName") ?: ""
                UserProfileScreen(
                    userName = userName,
                    onBack = { navController.popBackStack() },
                    onChatClick = { name ->
                        navController.navigate(Screen.ChatDetail.createRoute(name))
                    },
                    onFollowToggle = { }
                )
            }
        }
    }
}
