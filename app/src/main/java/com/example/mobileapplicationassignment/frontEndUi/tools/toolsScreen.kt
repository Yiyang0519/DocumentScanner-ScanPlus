package com.example.mobileapplicationassignment.frontEndUi.tools

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobileapplicationassignment.R
import com.example.mobileapplicationassignment.frontEndUi.components.HeaderText
import com.example.mobileapplicationassignment.frontEndUi.login.defaultPadding
import com.example.mobileapplicationassignment.ui.theme.MobileApplicationAssignmentTheme

data class ToolItems(
    val title: String,
    val iconResId: Int,
    val description: String

)

@Composable
fun ToolsScreen(){
    val toolItems = listOf(
        ToolItems("Text Recognition", R.drawable.text_recognition, stringResource(id = R.string.text_recognition)),
        ToolItems("PDF Converter", R.drawable.pdf_converter, stringResource(id = R.string.PDF_converter)),
        ToolItems("Image & File Import", R.drawable.add_files, stringResource(id = R.string.Add_files)),
    )
    Surface (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        color = MaterialTheme.colorScheme.background
    ){
        Column {
            HeaderText(
                text = "Tools",
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(alignment = Alignment.Start)
            )
            LazyColumn {
                items(toolItems) { tool ->
                    ToolItemView(tool)
                }
            }
        }

    }
}

@Composable
fun ToolItemView(tool:ToolItems){
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp)
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(painter = painterResource(id = tool.iconResId),
                contentDescription = tool.title,
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 16.dp)
            )
            Column {
                Text(text = tool.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text(text = tool.description, color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PrevToolsScreen(){
    MobileApplicationAssignmentTheme {
        ToolsScreen()
    }
}