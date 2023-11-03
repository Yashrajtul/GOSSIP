package com.example.gossip.ui.splash

import androidx.lifecycle.ViewModel
import com.example.gossip.repository.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val auth: AuthRepository
) : ViewModel(){
    val isLoggedIn: Boolean = auth.currentUser() != ""
}