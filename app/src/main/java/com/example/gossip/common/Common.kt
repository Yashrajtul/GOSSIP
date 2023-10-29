package com.example.gossip.common

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gossip.R
import com.example.gossip.ui.theme.PurpleGrey40
import com.example.gossip.ui.theme.PurpleGrey80

@Composable
fun CommonDialog() {
    Dialog(onDismissRequest = { /*TODO*/ }) {
        CircularProgressIndicator()
    }
}

@Preview(showBackground = true)
@Composable
fun CommonDialogPreview() {
    CommonDialog()
}


@Composable
fun OTPTextFields(
    otp: String,
    isError: Boolean,
    modifier: Modifier = Modifier,
    length: Int,
    getOtp: (otp: String) -> Unit
) {
    BasicTextField(
        modifier = modifier.padding(8.dp),
        value = otp,
        onValueChange = {
            getOtp(it)
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Next
        ),
        decorationBox = {
            Row(
                horizontalArrangement = Arrangement.Center,
            ) {
                repeat(length) { index ->
                    val char = when {
                        index >= otp.length -> ""
                        else -> otp[index].toString()
                    }
                    val isFocused = otp.length == index
                    Text(
                        modifier = Modifier
                            .width(40.dp)
                            .border(
                                if (isFocused) 1.5.dp
                                else 1.dp,
                                if (isFocused) PurpleGrey40
                                else PurpleGrey80,
                                RoundedCornerShape(8.dp)
                            )
                            .padding(2.dp),
                        text = char,
                        style = MaterialTheme.typography.headlineMedium,
                        color = if(isError) Color.Red else PurpleGrey40,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun OTPTextFields() {
    OTPTextFields(otp = "123", isError = true, length = 6, getOtp = {})
}

@Composable
fun ImagePickerScreen() {
    val context = LocalContext.current
    var imageUri: Any? by remember { mutableStateOf(R.drawable.baseline_person_24) }
    var selectedImageUris by remember {
        mutableStateOf<List<Uri>>(emptyList())
    }
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) {
        if (it != null) {
            Log.d("PhotoPicker", "Selected URI: $it")
            imageUri = it
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }
    val multiplePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 2)
    ) {
        if (it != null) {
            Log.d("PhotoPicker", "Selected URI: $it")
            selectedImageUris = it
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card (
            shape = CircleShape
        ){
            AsyncImage(
                modifier = Modifier
                    .size(250.dp)
                    .clickable {
                        photoPicker.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                model = ImageRequest.Builder(LocalContext.current).data(imageUri)
                    .crossfade(enable = true).build(),
                contentDescription = "Avatar Image",
                contentScale = ContentScale.Crop,
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        LazyRow {
            items(selectedImageUris) { uri ->
                AsyncImage(
                    modifier = Modifier.size(250.dp),
                    model = ImageRequest.Builder(LocalContext.current).data(uri)
                        .crossfade(enable = true).build(),
                    contentDescription = "Avatar Image",
                    contentScale = ContentScale.Crop,
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row {
            Button(onClick = {
                Toast.makeText(
                    context,
                    ActivityResultContracts.PickVisualMedia.isPhotoPickerAvailable().toString(),
                    Toast.LENGTH_LONG
                ).show()
            }) {
                Text(text = "Availability")
            }

            Spacer(modifier = Modifier.width(24.dp))
            Button(onClick = {
                multiplePhotoPicker.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }) {
                Text(text = "Pick multiple photo")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImagePickerScreenPreview() {
    ImagePickerScreen()
}
