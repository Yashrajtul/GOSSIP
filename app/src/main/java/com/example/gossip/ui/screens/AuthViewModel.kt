package com.example.gossip.ui.screens

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.example.gossip.repository.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel(){

    fun createUserWithPhone(
        phone:String,
        activity: Activity
    ) = repo.createUserWithPhone(phone, activity)

    fun signInWithCredential(
        otp:String
    ) = repo.signWithCredential(otp)
}