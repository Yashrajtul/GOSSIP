package com.example.gossip.ui.settings

import android.app.Activity
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gossip.model.UserDataModelResponse
import com.example.gossip.repository.auth.AuthRepository
import com.example.gossip.repository.firestoredb.FirestoreRepository
import com.example.gossip.utils.ResultState
import com.example.gossip.utils.showMsg
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val auth: AuthRepository,
    private val fstoreRepo: FirestoreRepository
) : ViewModel(){
    private val _settingUiState = MutableStateFlow(SettingsState())
    val settingUiState: StateFlow<SettingsState> = _settingUiState.asStateFlow()

    private var userId: String = auth.currentUser()
    init {
        if(userId != "") {
            getProfilePic(userId)
            getUserData(userId)
        }
    }
    fun getUserName(username: String) {
        _settingUiState.update { it.copy(username = username) }
    }

    fun getImage(image: Uri?) {
        _settingUiState.update { it.copy(image = image) }
    }

    fun signOut(){
        auth.signOut()
    }
    private fun getProfilePic(userId: String) {
        viewModelScope.launch {
            fstoreRepo.getProfilePic(userId)
                .collect { imageUri ->
                    when (imageUri) {
                        is ResultState.Success -> { _settingUiState.update {
                            it.copy(isDialog = false, image = imageUri.data)
                        } }
                        is ResultState.Failure -> { _settingUiState.update { it.copy(isDialog = false) } }
                        ResultState.Loading -> { _settingUiState.update { it.copy(isDialog = true) } }
                    }
                }
        }
    }
    private fun getUserData(userId: String) {
        viewModelScope.launch {
            fstoreRepo.getUserData(userId)
                .collect { user ->
                    when (user) {
                        is ResultState.Success -> {
                            _settingUiState.update { it.copy(isDialog = false)}
                            if(user.data != null)
                                _settingUiState.update { it.copy( username = user.data.username!!, phoneNumber = user.data.phone!! ) }
                        }
                        is ResultState.Failure -> { _settingUiState.update { it.copy(isDialog = false) } }
                        ResultState.Loading -> { _settingUiState.update { it.copy(isDialog = true) } }
                    }
                }
        }
    }
    private fun updateUser(user: UserDataModelResponse, activity: Activity) {
        viewModelScope.launch {
            fstoreRepo.updateUser(user)
                .collect { it ->
                    when (it) {
                        is ResultState.Success -> {
                            _settingUiState.update { it.copy(isDialog = false) }
                            activity.showMsg(it.data)
                        }

                        is ResultState.Failure -> {
                            _settingUiState.update { it.copy(isDialog = false) }
                            activity.showMsg(it.toString())
                        }

                        ResultState.Loading -> {
                            _settingUiState.update { it.copy(isDialog = true) }
                        }
                    }
                }
        }
    }

    private fun updateProfilePicture(image: Uri) {
        viewModelScope.launch {
            fstoreRepo.uploadPic(image, userId)
                .collect {it->
                    when (it) {
                        is ResultState.Success -> { _settingUiState.update { it.copy(isDialog = false) }}
                        is ResultState.Failure -> { _settingUiState.update { it.copy(isDialog = false) }}
                        ResultState.Loading -> { _settingUiState.update { it.copy(isDialog = true) }}
                    }
                }
        }
    }

    fun updateProfile(activity: Activity) {
        _settingUiState.update {
            it.copy(isError = _settingUiState.value.username.length < 3)
        }
        if (settingUiState.value.isError) {
            activity.showMsg("Username length should be at least 3 chars")
            return
        }
        val user = UserDataModelResponse(
            user = UserDataModelResponse.User(
                username = settingUiState.value.username,
                phone = settingUiState.value.phoneNumber,
                userId = userId,
                createdTimestamp = settingUiState.value.createdTimestamp
            ),
            key = userId
        )
        if(settingUiState.value.image != null)
            updateProfilePicture(settingUiState.value.image!!)
        updateUser(user, activity)
    }
}

data class SettingsState(
    val username: String = "",
    val phoneNumber: String = "",
    var image: Uri? = null,
    var isDialog: Boolean = false,
    var isError: Boolean = false,
    var createdTimestamp: Timestamp? = null
    )