package com.example.mobileapplicationassignment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mobileapplicationassignment.frontEndUi.screen.HomeScreen
import com.example.mobileapplicationassignment.frontEndUi.settings.GeneralSettings
import com.example.mobileapplicationassignment.frontEndUi.settings.UserSettings
import com.example.mobileapplicationassignment.frontEndUi.signUp.PolicyScreen
import com.example.mobileapplicationassignment.frontEndUi.signUp.PrivacyScreen
import com.example.mobileapplicationassignment.frontEndUi.tools.ImageFileImport
import com.example.mobileapplicationassignment.frontEndUi.tools.TextRecognition
import com.example.mobileapplicationassignment.frontEndUi.tools.ToolsScreen
import com.example.mobileapplicationassignment.frontEndUi.userProfile.UserProfile
import com.example.mobileapplicationassignment.frontEndUi.viewmodels.PdfViewModel
import com.example.mobileapplicationassignment.ui.theme.MobileApplicationAssignmentTheme


data class BottomNavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)

class MainActivity : ComponentActivity() {
    private val pdfViewModel by viewModels<PdfViewModel>{
        viewModelFactory {
            addInitializer(PdfViewModel::class){
                PdfViewModel(application)
            }
        }
    }
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen=installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            splashScreen.setKeepOnScreenCondition{pdfViewModel.isSplashScreen}
            MobileApplicationAssignmentTheme (pdfViewModel.isDarkMode,false){
                val items = listOf(
                    BottomNavItem(
                        title = "Scan",
                        selectedIcon = Icons.Filled.Home,
                        unselectedIcon = Icons.Outlined.Home,
                        hasNews = false,
                    ),
                    BottomNavItem(
                        title = "Lists",
                        selectedIcon = Icons.AutoMirrored.Filled.List,
                        unselectedIcon = Icons.AutoMirrored.Outlined.List,
                        hasNews = false,
                        badgeCount = 10
                    ),
                    BottomNavItem(
                        title = "Tools",
                        selectedIcon = Icons.Filled.Build,
                        unselectedIcon = Icons.Outlined.Build,
                        hasNews = false,
                    ),
                    BottomNavItem(
                        title = "Account",
                        selectedIcon = Icons.Filled.Person,
                        unselectedIcon = Icons.Outlined.Person,
                        hasNews = false,
                    )
                )

                var selectedItemIndex by rememberSaveable {
                    mutableIntStateOf(0)
                }

                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    Scaffold (
                        bottomBar = {
                            NavigationBar {
                                items.forEachIndexed { index, item ->
                                    NavigationBarItem(
                                        selected = selectedItemIndex == index,
                                        onClick = {
                                            selectedItemIndex =index
                                        },
                                        label = {
                                            Text(text = item.title)
                                        },
                                        icon = { 
                                            BadgedBox(
                                                badge = {
                                                    if(item.badgeCount != null){
                                                        Badge {
                                                            Text(text = item.badgeCount.toString())
                                                        }
                                                    }else if(item.hasNews){
                                                        Badge()
                                                    }
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = if(index == selectedItemIndex){
                                                        item.selectedIcon
                                                    }else item.unselectedIcon,
                                                    contentDescription = item.title
                                                )
                                            }
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                                            unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                                            indicatorColor = MaterialTheme.colorScheme.primary
                                        )
                                    )
                                }
                            }
                        }
                    ){
                        //ImageFileImport()
                        //UserSettings(currentNickname = "Andrew Yeo", currentEmail = "junken03@gmail.com")
                        HomeScreen(pdfViewModel)
                    }


                }
            }
        }
    }
}


