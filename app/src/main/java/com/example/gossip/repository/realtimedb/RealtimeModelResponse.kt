package com.example.gossip.repository.realtimedb

data class RealtimeModelResponse(
    val userChats: UserChats?,
    val key: String?

){
    data class UserChats(
        val chatRooms: List<String>? = emptyList(),
        val chatter: String? = ""
    )
}
