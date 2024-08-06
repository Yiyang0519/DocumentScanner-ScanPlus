package com.example.mobileapplicationassignment.frontEndUi.userProfile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobileapplicationassignment.ui.theme.MobileApplicationAssignmentTheme

@Composable
fun CreateProfileInfo(){
    Column (
        modifier = Modifier.fillMaxWidth().padding(5.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        ProfileText(
            text = "Andrew Yeo"
            )
        
        ProfileText(
            text = "junken03@gmail.com"
        )

        ProfileText(
            text = "@andrewyeoooo",
            fontSize = 15.sp,
            color = Color.DarkGray
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PrevCreateProfileInfo(){
    MobileApplicationAssignmentTheme {
        CreateProfileInfo()
    }
}