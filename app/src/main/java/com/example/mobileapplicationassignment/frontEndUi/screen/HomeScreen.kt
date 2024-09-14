package com.example.mobileapplicationassignment.frontEndUi.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.mobileapplicationassignment.R
import com.example.mobileapplicationassignment.frontEndUi.common.ErrorScreen
import com.example.mobileapplicationassignment.frontEndUi.common.LoadingScreen
import com.example.mobileapplicationassignment.frontEndUi.screen.components.PdfLayout
import com.example.mobileapplicationassignment.frontEndUi.screen.components.RenameDeleteDialog
import com.example.mobileapplicationassignment.utils.copyPdfFileToAppDirectory
import com.example.mobileapplicationassignment.utils.showToast
import com.example.mobileapplicationassignment.frontEndUi.viewmodels.PdfViewModel
import com.example.mobileapplicationassignment.models.PdfEntity
import com.example.mobileapplicationassignment.utils.Resource
import com.example.mobileapplicationassignment.utils.getFileSize
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(pdfViewModel: PdfViewModel) {
    LoadingScreen(pdfViewModel = pdfViewModel)
    RenameDeleteDialog(pdfViewModel = pdfViewModel)

    val activity = LocalContext.current as Activity
    val context = LocalContext.current

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

    val scannerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val scanningResult = GmsDocumentScanningResult.fromActivityResultIntent(result.data)

                scanningResult?.pdf?.let { pdf ->
                    Log.d("pdfName", pdf.uri.lastPathSegment.toString())

                    val date = Date()
                    val fileName = SimpleDateFormat(
                        "dd-MM-yyyy HH:mm:ss",
                        Locale.getDefault()
                    ).format(date) + ".pdf"

                    copyPdfFileToAppDirectory(
                        context,
                        pdf.uri, fileName
                    )

                    val pdfEntity = PdfEntity(
                        UUID.randomUUID().toString(),
                        fileName,
                        getFileSize(context, fileName),
                        date
                    )

                    pdfViewModel.insertPdf(pdfEntity)
                }
            }
        }

    val scanner = remember {
        GmsDocumentScanning.getClient(
            GmsDocumentScannerOptions.Builder()
                .setGalleryImportAllowed(true)
                .setResultFormats(GmsDocumentScannerOptions.RESULT_FORMAT_PDF)
                .setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_FULL).build()
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                actions = {
                    Switch(
                        checked = pdfViewModel.isDarkMode,
                        onCheckedChange = { pdfViewModel.isDarkMode = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.background,
                            uncheckedThumbColor = MaterialTheme.colorScheme.background,
                            checkedTrackColor = MaterialTheme.colorScheme.primary,
                            uncheckedTrackColor = MaterialTheme.colorScheme.onBackground
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    scanner.getStartScanIntent(activity).addOnSuccessListener {
                        scannerLauncher.launch(
                            IntentSenderRequest.Builder(it).build()
                        )
                    }.addOnFailureListener {
                        it.printStackTrace()
                        context.showToast(it.message.toString())
                    }
                },
                text = {
                    Text(text = stringResource(id = R.string.scan))
                },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.camera),
                        contentDescription = "camera"
                    )
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .padding(2.dp)
                    .offset(y = (-80).dp)
            )
        }
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
        ) {
            // Search Bar
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

            // Function Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FunctionButton(
                    imageResId = R.drawable.scan_icon,
                    text = "Extract Text",
                    onClick = {
                        // TODO: Add navigation to TextRecognition() screen here
                    }
                )

                FunctionButton(
                    imageResId = R.drawable.pdf_converter,
                    text = "PDF Tools",
                    onClick = {
                        // TODO: Add navigation to PDFTools() screen here
                    }
                )

                FunctionButton(
                    imageResId = R.drawable.photo,
                    text = "Import Images",
                    onClick = {
                        // TODO: Add navigation to ImportImages() screen here
                    }
                )

                FunctionButton(
                    imageResId = R.drawable.import_folder,
                    text = "Import Files",
                    onClick = {
                        // TODO: Add navigation to ImportFiles() screen here
                    }
                )
            }

            // Recents Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically // Align items vertically in the center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_drop_down_24),
                    contentDescription = "Dropdown",
                    tint = MaterialTheme.colorScheme.onBackground, // Adjust color if needed
                    modifier = Modifier.size(24.dp) // Adjust size as needed
                )

                Spacer(modifier = Modifier.width(8.dp)) // Space between icon and text

                Text(
                    text = "Recents",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }


            // Display Result - PDF Section
            pdfState.DisplayResult(
                onLoading = {
                    // Loading UI
                },
                onSuccess = { pdfList ->

                    val filteredList = if (searchQuery.isEmpty()) {
                        pdfList
                    } else {
                        pdfList.filter { it.name.contains(searchQuery, ignoreCase = true) }
                    }

                    // Limit to a maximum of 5 items
                    val limitedList = filteredList.take(5)

                    if (limitedList.isEmpty()) {
                        ErrorScreen(message = "No PDF found")
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                                .padding(top = 8.dp)
                        ) {
                            items(
                                items = limitedList,
                                key = { pdfEntity -> pdfEntity.id }
                            ) { pdfEntity ->
                                PdfLayout(pdfEntity = pdfEntity, pdfViewModel = pdfViewModel)
                            }

                            // Add the vector asset icon at the bottom center before the last padding
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_more_horiz_24),
                                        contentDescription = "More options",
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
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
        }
    }
}


@Composable
fun FunctionButton(imageResId: Int, text: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Round Button with Icon and Custom Background + Border
        Button(
            onClick = onClick,
            modifier = Modifier
                .size(64.dp), // Size of the round button
            shape = CircleShape, // Round shape
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color(0xFFF7FDF6) // Custom background color
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary), // Border color and width
            contentPadding = PaddingValues(0.dp) // No inner padding
        ) {
            Icon(
                painter = painterResource(id = imageResId),
                contentDescription = text,
                modifier = Modifier.size(40.dp), // Icon size
                tint = Color.Unspecified // Disable tint to check image visibility
            )
        }
        // Text below the button
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp), // Space between button and text
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}






