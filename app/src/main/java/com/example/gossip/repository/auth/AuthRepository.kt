package com.example.gossip.repository.auth

import android.app.Activity
import com.example.gossip.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun createUserWithPhone(
        phone:String,
        activity: Activity,
        resend: Boolean
    ):Flow<ResultState<String>>

    fun signWithCredential(
        otp: String
    ):Flow<ResultState<String>>

    fun currentUser():String
}