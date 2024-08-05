package com.example.mobileapplicationassignment.frontEndUi.login

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobileapplicationassignment.R
import com.example.mobileapplicationassignment.frontEndUi.components.HeaderText
import com.example.mobileapplicationassignment.frontEndUi.components.loginTxtField
import com.example.mobileapplicationassignment.ui.theme.MobileApplicationAssignmentTheme


val defaultPadding = 16.dp
val itemSpacing = 8.dp

@Composable
fun LoginScreen(){
    val (username,setUserName) = rememberSaveable {
        mutableStateOf("")
    }

    val (password,setPassword) = rememberSaveable {
        mutableStateOf("")
    }

    val (checked,onCheckChange) = rememberSaveable {
        mutableStateOf(false)
    }

    val isFieldsNotEmpty = username.isNotEmpty() && password.isNotEmpty()

    val context = LocalContext.current

    Column (
        modifier = Modifier.fillMaxSize()
            .padding(defaultPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderText(
            text = "Login",
            modifier = Modifier
                .padding(vertical = defaultPadding)
                .align(alignment = Alignment.Start)
        )
        loginTxtField(
            value = username,
            onValueChange = setUserName,
            labelText = "Username",
            leadingIcon = Icons.Default.Person,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(itemSpacing))

        loginTxtField(
            value = password,
            onValueChange = setPassword,
            labelText = "Password",
            leadingIcon = Icons.Default.Lock,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(itemSpacing))

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = checked, onCheckedChange = onCheckChange)
                Text(text = "Remember me")
            }
            Row {
                TextButton(onClick = { }) {
                    Text(text = "Forgot Password?")
                }
            }
        }

        Spacer(Modifier.height(itemSpacing))
        
        Button(
            onClick = {  },
            modifier = Modifier.fillMaxWidth(),
            enabled = isFieldsNotEmpty
        ) {
            Text(text = "Login")
        }

        AlternativeLoginOptions(
            onIconClick = {index ->
                when(index){
                    0 -> {
                        Toast.makeText(context, "Facebook Login Click", Toast.LENGTH_SHORT).show()
                    }
                    1 -> {
                        Toast.makeText(context, "Google Login Click", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            onSignUpClick = {  },
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(align = Alignment.BottomCenter)
        )


    }
}

@Composable
fun AlternativeLoginOptions(
    onIconClick:(index:Int) -> Unit,
    onSignUpClick:() -> Unit,
    modifier: Modifier = Modifier
){
    val iconList = listOf(
        R.drawable.facebook_icon,
        R.drawable.google_icon
    )
    Column (
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = "Or Sign in With")
        Row (
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ){
            iconList.forEachIndexed { index, iconResId ->
                Icon(
                    painter = painterResource(iconResId),
                    contentDescription = "Alternative login",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            onIconClick(index)
                        }
                        .clip(CircleShape)
                )
                Spacer(Modifier.width(defaultPadding))
            }
        }
        Spacer(Modifier.height(itemSpacing))
        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Don't have an Account?")
            Spacer(Modifier.height(itemSpacing))
            TextButton(onClick = onSignUpClick) {
                Text(text = "Sign Up")
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PrevLoginScreen(){
    MobileApplicationAssignmentTheme {
        LoginScreen()
    }
}
