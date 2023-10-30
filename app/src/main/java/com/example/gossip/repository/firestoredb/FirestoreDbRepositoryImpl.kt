package com.example.gossip.repository.firestoredb

import android.net.Uri
import com.example.gossip.model.ChatRoom
import com.example.gossip.model.Messages
import com.example.gossip.model.UserDataModelResponse
import com.example.gossip.utils.ResultState
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirestoreDbRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val storage: StorageReference
) : FirestoreRepository {
    override fun getUsers(): Flow<ResultState<List<UserDataModelResponse>>> =
        callbackFlow {
            trySend(ResultState.Loading)

            db.collection("user")
                .get()
                .addOnSuccessListener {
                    val items = it.map { data ->
                        UserDataModelResponse(
                            user = UserDataModelResponse.User(
                                username = data["username"] as String?,
                                phone = data["phone"] as String?,
                                createdTimestamp = data["createdTimestamp"] as Timestamp
                            ),
                            key = data.id
                        )
                    }
                    trySend(ResultState.Success(items))
                }.addOnFailureListener {
                    trySend(ResultState.Failure(it))
                }

            awaitClose {
                close()
            }
        }

    override fun getUserData(key: String): Flow<ResultState<UserDataModelResponse.User?>> = callbackFlow{
        trySend(ResultState.Loading)

        db.collection("user")
            .document(key)
            .get()
            .addOnSuccessListener {data->
                val user: UserDataModelResponse.User? = data.toObject(UserDataModelResponse.User::class.java)
                trySend(ResultState.Success(user))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }

        awaitClose {
            close()
        }
    }

    override fun delete(key: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        db.collection("user")
            .document(key)
            .delete()
            .addOnCompleteListener {
                if (it.isSuccessful)
                    trySend(ResultState.Success("Deleted Successfully.."))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }

        awaitClose {
            close()
        }
    }

    override fun uploadPic(image: Uri, key: String): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)

            storage.child("profilePictures/${key}")
                .putFile(image)
                .addOnCompleteListener {
                    if (it.isSuccessful)
                        trySend(ResultState.Success("Picture uploaded"))
                }.addOnFailureListener {
                    trySend(ResultState.Failure(it))
                }

            awaitClose {
                close()
            }
        }

    override fun getProfilePic(key: String): Flow<ResultState<Uri>> = callbackFlow {
        trySend(ResultState.Loading)

        storage.child("profilePictures")
            .child(key)
            .downloadUrl
            .addOnSuccessListener {
                trySend(ResultState.Success(it))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }
        awaitClose {
            close()
        }
    }

    override fun updateUser(res: UserDataModelResponse): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        db.collection("user")
            .document(res.key!!)
            .set(res.user!!)
            .addOnCompleteListener {
                if (it.isSuccessful)
                    trySend(ResultState.Success("User Updated"))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }

        awaitClose {
            close()
        }
    }

    override fun createChatRoom(chat: ChatRoom.Chat): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        db.collection("chatroom")
            .add(chat)
            .addOnCompleteListener {
                trySend(ResultState.Success(it.result.id))

            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }

        awaitClose {
            close()
        }
    }

    override fun sendMessage(message: Messages.Message): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        db.collection("messages")
            .add(message)
            .addOnCompleteListener {
                trySend(ResultState.Success(it.result.id))
            }
            .addOnFailureListener {
                trySend(ResultState.Failure(it))
            }

        awaitClose {
            close()
        }
    }

    override fun deleteMessage(key: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        db.collection("messages")
            .document(key)
            .delete()
            .addOnCompleteListener {

                trySend(ResultState.Success("Message deleted successfully..."))
            }.addOnFailureListener {
                trySend(ResultState.Failure(it))
            }

        awaitClose {
            close()
        }
    }


}