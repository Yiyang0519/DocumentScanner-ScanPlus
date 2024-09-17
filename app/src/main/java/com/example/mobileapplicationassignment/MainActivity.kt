package com.example.mobileapplicationassignment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobileapplicationassignment.frontEndUi.login.LoginScreen
import com.example.mobileapplicationassignment.frontEndUi.pdfLists.PdfListsScreen
import com.example.mobileapplicationassignment.frontEndUi.screen.HomeScreen
import com.example.mobileapplicationassignment.frontEndUi.signUp.PolicyScreen
import com.example.mobileapplicationassignment.frontEndUi.signUp.PrivacyScreen
import com.example.mobileapplicationassignment.frontEndUi.signUp.RegisterScreen
import com.example.mobileapplicationassignment.frontEndUi.tools.PdfConverter
import com.example.mobileapplicationassignment.frontEndUi.tools.TextRecognition
import com.example.mobileapplicationassignment.frontEndUi.tools.ToolsScreen
import com.example.mobileapplicationassignment.frontEndUi.userProfile.UserProfile
import com.example.mobileapplicationassignment.frontEndUi.viewmodels.PdfViewModel
import com.example.mobileapplicationassignment.ui.theme.MobileApplicationAssignmentTheme
import com.google.firebase.FirebaseApp

data class BottomNavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)


class MainActivity : ComponentActivity() {
    private val pdfViewModel by viewModels<PdfViewModel> {
        viewModelFactory {
            addInitializer(PdfViewModel::class) {
                PdfViewModel(application)
            }
        }
    }


    private val authViewModel by viewModels<AuthViewModel>()

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContent {
            splashScreen.setKeepOnScreenCondition { pdfViewModel.isSplashScreen }

            val pdfCount by pdfViewModel.getPdfCount().collectAsState(initial = 0)
            val authState by authViewModel.authState.observeAsState()

            MobileApplicationAssignmentTheme(pdfViewModel.isDarkMode, false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (authState) {
                        is AuthState.Loading -> {
                            LoadingScreen1()
                        }
                        is AuthState.Authenticated -> {
                            AuthenticatedScreen(
                                pdfCount = pdfCount,
                                authViewModel = authViewModel,
                                pdfViewModel = pdfViewModel // Pass pdfViewModel here
                            )
                        }
                        is AuthState.Unauthenticated, is AuthState.Error -> {
                            InitialNavigation(authViewModel)
                        }
                        else -> {
                            Text(text = "Unknown State")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingScreen1(){
    Text(text = "Loading ...")
}

@RequiresApi(Build.VERSION_CODES.Q)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AuthenticatedScreen(
    pdfCount: Int,
    authViewModel: AuthViewModel,
    pdfViewModel: PdfViewModel // Add this parameter
) {
    val navController = rememberNavController()
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
            badgeCount = pdfCount
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

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            when (index) {
                                0 -> navController.navigate("home")
                                1 -> navController.navigate("lists")
                                2 -> navController.navigate("tools")
                                3 -> navController.navigate("profile")
                            }
                        },
                        label = {
                            Text(text = item.title)
                        },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (item.badgeCount != null) {
                                        Badge {
                                            Text(text = item.badgeCount.toString())
                                        }
                                    } else if (item.hasNews) {
                                        Badge()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = item.title,
                                )
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onBackground,
                            unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                            indicatorColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }
    )
    {
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                HomeScreen(pdfViewModel, navController, authViewModel) // Pass pdfViewModel here
            }
            composable("lists") {
                PdfListsScreen(pdfViewModel) // Pass pdfViewModel here
            }
            composable("tools") { ToolsScreen(navController) }
            composable("profile") { UserProfile(authViewModel) }
            composable("text_recognition") { TextRecognition(navController) }
            composable("pdf_converter") { PdfConverter(navController) }
            composable("image_file_import") { /* ImageFileImport() */ }
            }
        }
}

@Composable
fun InitialNavigation(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController, authViewModel) }
        composable("register") { RegisterScreen(navController, authViewModel) }
        composable("privacy") { PrivacyScreen(navController) }
        composable("policy") { PolicyScreen(navController)}
    }
}