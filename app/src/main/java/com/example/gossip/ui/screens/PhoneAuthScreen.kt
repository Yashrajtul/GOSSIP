package com.example.gossip.ui.screens

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gossip.R
import com.example.gossip.common.CommonDialog
import com.example.gossip.common.OTPTextFields
import com.example.gossip.ui.theme.Purple80
import com.example.gossip.utils.ResultState
import com.example.gossip.utils.showMsg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneAuthScreen(
    activity: Activity,
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var mobile by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var isDialog by remember { mutableStateOf(false) }

    if(isDialog)
        CommonDialog()

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ){
        Column (
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "OTP Screen",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            Image(
                painter = painterResource(id = R.drawable.baseline_phone_android_24),
                contentDescription = "otp image",
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
            )
            Spacer(modifier = Modifier.height(50.dp))
            OutlinedTextField(
                value = mobile,
                onValueChange = { mobile = it },
                label = { Text(text = "Phone Number") },
                placeholder = { Text(text = "Phone Number") },
                leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = "Phone Number") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done

                ),
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                          scope.launch(Dispatchers.Main){
                              viewModel.createUserWithPhone(
                                  mobile,
                                  activity
                              ).collect{
                                  when(it){
                                      is ResultState.Success->{
                                          isDialog = false
                                          activity.showMsg(it.data)
                                      }
                                      is ResultState.Failure->{
                                          isDialog = false
                                          activity.showMsg(it.msg.toString())
                                      }
                                      ResultState.Loading->{
                                          isDialog = true
                                      }
                                  }
                              }
                          }
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(45.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Purple80)
            ) {
                Text(text = "Send Otp", fontSize = 15.sp, color = Color.White)

            }
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Enter the OTP",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
            OTPTextFields(length = 6) { getOpt ->
                otp = getOpt

            }
            Spacer(modifier = Modifier.height(30.dp))
            Button(
                onClick = {
                    scope.launch(Dispatchers.Main){
                        viewModel.signInWithCredential(
                            otp
                        ).collect{
                            when(it){
                                is ResultState.Success->{
                                    isDialog = false
                                    activity.showMsg(it.data)
                                }
                                is ResultState.Failure->{
                                    isDialog = false
                                    activity.showMsg(it.msg.toString())
                                }
                                ResultState.Loading->{
                                    isDialog = true
                                }
                            }
                        }
                    }
                }, modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(45.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Purple80),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Otp Verify",
                    fontSize = 13.sp,
                    color = Color.White
                )
            }
        }
    }
}



@Preview(showSystemUi = true)
@Composable
fun PhoneAuthScreenPreview() {
    PhoneAuthScreen(
        LocalContext.current as Activity
    )
}