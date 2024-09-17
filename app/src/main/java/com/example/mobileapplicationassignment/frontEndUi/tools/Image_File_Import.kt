package com.example.mobileapplicationassignment.frontEndUi.tools

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults.textFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobileapplicationassignment.R
import com.example.mobileapplicationassignment.frontEndUi.common.ErrorScreen
import com.example.mobileapplicationassignment.frontEndUi.common.LoadingScreen
import com.example.mobileapplicationassignment.frontEndUi.screen.components.PdfLayout
import com.example.mobileapplicationassignment.frontEndUi.viewmodels.PdfViewModel
import com.example.mobileapplicationassignment.utils.showToast
import com.example.mobileapplicationassignment.utils.Resource

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageFileImport(pdfViewModel: PdfViewModel, navController: NavController) {
    val context = LocalContext.current
    val activity = context as Activity

    val pdfState by pdfViewModel.pdfStateFlow.collectAsState()
    val message = pdfViewModel.message

    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        message.collect {
            when (it) {
                is Resource.Success -> {
                    context.showToast(it.data)
                }
                is Resource.Error -> {
                    context.showToast(it.message)
                }
                Resource.Idle -> {}
                Resource.Loading -> {}
            }
        }
    }

    val imageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            uri?.let {
                Log.d("ImageFileImport", "Selected image URI: $uri")
                // Handle the selected image URI (e.g., upload, save)
                // For example, upload to server or save locally
            }
        }
    }

    val fileLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            uri?.let {
                Log.d("ImageFileImport", "Selected file URI: $uri")
                // Handle the selected file URI (e.g., upload, save)
                // For example, upload to server or save locally
            }
        }
    }

    val selectFileLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            uri?.let {
                Log.d("ImageFileImport", "Selected file URI: $uri")
                // Handle the selected file URI (e.g., upload, save)
                // For example, upload to server or save locally
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Display the drawable as an image on the left side of the title
                        Image(
                            painter = painterResource(id = R.drawable.add_files), // Replace with your drawable resource ID
                            contentDescription = "Import file or image",
                            modifier = Modifier.size(40.dp) // Adjust the size as necessary
                        )
                        Text(
                            text = "Import Image/File",
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Show options to select image or file
                    // This could be done using a Dialog or a menu
                    val chooseIntent = Intent(Intent.ACTION_PICK).apply {
                        type = "*/*" // Allow selecting all types
                        putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "*/*")) // Filter for images and all files
                    }
                    selectFileLauncher.launch(chooseIntent)
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier
                    .padding(2.dp)
                    .offset(y = (-80).dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add_button),
                    contentDescription = "Import Image/File",
                    tint = Color.White
                )
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(bottom = 72.dp, start = 16.dp, end = 16.dp)
            ) {
                // Search Bar
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .padding(horizontal = 16.dp) // Adjust padding as needed
                        .padding(top = 8.dp) // Space between search bar and content
                        .fillMaxWidth(),
                    placeholder = {
                        Text(text = stringResource(id = R.string.search_pdf))
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
                            contentDescription = "Search Icon"
                        )
                    },
                    colors = textFieldColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    shape = RoundedCornerShape(12.dp) // Rounded corners
                )

                // Display Result - Only filter the list if the search query is not empty
                pdfState.DisplayResult(
                    onLoading = {
                        // Loading UI
                        LoadingScreen(pdfViewModel = pdfViewModel)
                    },
                    onSuccess = { pdfList ->
                        val filteredList = if (searchQuery.isEmpty()) {
                            pdfList // Show all PDFs by default
                        } else {
                            pdfList.filter { it.name.contains(searchQuery, ignoreCase = true) } // Filtered list based on `name`
                        }

                        if (filteredList.isEmpty()) {
                            ErrorScreen(message = "No PDF found")
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp) // Reduced padding
                                    .padding(top = 8.dp) // Added top padding for spacing
                            ) {
                                items(items = filteredList, key = { pdfEntity -> pdfEntity.id }) { pdfEntity ->
                                    PdfLayout(pdfEntity = pdfEntity, pdfViewModel = pdfViewModel)
                                }

                                // Add a large space at the bottom
                                item {
                                    Spacer(modifier = Modifier.height(100.dp)) // Adjust height as needed
                                }
                            }
                        }
                    },
                    onError = {
                        ErrorScreen(message = it)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                        type = "*/*" // Set MIME type to allow file selection
                        addCategory(Intent.CATEGORY_OPENABLE)
                    }
                    fileLauncher.launch(intent)
                }) {
                    Text(text = "Upload File")
                }
            }
        }
    )
}
