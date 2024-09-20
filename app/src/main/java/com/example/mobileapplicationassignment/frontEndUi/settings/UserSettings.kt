package com.example.mobileapplicationassignment.frontEndUi.settings

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSettings(currentNickname: String, currentEmail: String, navController: NavController){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid ?: ""
    val database = FirebaseDatabase.getInstance("https://scanplus-befd8-default-rtdb.asia-southeast1.firebasedatabase.app").reference.child("user").child(userId)

    var username by remember { mutableStateOf(currentNickname) }
    var currentPass by remember { mutableStateOf("") }
    var newPass1 by remember { mutableStateOf("") }
    var newPass2 by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Surface (
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(scrollState)
    ){
        Column (
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxWidth()
        ){
            TopAppBar(
                title = { Text(text = "User Settings") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White)
                    }
                }
            )
            Spacer(modifier = Modifier.padding(8.dp))

            ProfileCard(
                title = "Change Username",
                textFieldValue = username,
                onTextFieldChange = { username = it },
                leadingIcon = Icons.Default.Person
            )
            Spacer(modifier = Modifier.padding(8.dp))

            ProfileCard(
                title = "Email Address",
                textFieldValue = currentEmail,
                onTextFieldChange = {},
                leadingIcon = Icons.Default.Email,
                isEditable = false // Make the email field non-editable
            )
            Spacer(modifier = Modifier.padding(8.dp))

            PasswordCard(
                currentPass = currentPass,
                newPass1 = newPass1,
                newPass2 = newPass2,
                onCurrentPass = { currentPass = it},
                onNewPass1 = { newPass1 = it },
                onNewPass2 = { newPass2 = it }
            )
            Spacer(modifier = Modifier.padding(10.dp))
            
            Button(
                onClick = {
                    scope.launch {
                        if(newPass1 != newPass2){
                            Toast.makeText(context, "Password and Confirm Password does not match.", Toast.LENGTH_SHORT).show()
                        } else {
                            // Update username
                            database.child("username").setValue(username)
                            Toast.makeText(context, "Profile Updated!", Toast.LENGTH_SHORT).show()

                            // Update password
                            if (newPass1.isNotEmpty() && newPass1 == newPass2) {
                                auth.currentUser?.updatePassword(newPass1)?.addOnCompleteListener { passwordTask ->
                                    if (passwordTask.isSuccessful) {
                                        Toast.makeText(context, "Password updated successfully!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        scope.launch {
                                            Toast.makeText(context, "Fail to update password. Your original password is incorrect!", Toast.LENGTH_SHORT).show()
                                            kotlinx.coroutines.delay(2000)
                                        }
                                    }
                                }
                            }


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
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun ProfileCard(title: String, textFieldValue: String, onTextFieldChange: (String) -> Unit, leadingIcon: ImageVector, isEditable: Boolean = true){
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
                ),
                enabled = isEditable
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
        label = { Text(text = label) },
        visualTransformation = PasswordVisualTransformation(),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            cursorColor = MaterialTheme.colorScheme.secondary
        )
    )
}

