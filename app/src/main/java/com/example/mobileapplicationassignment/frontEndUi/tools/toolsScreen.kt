package com.example.mobileapplicationassignment.frontEndUi.tools

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.mobileapplicationassignment.R
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
        ToolItems("Text Recognition", R.drawable.add_files, stringResource(id = R.string.Add_files)),
    )
    Surface (

    ){

    }
}

@Composable
fun ToolItemView(){

}

@Preview(showSystemUi = true)
@Composable
fun PrevToolsScreen(){
    MobileApplicationAssignmentTheme {
        ToolsScreen()
    }
}