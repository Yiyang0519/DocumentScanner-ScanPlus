package com.example.mobileapplicationassignment.frontEndUi.userProfile

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.mobileapplicationassignment.ui.theme.MobileApplicationAssignmentTheme

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
fun PrevProfileText(){
    MobileApplicationAssignmentTheme {
        ProfileText(text = "hello world")
    }
}