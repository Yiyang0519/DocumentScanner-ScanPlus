package com.example.mobileapplicationassignment

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobileapplicationassignment.frontEndUi.login.LoginScreen
import com.example.mobileapplicationassignment.frontEndUi.screen.HomeScreen
import com.example.mobileapplicationassignment.frontEndUi.signUp.RegisterScreen

@Composable
fun MyAppNavigation(modifier: Modifier,authViewModel: AuthViewModel){
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = "login", builder = {
        composable("login") {
            LoginScreen(navController = navController, authViewModel = authViewModel)
        }
        
        composable("register"){
            RegisterScreen(navController = navController, authViewModel = authViewModel)
        }
        

    })
}