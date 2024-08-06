package com.example.mobileapplicationassignment.frontEndUi.userProfile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobileapplicationassignment.R
import com.example.mobileapplicationassignment.ui.theme.MobileApplicationAssignmentTheme

@Composable
fun CreateImageProfile(
    image: Painter,
    modifier: Modifier = Modifier
){
    Surface (
        modifier = Modifier
            .size(154.dp)
            .padding(5.dp),
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

@Preview(showSystemUi = true)
@Composable
fun PrevCreateImageProfile(){
    MobileApplicationAssignmentTheme {
        CreateImageProfile(painterResource(id = R.drawable.facebook_icon))
    }
}