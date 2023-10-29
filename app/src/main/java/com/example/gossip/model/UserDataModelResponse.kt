package com.example.gossip.model

import android.net.Uri
import com.google.firebase.Timestamp

data class UserDataModelResponse(
    val user: User?,
    val key: String? = ""
){
    data class User(
        val username: String? = "",
        val phone: String? = "",
        val userId: String? = "",
        val createdTimestamp: Timestamp? = null
    )
}
