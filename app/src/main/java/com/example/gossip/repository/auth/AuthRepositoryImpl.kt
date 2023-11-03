package com.example.gossip.repository.auth

import android.app.Activity
import com.example.gossip.repository.auth.AuthRepository
import com.example.gossip.utils.ResultState
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authdb: FirebaseAuth
) : AuthRepository {
    private lateinit var mVerificationCode:String
    private lateinit var resendingToken:PhoneAuthProvider.ForceResendingToken
    override fun createUserWithPhone(phone: String, activity: Activity, resend: Boolean): Flow<ResultState<String>> = callbackFlow{
        trySend(ResultState.Loading)

        val onVerificationCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                TODO("Not yet implemented")
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                trySend(ResultState.Failure(p0))
            }

            override fun onCodeSent(verificationCode: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(verificationCode, forceResendingToken)
                trySend(ResultState.Success("OTP Sent Successfully"))
                mVerificationCode = verificationCode
                resendingToken = forceResendingToken
            }

        }
        val options = PhoneAuthOptions.newBuilder(authdb)
            .setPhoneNumber("+91$phone")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(onVerificationCallback)
        if(resend)
            PhoneAuthProvider.verifyPhoneNumber(options.setForceResendingToken(resendingToken).build())
        else
            PhoneAuthProvider.verifyPhoneNumber(options.build())
        awaitClose {
            close()
        }
    }


    override fun signWithCredential(otp: String): Flow<ResultState<String>> = callbackFlow{
        trySend(ResultState.Loading)
        val credential = PhoneAuthProvider.getCredential(mVerificationCode, otp)
        authdb.signInWithCredential(credential)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    trySend(ResultState.Success("OTP Verified"))
                }
            }
            .addOnFailureListener {
                trySend(ResultState.Failure(it))
            }
        awaitClose {
            close()
        }
    }

    override fun signOut(){
        authdb.signOut()
    }

    override fun currentUser(): String{
        return authdb.currentUser?.uid ?: ""
    }
}