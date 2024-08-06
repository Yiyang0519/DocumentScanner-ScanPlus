package com.example.mobileapplicationassignment.frontEndUi.userProfile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import com.example.mobileapplicationassignment.ui.theme.MobileApplicationAssignmentTheme

data class CategoryItem(
    val title:String,
    val iconResId: Int,
    val description: String
)

@Composable
fun ProfileCategory(){
    val categories = listOf(
        CategoryItem("Edit Profile", R.drawable.edit_profile, stringResource(id =R.string.EditProfile)),
        CategoryItem("Settings", R.drawable.settings, stringResource(id = R.string.Settings)),
        CategoryItem("FAQs", R.drawable.faq, stringResource(id = R.string.FAQs)),
        CategoryItem("Logout", R.drawable.logout, stringResource(id = R.string.Logout))
    )
    Box (
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ){
        Surface (
            modifier = Modifier
                .padding(3.dp)
                .fillMaxWidth()
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.background),
            shape = RoundedCornerShape(corner = CornerSize(6.dp)),
            border = BorderStroke(
                width = 2.dp,
                color = Color.LightGray
            )
        ){
            AllCategories(categories)
        }
    }
}

@Composable
fun AllCategories(categories: List<CategoryItem>){
    LazyColumn {
        items(categories) { category ->
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                        .background(MaterialTheme.colorScheme.background)
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background),
                    ) {
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Image(
                                painter = painterResource(id = category.iconResId),
                                contentDescription = category.title,
                                modifier = Modifier.size(50.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .align(alignment = Alignment.CenterVertically)
                            ) {
                                Text(text = category.title, fontWeight = FontWeight.Bold)
                                Text(text = category.description)
                            }
                        }
                    }
                }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PrevProfileCategory (){
    MobileApplicationAssignmentTheme {
        ProfileCategory()
    }
}