package com.example.mobileapplicationassignment.frontEndUi.userProfile


import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobileapplicationassignment.AuthViewModel
import com.example.mobileapplicationassignment.R
import com.example.mobileapplicationassignment.frontEndUi.settings.FAQScreen
import com.example.mobileapplicationassignment.frontEndUi.settings.GeneralSettings
import com.example.mobileapplicationassignment.frontEndUi.settings.UserSettings
import com.example.mobileapplicationassignment.ui.theme.MobileApplicationAssignmentTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

val defaultPadding = 20.dp

@Composable
fun UserProfile(authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background)
    ) {
        NavHost(
            navController = navController,
            startDestination = "profile_screen"
        ) {
            composable("profile_screen") {
                ProfileScreen(authViewModel, navController)
            }
            composable("edit_profile") {
                //UserSettings(currentNickname = , currentEmail = )
            }
            composable("settings") {
                GeneralSettings(navControllerOld = navController)
            }
            composable("faqs") {
                FAQScreen(navController = navController)
            }
        }
    }
}

@Composable
fun ProfileScreen(authViewModel: AuthViewModel, navController: NavController){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(defaultPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(defaultPadding))
                    CreateImageProfile(painterResource(id = R.drawable.profile_pic), Modifier)
                    HorizontalDivider()
                    CreateProfileInfo()
                }
            }
            Spacer(modifier = Modifier.height(defaultPadding))
            ProfileCategory(navController, authViewModel)
        }
    }
}

@Composable
fun ProfileCategory(navController: NavController, authViewModel: AuthViewModel){
    Surface(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        shape = RoundedCornerShape(corner = CornerSize(6.dp)),
        border = BorderStroke(2.dp, Color.LightGray)
    ) {
        Column {
            // Profile button that requires authViewModel, such as Edit Profile
            ProfileButtonWithAuth(
                authViewModel = authViewModel,
                iconRes = R.drawable.edit_profile,
                title = "Edit Profile",
                route = "edit_profile",
                navController = navController
            )
            // Normal Profile buttons
            ProfileButton(navController, R.drawable.settings, "Settings", R.string.Settings, "settings")
            ProfileButton(navController, R.drawable.faq, "FAQs", R.string.FAQs, "faqs")
            // Logout button with authViewModel
            LogoutButton(authViewModel)
        }
    }
}

//Edit Profile
@Composable
fun ProfileButtonWithAuth(
    authViewModel: AuthViewModel,
    iconRes: Int,
    title: String,
    route: String,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .background(MaterialTheme.colorScheme.background)
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {
                // You can handle additional authViewModel logic here if needed
                navController.navigate(route)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = title,
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = title, fontWeight = FontWeight.Bold)
                    Text(text = stringResource(id = R.string.EditProfile))
                }
            }
        }
    }
}

//Settings & FAQs
@Composable
fun ProfileButton(navController: NavController, iconRes: Int, title: String, descriptionResId:Int, route: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .background(MaterialTheme.colorScheme.background)
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { navController.navigate(route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = title,
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = title, fontWeight = FontWeight.Bold)
                    Text(text = stringResource(id = descriptionResId))
                }
            }
        }
    }
}

@Composable
fun LogoutButton(authViewModel: AuthViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .background(MaterialTheme.colorScheme.background)
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { authViewModel.signout() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logout),
                    contentDescription = "Logout",
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = "Logout", fontWeight = FontWeight.Bold)
                    Text(text = stringResource(id = R.string.Logout))
                }
            }
        }
    }
}

@Composable
fun CreateImageProfile(
    image: Painter,
    modifier: Modifier = Modifier
) {
    Surface (
        modifier = modifier
            .size(154.dp)
            .padding(10.dp),
        shape = CircleShape,
        border = BorderStroke(0.5.dp, Color.LightGray),
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    ){
        Image(painter = image,
            contentDescription = "User profile img",
            modifier = Modifier.size(135.dp),
            contentScale = ContentScale.Crop)
    }
}

@Composable
fun CreateProfileInfo() {
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    // State to hold the username and email
    var username by remember { mutableStateOf("Loading...") }
    var email by remember { mutableStateOf("Loading...") }

    // Use LaunchedEffect to fetch data from Firebase
    LaunchedEffect(userId) {
        if (userId != null) {
            val database = FirebaseDatabase.getInstance("https://scanplus-befd8-default-rtdb.asia-southeast1.firebasedatabase.app").reference.child("user").child(userId)
            database.get().addOnSuccessListener { snapshot ->
                username = snapshot.child("username").getValue(String::class.java) ?: "Unknown User"
                email = snapshot.child("email").getValue(String::class.java) ?: "Unknown Email"
            }.addOnFailureListener { exception ->
                // Handle error
                Log.e("Firebase", "Error fetching data", exception)
            }
        }
    }

    // Now update the UI using the state variables
    UpdateProfileInfo(username, email)
}
@Composable
fun UpdateProfileInfo(username:String, email:String){

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        ProfileText(
            text = username
        )

        ProfileText(
            text = email
            )
        }
}

@Composable
fun ProfileText(
    text: String,
    modifier: Modifier = Modifier,
    color : Color = Color.DarkGray,
    fontSize: TextUnit = 18.sp
){
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        fontSize = fontSize,
        color = color,
        modifier = modifier
    )
}

@Preview(showSystemUi = true)
@Composable
fun PrevUserProfile(){
    MobileApplicationAssignmentTheme {
        UserProfile(authViewModel = AuthViewModel())
    }
}