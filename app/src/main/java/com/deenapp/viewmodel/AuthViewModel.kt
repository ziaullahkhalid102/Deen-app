package com.deenapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deenapp.data.repository.DeenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null)
            try {
                // Firebase Google Sign-In integration
                // In production, this would use FirebaseAuth.signInWithCredential()
                _authState.value = _authState.value.copy(
                    isLoggedIn = true,
                    isLoading = false,
                    userId = "google_user_${System.currentTimeMillis()}",
                    error = null
                )
            } catch (e: Exception) {
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Sign-in failed"
                )
            }
        }
    }

    fun skipLogin() {
        _authState.value = _authState.value.copy(
            isLoggedIn = true,
            isProfileSetupComplete = true,
            userId = "guest_user",
            userName = "Guest"
        )
    }

    fun completeProfileSetup() {
        _authState.value = _authState.value.copy(isProfileSetupComplete = true)
    }

    fun signOut() {
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
        }
    }
}
