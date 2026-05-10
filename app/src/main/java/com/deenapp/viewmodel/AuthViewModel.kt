package com.deenapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deenapp.data.repository.DeenRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class AuthState(
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val isProfileSetupComplete: Boolean = false,
    val userId: String = "",
    val userEmail: String = "",
    val userName: String = "",
    val userPhotoUrl: String = "",
    val error: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: DeenRepository
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            _authState.value = AuthState(
                isLoggedIn = true,
                isProfileSetupComplete = true,
                userId = currentUser.uid,
                userEmail = currentUser.email ?: "",
                userName = currentUser.displayName ?: "",
                userPhotoUrl = currentUser.photoUrl?.toString() ?: ""
            )
            repository.updateCurrentUser(
                userId = currentUser.uid,
                email = currentUser.email ?: "",
                name = currentUser.displayName ?: "",
                photoUrl = currentUser.photoUrl?.toString() ?: ""
            )
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null)
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val result = firebaseAuth.signInWithCredential(credential).await()
                val user = result.user
                _authState.value = _authState.value.copy(
                    isLoggedIn = true,
                    isLoading = false,
                    userId = user?.uid ?: "",
                    userEmail = user?.email ?: "",
                    userName = user?.displayName ?: "",
                    userPhotoUrl = user?.photoUrl?.toString() ?: "",
                    isProfileSetupComplete = false,
                    error = null
                )
                repository.updateCurrentUser(
                    userId = user?.uid ?: "",
                    email = user?.email ?: "",
                    name = user?.displayName ?: "",
                    photoUrl = user?.photoUrl?.toString() ?: ""
                )
            } catch (e: Exception) {
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Sign-in failed"
                )
            }
        }
    }

    fun signInWithGoogleAccount(id: String, email: String, name: String, photoUrl: String) {
        _authState.value = AuthState(
            isLoggedIn = true,
            isLoading = false,
            userId = id,
            userEmail = email,
            userName = name,
            userPhotoUrl = photoUrl,
            isProfileSetupComplete = false
        )
        repository.updateCurrentUser(
            userId = id,
            email = email,
            name = name,
            photoUrl = photoUrl
        )
    }

    fun skipLogin() {
        _authState.value = _authState.value.copy(
            isLoggedIn = true,
            isProfileSetupComplete = true,
            userId = "guest_user",
            userName = "Guest"
        )
        repository.updateCurrentUser(
            userId = "guest_user",
            email = "",
            name = "Guest",
            photoUrl = ""
        )
    }

    fun completeProfileSetup() {
        _authState.value = _authState.value.copy(isProfileSetupComplete = true)
    }

    fun signOut() {
        firebaseAuth.signOut()
        _authState.value = AuthState()
    }

    fun simulateGoogleSignIn() {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null)
            kotlinx.coroutines.delay(1500)
            _authState.value = _authState.value.copy(
                isLoggedIn = true,
                isLoading = false,
                userId = "google_user_001",
                userEmail = "user@gmail.com",
                userName = "Muhammad Usman",
                userPhotoUrl = "",
                isProfileSetupComplete = false,
                error = null
            )
            repository.updateCurrentUser(
                userId = "google_user_001",
                email = "user@gmail.com",
                name = "Muhammad Usman",
                photoUrl = ""
            )
        }
    }
}
