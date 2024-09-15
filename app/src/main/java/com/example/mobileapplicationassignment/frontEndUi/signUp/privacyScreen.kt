package com.example.mobileapplicationassignment.frontEndUi.signUp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.stringResource
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobileapplicationassignment.R
import com.example.mobileapplicationassignment.ui.theme.MobileApplicationAssignmentTheme

@Composable
fun PrivacyScreen(navController: NavController){
    Box(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.TopCenter
    ){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ){
            Text(text = stringResource(id = R.string.privacyScreenTitle),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            Text(
                text = "We value your privacy. Here’s a brief overview of how we handle your data:\n\n" +
                        "1. Introduction: We respect your privacy and are committed to protecting it.\n\n" +
                        "2. Data Collection: We collect information you provide, such as name and email.\n\n" +
                        "3. Usage: We use your data to provide and improve our services.\n\n" +
                        "4. Sharing: We do not share your personal information with third parties.\n\n" +
                        "5. Security: We implement measures to protect your data.\n\n" +
                        "For questions, contact us at support@example.com.",
                style = MaterialTheme.typography.bodyLarge
            )

            Button(onClick = { navController.popBackStack() }, //Here navigate back to registerScreen.kt
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)) {
                Text(text = stringResource(id = R.string.finish))
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewPrivacyScreen() {
    MobileApplicationAssignmentTheme {
        PrivacyScreen(navController = rememberNavController())
    }
}