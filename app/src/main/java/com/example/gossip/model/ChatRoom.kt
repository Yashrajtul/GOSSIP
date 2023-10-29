package com.example.gossip.model

data class ChatRoom(
    val chat: Chat?,
    val key: String? = ""
){
    data class Chat(
        val members: List<String>? = emptyList(),
        val messages: List<String>? = emptyList(),
        val lastUpdated: Int
    )
}
