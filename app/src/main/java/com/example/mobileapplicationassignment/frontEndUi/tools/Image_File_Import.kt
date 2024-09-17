package com.example.mobileapplicationassignment.frontEndUi.tools

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.mobileapplicationassignment.R
import com.example.mobileapplicationassignment.frontEndUi.common.ErrorScreen
import com.example.mobileapplicationassignment.frontEndUi.common.LoadingScreen
import com.example.mobileapplicationassignment.frontEndUi.screen.components.PdfLayout
import com.example.mobileapplicationassignment.frontEndUi.viewmodels.PdfViewModel
import com.example.mobileapplicationassignment.models.PdfEntity
import com.example.mobileapplicationassignment.utils.showToast
import com.example.mobileapplicationassignment.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

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
                is Resource.Success -> context.showToast(it.data)
                is Resource.Error -> context.showToast(it.message)
                Resource.Idle -> {}
                Resource.Loading -> {}
            }
        }
    }

    val fileLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            uri?.let {
                Log.d("ImageFileImport", "Selected file URI: $uri")
                handleFileSelection(uri, pdfViewModel, context)
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
                        Image(
                            painter = painterResource(id = R.drawable.add_files),
                            contentDescription = "Import file or image",
                            modifier = Modifier.size(40.dp)
                        )
                        Text(
                            text = "Import Image/File",
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
                    val chooseIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
                        type = "*/*"
                        addCategory(Intent.CATEGORY_OPENABLE)
                    }
                    fileLauncher.launch(chooseIntent)
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
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp)
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
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                pdfState.DisplayResult(
                    onLoading = {
                        LoadingScreen(pdfViewModel = pdfViewModel)
                    },
                    onSuccess = { pdfList ->
                        val filteredList = if (searchQuery.isEmpty()) {
                            pdfList
                        } else {
                            pdfList.filter { it.name.contains(searchQuery, ignoreCase = true) }
                        }

                        if (filteredList.isEmpty()) {
                            ErrorScreen(message = "No PDF found")
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp)
                                    .padding(top = 8.dp)
                            ) {
                                items(items = filteredList, key = { pdfEntity -> pdfEntity.id }) { pdfEntity ->
                                    PdfLayout(pdfEntity = pdfEntity, pdfViewModel = pdfViewModel)
                                }

                                item {
                                    Spacer(modifier = Modifier.height(100.dp))
                                }
                            }
                        }
                    },
                    onError = {
                        ErrorScreen(message = it)
                    }
                )
            }
        }
    )
}

private fun handleFileSelection(
    uri: Uri,
    pdfViewModel: PdfViewModel,
    context: android.content.Context
) {
    val contentResolver = context.contentResolver
    val cursor = contentResolver.query(uri, null, null, null, null)
    val nameColumn = cursor?.getColumnIndexOrThrow(android.provider.OpenableColumns.DISPLAY_NAME)
    val sizeColumn = cursor?.getColumnIndexOrThrow(android.provider.OpenableColumns.SIZE)
    cursor?.moveToFirst()

    val fileName = cursor?.getString(nameColumn ?: -1) ?: "Unknown"
    val fileSize = cursor?.getLong(sizeColumn ?: -1) ?: 0L
    cursor?.close()

    // Get the last modified date
    val lastModifiedTime = getLastModifiedDate(context, uri)

    // Create a PdfEntity
    val pdfEntity = PdfEntity(
        id = UUID.randomUUID().toString(),
        name = fileName,
        size = formatFileSize(fileSize),
        lastModifiedTime = lastModifiedTime
    )

    // Use viewModelScope to launch the coroutine
    pdfViewModel.viewModelScope.launch(Dispatchers.IO) {
        try {
            pdfViewModel.insertPdf(pdfEntity)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


private fun getLastModifiedDate(context: android.content.Context, uri: Uri): Date {
    // Implement logic to get last modified date from the URI
    // This may involve querying metadata or using file operations
    return Date()
}

private fun formatFileSize(size: Long): String {
    return if (size < 1024) {
        "$size B"
    } else if (size < 1048576) {
        "${size / 1024} KB"
    } else {
        "${size / 1048576} MB"
    }
}
