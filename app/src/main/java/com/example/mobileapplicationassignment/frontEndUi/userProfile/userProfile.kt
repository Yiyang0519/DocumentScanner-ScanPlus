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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.mobileapplicationassignment.R
import com.example.mobileapplicationassignment.ui.theme.MobileApplicationAssignmentTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

val defaultPadding = 20.dp

data class DataUserProfile(
    val name: String = "",
    val email: String = "",
    val userId: String = "",
    val profileImgUrl: String = ""
)

data class CategoryItem(
    val title:String,
    val iconResId: Int,
    val description: String
)


@Composable
fun FetchUserProfile(userId: String = "1001") {
    var userProfile by remember { mutableStateOf(DataUserProfile()) }
    var imagePainter by remember { mutableStateOf<Painter?>(null) }

    // Retrieve User Profile Data from Firebase Realtime Database
    val databaseReference = FirebaseDatabase.getInstance().getReference("User/$userId")

    // Using LaunchedEffect to fetch data once
    LaunchedEffect(Unit) {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val email = dataSnapshot.child("email").getValue(String::class.java) ?: ""
                val username = dataSnapshot.child("username").getValue(String::class.java) ?: ""
                val imageUrl = dataSnapshot.child("imageUrl").getValue(String::class.java) ?: ""

                userProfile = DataUserProfile(name = username, email = email, profileImgUrl = imageUrl)

                // Load image from Firebase Storage
                val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)
                storageReference.downloadUrl.addOnSuccessListener { uri ->
                    //imagePainter = rememberImagePainter(data = uri) //Here have an error, there have a red line under "rememberAsyncImagePainter
                }.addOnFailureListener { exception ->
                    Log.e("Firebase Storage", "Failed to load image", exception)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Firebase Database", "Failed to load user data", databaseError.toException())
            }
        })
    }

    UserProfileScreen(userProfile = userProfile, imagePainter = imagePainter)
}

@Composable
fun UserProfileScreen(userProfile: DataUserProfile, imagePainter: Painter?){
    Surface (modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .background(MaterialTheme.colorScheme.background)
    ){
        Card (
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ){
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize()
                    .height(defaultPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary),

                ) {
                    Column (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        Spacer(modifier = Modifier.height(defaultPadding))
                        CreateImageProfile(image = imagePainter)
                        HorizontalDivider()
                        CreateProfileInfo(userProfile)
                    }
                }
                ProfileCategory()
            }
        }
    }
}

//Sub-function
@Composable
fun CreateImageProfile(
    image: Painter?,
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
        if (image != null) {
            Image(
                painter = image,
                contentDescription = "User profile image",
                modifier = Modifier.size(135.dp),
                contentScale = ContentScale.Crop
            )
        } else {
            // Display placeholder image if imagePainter is null
            Image(
                painter = painterResource(id = R.drawable.account), // Replace with your placeholder image
                contentDescription = "Default profile image",
                modifier = Modifier.size(135.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun CreateProfileInfo(userProfile: DataUserProfile){
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        ProfileText(
            text = userProfile.name
        )

        ProfileText(
            text = userProfile.email
        )

        ProfileText(
            text = userProfile.userId,
            fontSize = 15.sp,
            color = Color.DarkGray
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

@Composable
fun ProfileCategory(){
    val categories = listOf(
        CategoryItem("Edit Profile", R.drawable.edit_profile, stringResource(id =R.string.EditProfile)),
        CategoryItem("Settings", R.drawable.settings, stringResource(id = R.string.Settings)),
        CategoryItem("FAQs", R.drawable.faq, stringResource(id = R.string.FAQs)),
        CategoryItem("Logout", R.drawable.logout, stringResource(id = R.string.Logout))
    )
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
            AllCategories(categories)
        }
    }
}

@Composable
fun AllCategories(categories: List<CategoryItem>){
    LazyColumn {
        items(categories) { category ->
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(5.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background),
                ) {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Image(
                            painter = painterResource(id = category.iconResId),
                            contentDescription = category.title,
                            modifier = Modifier.size(50.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .align(alignment = Alignment.CenterVertically)
                        ) {
                            Text(text = category.title, fontWeight = FontWeight.Bold)
                            Text(text = category.description)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserProfile() {
    FetchUserProfile(userId = "1001")
}

@Preview(showSystemUi = true)
@Composable
fun PrevUserProfile(){
    MobileApplicationAssignmentTheme {
        UserProfile()
    }
}