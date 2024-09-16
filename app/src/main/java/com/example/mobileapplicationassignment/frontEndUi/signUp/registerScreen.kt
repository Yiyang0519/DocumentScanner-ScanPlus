@file:Suppress("DEPRECATION")

package com.example.mobileapplicationassignment.frontEndUi.signUp

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobileapplicationassignment.AuthState
import com.example.mobileapplicationassignment.AuthViewModel
import com.example.mobileapplicationassignment.R
import com.example.mobileapplicationassignment.frontEndUi.components.HeaderText
import com.example.mobileapplicationassignment.frontEndUi.components.loginTxtField
import com.example.mobileapplicationassignment.ui.theme.MobileApplicationAssignmentTheme
import com.google.firebase.database.FirebaseDatabase

val defaultPadding = 16.dp
val itemSpacing = 8.dp

data class User(
    val username: String = "",
    val email: String = ""
)

@Composable
fun RegisterScreen(navController: NavController, authViewModel: AuthViewModel){


    val (userName,onUserNameChange) = rememberSaveable {
        mutableStateOf("")
    }

    val (email,onEmailChange) = rememberSaveable {
        mutableStateOf("")
    }

    val (pass1,onPass1Change) = rememberSaveable {
        mutableStateOf("")
    }

    val (pass2,onPass2Change) = rememberSaveable {
        mutableStateOf("")
    }

    val (agree,onAgreeChange) = rememberSaveable {
        mutableStateOf(false)
    }

    var isPasswordSame by remember {
        mutableStateOf(false)
    }

    val isFieldsNotEmpty = userName.isNotEmpty() && email.isNotEmpty() && pass1.isNotEmpty() && pass2.isNotEmpty() && agree

    val context = LocalContext.current

    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect (authState.value){
        when(authState.value){
            is AuthState.Authenticated -> navController.navigate("home")
            is AuthState.Error -> Toast.makeText(context,(authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(defaultPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        AnimatedVisibility(isPasswordSame) {
            Text(
                text = stringResource(id = R.string.passwordErrorMsg),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.height(defaultPadding)
            )
        }
        HeaderText(
            text = "Sign Up",
            modifier = Modifier
                .padding(vertical = defaultPadding)
                .align(alignment = Alignment.Start)
        )

        Spacer(Modifier.height(itemSpacing))
        loginTxtField(
            value = userName,
            onValueChange = onUserNameChange,
            labelText = "User Name",
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Text
        )

        Spacer(Modifier.height(itemSpacing))
        loginTxtField(
            value = email,
            onValueChange = onEmailChange,
            labelText = "Email",
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Email
        )

        Spacer(Modifier.height(itemSpacing))
        loginTxtField(
            value = pass1,
            onValueChange = onPass1Change,
            labelText = "Password",
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(itemSpacing))
        loginTxtField(
            value = pass2,
            onValueChange = onPass2Change,
            labelText = "Confirm Password",
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(defaultPadding))
        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            val privacyText = "Privacy"
            val policyText = "Policy"
            val annotatedString = buildAnnotatedString {
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.onBackground)){
                    append("I Agree with")
                }
                append(" ")
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)){
                    pushStringAnnotation(tag = privacyText,privacyText)
                    append(privacyText)
                }
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.onBackground)){
                    append(" And ")
                }
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)){
                    pushStringAnnotation(tag = policyText,policyText)
                    append(policyText)
                }
            }
            Checkbox(checked = agree, onCheckedChange = onAgreeChange)
            ClickableText(text = annotatedString) {offset ->
                annotatedString.getStringAnnotations(offset,offset).forEach {
                    when(it.tag){
                        privacyText -> {
                            //Here navigate to privacyScreen.kt
                            navController.navigate("privacy")
                            Toast.makeText(context, "Privacy Clicked", Toast.LENGTH_SHORT).show()
                        }
                        policyText -> {
                            //Here navigate to policyScreen.kt
                            navController.navigate("policy")
                            Toast.makeText(context, "Policy Clicked", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(defaultPadding + 8.dp))

        Button(onClick = {
            isPasswordSame = pass1 == pass2
            if(isPasswordSame){
                /*authViewModel.signup(email,pass1)*/
                authViewModel.signup(email, pass1, {userId ->
                    val user = User(username = userName, email = email)
                    FirebaseDatabase.getInstance("https://scanplus-befd8-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("user").child(userId).setValue(user)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                println("User data successfully written to the realtime database.")
                            } else {
                                println("Error writing user data: ${task.exception?.message}")
                            }
                        }
                }, { error ->
                    println("Sign Up Error: $error")
                })
            }else{
                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        },
            modifier = Modifier.fillMaxWidth(),
            enabled = isFieldsNotEmpty
        ) {
            Text(text = "Sign Up")
        }

        Spacer(Modifier.height(itemSpacing))
        val signInText = "Sign In"
        val signInAnnotation = buildAnnotatedString {
            withStyle(SpanStyle(color = MaterialTheme.colorScheme.onBackground)){
                append("Already have an account?")
            }
            append(" ")
            withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)){
                pushStringAnnotation(signInText,signInText)
                append(signInText)
            }
        }
        ClickableText(text = signInAnnotation) {offset ->
            signInAnnotation.getStringAnnotations(offset,offset).forEach {
                if(it.tag == signInText){
                    //Here navigate back to loginScreen.kt
                    navController.popBackStack()
                    Toast.makeText(context, "Sign In clicked", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}








@Preview(showSystemUi = true)
@Composable
fun PrevRegisterScreen(){
    MobileApplicationAssignmentTheme {
        RegisterScreen(navController = rememberNavController(), authViewModel = AuthViewModel())
        }
}