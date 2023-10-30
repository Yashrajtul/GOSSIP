package com.example.gossip.ui.phonelogin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gossip.R
import com.example.gossip.firebaseauth.common.CommonDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(
    phoneNumber: String,
    isDialog: Boolean,
    isError: Boolean,
    getPhoneNumber: (phoneNumber: String) -> Unit,
    sendOtp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    if(isDialog)
        CommonDialog()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.baseline_phone_android_24),
            contentDescription = "Phone Icon",
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = {
                getPhoneNumber(it)
            },
            label = { Text("Phone Number") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Phone Icon",
                    tint = Color.Gray
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    sendOtp()
                    focusManager.clearFocus()
                }
            ),
            maxLines = 1,
            isError = isError,
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
                sendOtp()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors()
        ) {
            Text(
                text = "Login",
                fontSize = 18.sp,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    Login(
        phoneNumber = "9914",
        isDialog = false,
        isError = true,
        getPhoneNumber = {},
        sendOtp = {},
    )
}
@Preview(showBackground = true)
@Composable
fun LoginPreview1() {
    Login(
        phoneNumber = "",
        isDialog = false,
        isError = false,
        getPhoneNumber = {},
        sendOtp = {},
    )
}
@Preview(showBackground = true)
@Composable
fun LoginPreview2() {
    Login(
        phoneNumber = "",
        isDialog = true,
        isError = false,
        getPhoneNumber = {},
        sendOtp = {},
    )
}
