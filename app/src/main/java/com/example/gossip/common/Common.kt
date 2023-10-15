package com.example.gossip.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.gossip.ui.theme.PurpleGrey40
import com.example.gossip.ui.theme.PurpleGrey80

@Composable
fun CommonDialog() {
    Dialog(onDismissRequest = { /*TODO*/ }) {
        CircularProgressIndicator()
    }
}
@Composable
fun OTPTextFields(
    modifier: Modifier = Modifier,
    length: Int,
    onFilled: (code: String) -> Unit
) {
    var otpValue by remember {
        mutableStateOf("")
    }
    BasicTextField(
        modifier = modifier,
        value = otpValue,
        onValueChange = {
            if (it.length <= 6)
                otpValue = it;
            if (it.length == 6) {
                onFilled(otpValue)
//                Log.d("otp", otpValue)
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Next
        ),
        decorationBox = {
            Row(horizontalArrangement = Arrangement.Center) {
                repeat(length) { index ->
                    val char = when {
                        index >= otpValue.length -> ""
                        else -> otpValue[index].toString()
                    }
                    val isFocused = otpValue.length == index
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
                        color = PurpleGrey40,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    )
}
