package com.example.gossip.repository.firestoredb

import android.net.Uri
import com.example.gossip.model.ChatRoom
import com.example.gossip.model.Messages
import com.example.gossip.model.UserDataModelResponse
import com.example.gossip.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {
    fun getUsers(): Flow<ResultState<List<UserDataModelResponse>>>

    fun getUserData(key: String): Flow<ResultState<UserDataModelResponse>>

    fun delete(
        key: String
    ): Flow<ResultState<String>>

    fun uploadPic(
        image: Uri,
        key: String
    ): Flow<ResultState<String>>

    fun getProfilePic(
        key: String
    ): Flow<ResultState<Uri>>

    fun updateUser(
        res: UserDataModelResponse
    ): Flow<ResultState<String>>

    fun createChatRoom(
        chat: ChatRoom.Chat
    ): Flow<ResultState<String>>

    fun sendMessage(
        message: Messages.Message
    ): Flow<ResultState<String>>

    fun deleteMessage(
        key: String
    ): Flow<ResultState<String>>
}