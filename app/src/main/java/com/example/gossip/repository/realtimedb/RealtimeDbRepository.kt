package com.example.gossip.repository.realtimedb

import com.example.gossip.utils.ResultState
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RealtimeDbRepository @Inject constructor(
    private val db: DatabaseReference
) : RealtimeRepository {
    override fun insertUserChats(userChats: RealtimeModelResponse): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)

            db.child(userChats.key!!)
                .setValue(userChats.userChats)
                .addOnSuccessListener {

                }.addOnFailureListener {

                }

            awaitClose {
                close()
            }
        }

    override fun getUser(): Flow<ResultState<List<RealtimeModelResponse>>> = callbackFlow {
        trySend(ResultState.Loading)

        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.map {
                    RealtimeModelResponse(
                        it.getValue(RealtimeModelResponse.UserChats::class.java),
                        key = it.key

                    )
                }
                trySend(ResultState.Success(items))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Failure(error.toException()))
            }

        }
        db.addValueEventListener(valueEvent)

        awaitClose {
            db.removeEventListener(valueEvent)
            close()
        }
    }

    override fun delete(key: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        db.child(key).removeValue()
            .addOnCompleteListener {
                trySend(ResultState.Success("User Deleted"))
            }
            .addOnFailureListener {
                trySend(ResultState.Failure(it))
            }


        awaitClose {
            close()
        }
    }

    override fun updateUser(res: RealtimeModelResponse): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        val map = HashMap<String, Any>()
        map["chatRooms"] = res.userChats?.chatRooms!!
        map["chatter"] = res.userChats.chatter!!

        db.child(res.key!!).updateChildren(
            map
        ).addOnCompleteListener {
            trySend(ResultState.Success("Updated Successfully..."))
        }.addOnFailureListener {
            trySend(ResultState.Failure(it))
        }

        awaitClose {
            close()
        }
    }
}