package com.example.mobileapplicationassignment.frontEndUi.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun loginTxtField (
    modifier: Modifier = Modifier,
    value:String,
    onValueChange:(String) -> Unit,
    labelText: String,
    leadingIcon: ImageVector? =null,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None

){
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = labelText)},
        leadingIcon = {if (leadingIcon != null)  Icon(imageVector = leadingIcon, null)},
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = visualTransformation,
        shape = RoundedCornerShape(30)
    )

}

@Preview(showBackground = true)
@Composable
fun PrevTextField(){
    loginTxtField(value = "", onValueChange = {}, labelText = "Enter your email")
}