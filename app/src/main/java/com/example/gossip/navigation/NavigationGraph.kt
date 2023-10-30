package com.example.gossip.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.gossip.ui.phonelogin.DetailsLogin
import com.example.gossip.ui.phonelogin.Login
import com.example.gossip.ui.phonelogin.LoginViewModel
import com.example.gossip.ui.phonelogin.OtpScreen

@Composable
fun NavigationGraph(activity: Activity) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "auth") {
        composable("splashscreen") {
//            SplashScreen {
//
//            }
        }
        navigation(
            startDestination = "login",
            route = "auth"
        ) {
            composable("login") {
                val viewModel = it.sharedViewModel<LoginViewModel>(navController)
                val loginState = viewModel.loginUiState.collectAsStateWithLifecycle()
                LaunchedEffect(key1 = loginState.value.otpSent){
                    if(loginState.value.otpSent)
                        navController.navigate("otp")
                }
                Login(
                    phoneNumber = loginState.value.phoneNumber,
                    isError = loginState.value.isError,
                    isDialog = loginState.value.isDialog,
                    getPhoneNumber = viewModel::getPhoneNumber,
                    sendOtp = { viewModel.sendOtp(activity) }
                )
            }
            composable("otp") {
                val viewModel = it.sharedViewModel<LoginViewModel>(navController)
                val loginState = viewModel.loginUiState.collectAsStateWithLifecycle()
                LaunchedEffect(key1 = loginState.value.isOtpVerified){
                    if(loginState.value.isOtpVerified)
                        navController.navigate("userinfo")
                }
                OtpScreen(
                    otp = loginState.value.otp,
                    timer = loginState.value.timer,
                    isError = loginState.value.isError,
                    isDialog = loginState.value.isDialog,
                    isButtonEnabled = loginState.value.isButtonEnabled,
                    getOtp = { otp ->
                        viewModel.getOtp(otp)
                        if(otp.length == 6){
                            viewModel.verifyOtp(activity)
                        }
                    },
                    updateTimer = viewModel::updateTimer,
                    resendOtp = { viewModel.resendOtp(activity) }
                )
            }
            composable("userinfo") {
                val viewModel = it.sharedViewModel<LoginViewModel>(navController)
                val loginState = viewModel.loginUiState.collectAsStateWithLifecycle()
                DetailsLogin(
                    username = loginState.value.username,
                    image = loginState.value.image,
                    isDialog = loginState.value.isDialog,
                    isError = loginState.value.isError,
                    getUserName = viewModel::getUserName,
                    getImage = viewModel::getImage,
                    updateProfile = { viewModel.updateProfile(activity) }
                )
            }
        }
        navigation(
            startDestination = "chats",
            route = "home"
        ) {
            composable("chats") {

            }
            composable("settings") {

            }
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavHostController
): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}