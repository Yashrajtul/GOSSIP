package com.example.gossip.ui.screens

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.example.gossip.repository.auth.AuthRepository
import javax.inject.Inject

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