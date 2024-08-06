package com.example.mobileapplicationassignment.frontEndUi.userProfile


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobileapplicationassignment.R
import com.example.mobileapplicationassignment.frontEndUi.components.CreateImageProfile
import com.example.mobileapplicationassignment.frontEndUi.components.CreateProfileInfo
import com.example.mobileapplicationassignment.frontEndUi.components.ProfileCategory
import com.example.mobileapplicationassignment.ui.theme.MobileApplicationAssignmentTheme

@Composable
fun UserProfile(){
    Surface (modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .background(MaterialTheme.colorScheme.background)
    ){
        Card (
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(12.dp),
            shape = RoundedCornerShape(corner = CornerSize(15.dp)),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ){
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Column (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        CreateImageProfile(painterResource(id = R.drawable.google_icon), modifier = Modifier)
                        HorizontalDivider()
                        CreateProfileInfo()
                    }
                }
                ProfileCategory()
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PrevUserProfile(){
    MobileApplicationAssignmentTheme {
        UserProfile()
    }
}