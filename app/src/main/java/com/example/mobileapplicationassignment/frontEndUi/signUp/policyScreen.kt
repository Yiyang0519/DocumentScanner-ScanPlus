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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobileapplicationassignment.R
import com.example.mobileapplicationassignment.ui.theme.MobileApplicationAssignmentTheme

@Composable
fun PolicyScreen(){
    Box(
        Modifier
            .fillMaxSize()
            .padding(25.dp)
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
            Text(text = stringResource(id = R.string.policyScreenTitle),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "This is the privacy policy for the app. Here we describe how we handle your data. " +
                        "We are committed to protecting your privacy and ensuring that your personal information " +
                        "is handled in a safe and responsible manner.\n\n" +
                        "1. Introduction\n" +
                        "2. Data We Collect\n" +
                        "3. How We Use Your Data\n" +
                        "4. Data Sharing and Disclosure\n" +
                        "5. Your Rights\n" +
                        "6. Changes to This Policy\n\n" +
                        "For any questions regarding this policy, please contact us at support@scanplus.com.",
                style = MaterialTheme.typography.bodyLarge
            )

            Button(onClick = {  },
                modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text(text = stringResource(id = R.string.finish))
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewPolicyScreen() {
    MobileApplicationAssignmentTheme {
        PolicyScreen()
    }
}