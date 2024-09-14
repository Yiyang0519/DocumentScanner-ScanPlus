package com.example.mobileapplicationassignment.frontEndUi.pdfLists

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.sp
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
fun PdfListsScreen(pdfViewModel: PdfViewModel) {
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
        topBar = @Composable {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp) // Adjust spacing as needed
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_drop_down_24),
                            contentDescription = "Dropdown",
                            tint = MaterialTheme.colorScheme.onBackground // Adjust color if needed
                        )
                        Text(
                            text = "All Files",
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp) // Adjust fontSize as needed
                        )
                    }
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
    )
    { paddingValue ->
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
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(12.dp) // Rounded corners
            )


            // Display Result - Only filter the list if the search query is not empty
            pdfState.DisplayResult(onLoading = {
                // Loading UI
            }, onSuccess = { pdfList ->
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
                        items(items = filteredList, key = { pdfEntity ->
                            pdfEntity.id
                        }) { pdfEntity ->
                            PdfLayout(pdfEntity = pdfEntity, pdfViewModel = pdfViewModel)
                        }
                    }
                }
            }, onError = {
                ErrorScreen(message = it)
            })

        }
    }
}

