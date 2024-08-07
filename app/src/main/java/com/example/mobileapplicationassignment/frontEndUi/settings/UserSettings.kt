package com.example.mobileapplicationassignment.frontEndUi.settings

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobileapplicationassignment.frontEndUi.components.HeaderText
import com.example.mobileapplicationassignment.ui.theme.MobileApplicationAssignmentTheme
import kotlinx.coroutines.launch

@Composable
fun UserSettings(currentNickname: String, currentEmail: String){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val (nickname, setNickName) = remember {
        mutableStateOf(currentNickname)
    }

    val (email, setEmail) = remember {
        mutableStateOf(currentEmail)
    }

    val (currentPass, setCurrentPass) = remember {
        mutableStateOf("")
    }

    val (newPass1, setNewPass1) = remember {
        mutableStateOf("")
    }

    val (newPass2, setNewPass2) = remember {
        mutableStateOf("")
    }


    Surface (
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ){
        Column (
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxWidth()
        ){
            HeaderText(
                text = "Edit Profile",
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .align(alignment = Alignment.Start)
            )
            Spacer(modifier = Modifier.padding(8.dp))

            ProfileCard(
                title = "Change Nickname",
                textFieldValue = nickname,
                onTextFieldChange = setNickName,
                leadingIcon = Icons.Default.Person
            )
            Spacer(modifier = Modifier.padding(8.dp))

            ProfileCard(
                title = "Change Email",
                textFieldValue = email,
                onTextFieldChange = setEmail,
                leadingIcon = Icons.Default.Email
            )
            Spacer(modifier = Modifier.padding(8.dp))

            PasswordCard(
                currentPass = currentPass,
                newPass1 = newPass1,
                newPass2 = newPass2,
                onCurrentPass = setCurrentPass,
                onNewPass1 = setNewPass1,
                onNewPass2 = setNewPass2
            )
            Spacer(modifier = Modifier.padding(10.dp))
            
            Button(
                onClick = {
                    scope.launch {
                        if(newPass1 != newPass2){
                            Toast.makeText(context, "Password and Confirm Password does not match.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Profile Updated", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ){
                Text(
                    text = "Save Changes",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun ProfileCard(title: String, textFieldValue: String, onTextFieldChange: (String) -> Unit, leadingIcon: ImageVector){
    Card (
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, Color.LightGray),
        elevation = CardDefaults.cardElevation(4.dp)
    ){
        Column (
            modifier = Modifier.padding(16.dp)
        ){
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Spacer(modifier = Modifier.padding(8.dp))
            OutlinedTextField(
                value = textFieldValue,
                onValueChange = onTextFieldChange,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                        )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    cursorColor = MaterialTheme.colorScheme.secondary
                )
                )
        }
    }
}

@Composable
fun PasswordCard(
    currentPass: String,
    newPass1: String,
    newPass2: String,
    onCurrentPass: (String) -> Unit,
    onNewPass1: (String) -> Unit,
    onNewPass2: (String) -> Unit
) {
    Card (
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, color = Color.LightGray),
        elevation = CardDefaults.cardElevation(4.dp)
    ){
        Column (
            modifier = Modifier.padding(16.dp)
        ){
            Text(
                text = "Change Password",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Spacer(modifier = Modifier.padding(8.dp))

            PasswordTextField(
                label = "Current Password",
                value = currentPass,
                onValueChange = onCurrentPass
            )
            Spacer(modifier = Modifier.padding(8.dp))

            PasswordTextField(
                label = "New Password",
                value = newPass1,
                onValueChange = onNewPass1
            )
            Spacer(modifier = Modifier.padding(8.dp))

            PasswordTextField(
                label = "Confirm New Password",
                value = newPass2,
                onValueChange = onNewPass2
            )
            Spacer(modifier = Modifier.padding(8.dp))
        }
    }
}

@Composable
fun PasswordTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label)},
        visualTransformation = PasswordVisualTransformation(),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            cursorColor = MaterialTheme.colorScheme.secondary
        )
    )
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PrevUserSettings (){
    MobileApplicationAssignmentTheme {
        UserSettings(currentNickname = "Andrew Yeo", currentEmail = "junken03@gmail.com")
    }
}