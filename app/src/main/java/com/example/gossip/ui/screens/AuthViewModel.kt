package com.example.gossip.ui.screens

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gossip.repository.auth.AuthRepository
import com.example.gossip.utils.ResultState
import com.example.gossip.utils.showMsg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository
) : ViewModel(){

    var isDialog by mutableStateOf(false)
    private fun createUserWithPhone(
        phone:String,
        activity: Activity
    ) = repo.createUserWithPhone(phone, activity)

    private fun signInWithCredential(
        otp:String
    ) = repo.signWithCredential(otp)

    fun sendOtp(
        mobile:String,
        activity: Activity
    ){
        viewModelScope.launch {
            createUserWithPhone(
                mobile,
                activity
            ).collect{
                when(it){
                    is ResultState.Success->{
                        isDialog = false
                        activity.showMsg(it.data)
                    }
                    is ResultState.Failure->{
                        isDialog = false
                        activity.showMsg(it.msg.toString())
                    }
                    ResultState.Loading->{
                        isDialog = true
                    }
                }
            }
        }
    }

    fun verifyOtp(
        otp: String,
        activity: Activity
    ){
        viewModelScope.launch{
            signInWithCredential(
                otp
            ).collect{
                when(it){
                    is ResultState.Success->{
                        isDialog = false
                        activity.showMsg(it.data)
                    }
                    is ResultState.Failure->{
                        isDialog = false
                        activity.showMsg(it.msg.toString())
                    }
                    ResultState.Loading->{
                        isDialog = true
                    }
                }
            }
        }
    }
}