package com.example.mobileapplicationassignment.frontEndUi.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobileapplicationassignment.ui.theme.MobileApplicationAssignmentTheme

val defaultPadding = 16.dp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralSettings (){
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(text = "Settings", fontSize = 20.sp, color = MaterialTheme.colorScheme.onPrimary) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ){
        Column (
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        ){
            SettingsCategory("Theme", onClick = {})
            Spacer(modifier = Modifier.height(defaultPadding))
            SettingsCategory("Notification", onClick = {})
            Spacer(modifier = Modifier.height(defaultPadding))
            SettingsCategory("Import File & Converter", onClick = {})
            Spacer(modifier = Modifier.height(defaultPadding))
            SettingsCategory("Help", onClick = {})
            Spacer(modifier = Modifier.height(defaultPadding))
        }
    }
}

@Composable
fun SettingsCategory(title: String, onClick: () -> Unit) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background)
    ){
        Row (
            modifier = Modifier.padding(defaultPadding),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = title, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PrevGeneralSettings(){
    MobileApplicationAssignmentTheme {
        GeneralSettings()
    }
}