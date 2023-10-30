package com.example.gossip.ui.phonelogin

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gossip.R
import com.example.gossip.firebaseauth.common.CommonDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsLogin(
    username: String,
    image: Uri?,
    isDialog: Boolean,
    isError: Boolean,
    getUserName: (username: String) -> Unit,
    getImage: (image: Uri) -> Unit,
    updateProfile: () -> Unit
) {

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

//    var imageUri: Any? by remember { mutableStateOf(R.drawable.baseline_account_circle_24) }
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) {
        if (it != null) {
            Log.d("PhotoPicker", "Selected URI: $it")
//            imageUri = it
            getImage(it)
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }
    if (isDialog)
        CommonDialog()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = CircleShape
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(200.dp)
                    .clickable {
                        photoPicker.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                model = ImageRequest.Builder(LocalContext.current)
                    .data(image ?: R.drawable.baseline_account_circle_24) // (imageUri)
                    .crossfade(enable = true).build(),
                contentDescription = "Avatar Image",
                contentScale = ContentScale.Crop,
            )
        }
        Text(
            text = "Add profile picture",
            style = MaterialTheme.typography.labelLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = username,
            onValueChange = {
                getUserName(it)
            },
            label = { Text("Username") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = Color.Gray
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            isError = isError,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .focusRequester(focusRequester)
                .onFocusChanged { newFocusState ->
                    if (newFocusState.isFocused) {
//                        phoneSelection()
                    } else {
                    }
                }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                focusManager.clearFocus()
                updateProfile()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors()
        ) {
            Text(
                text = "Let Me In",
                fontSize = 18.sp,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsLoginPreview() {
    DetailsLogin(
        username = "",
        image = null,
        isDialog = false,
        isError = false,
        getUserName = {},
        getImage = {},
        updateProfile = {}
    )
}