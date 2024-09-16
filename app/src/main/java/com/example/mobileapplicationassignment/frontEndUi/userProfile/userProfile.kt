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
import com.example.mobileapplicationassignment.AuthViewModel
import com.example.mobileapplicationassignment.R
import com.example.mobileapplicationassignment.ui.theme.MobileApplicationAssignmentTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

val defaultPadding = 20.dp

@Composable
fun UserProfile(authViewModel: AuthViewModel) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background)
    ) {
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
                    .fillMaxWidth()
                    .fillMaxSize()
                    .height(defaultPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary),

                    ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.height(defaultPadding))
                        CreateImageProfile(
                            painterResource(id = R.drawable.google_icon),
                            modifier = Modifier
                        )
                        HorizontalDivider()
                        CreateProfileInfo()
                    }
                }
                ProfileCategory(authViewModel)
            }
        }
    }
}

@Composable
fun ProfileCategory(authViewModel: AuthViewModel){
    Box (
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ){
        Surface (
            modifier = Modifier
                .padding(3.dp)
                .fillMaxWidth()
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.background),
            shape = RoundedCornerShape(corner = CornerSize(6.dp)),
            border = BorderStroke(
                width = 2.dp,
                color = Color.LightGray
            )
        ){
            AllCategories(authViewModel)
        }
    }
}

@Composable
fun AllCategories(authViewModel: AuthViewModel) {
    Column {
        //Edit Profile
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .background(MaterialTheme.colorScheme.background)
                .padding(5.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {  },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.edit_profile),
                        contentDescription = "Edit Profile",
                        modifier = Modifier.size(50.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .align(alignment = Alignment.CenterVertically)
                    ) {
                        Text(text = "Edit Profile", fontWeight = FontWeight.Bold)
                        Text(text = stringResource(id = R.string.EditProfile))
                    }
                }
            }
        }

        //Settings
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .background(MaterialTheme.colorScheme.background)
                .padding(5.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {  },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.settings),
                        contentDescription = "Settings",
                        modifier = Modifier.size(50.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .align(alignment = Alignment.CenterVertically)
                    ) {
                        Text(text = "Settings", fontWeight = FontWeight.Bold)
                        Text(text = stringResource(id = R.string.Settings))
                    }
                }
            }
        }

        //FAQs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .background(MaterialTheme.colorScheme.background)
                .padding(5.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {  },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.faq),
                        contentDescription = "FAQs",
                        modifier = Modifier.size(50.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .align(alignment = Alignment.CenterVertically)
                    ) {
                        Text(text = "FAQs", fontWeight = FontWeight.Bold)
                        Text(text = stringResource(id = R.string.FAQs))
                    }
                }
            }
        }

        //Logout
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .background(MaterialTheme.colorScheme.background)
                .padding(5.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { authViewModel.signout() },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logout),
                        contentDescription = "Logout",
                        modifier = Modifier.size(50.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .align(alignment = Alignment.CenterVertically)
                    ) {
                        Text(text = "Logout", fontWeight = FontWeight.Bold)
                        Text(text = stringResource(id = R.string.Logout))
                    }
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