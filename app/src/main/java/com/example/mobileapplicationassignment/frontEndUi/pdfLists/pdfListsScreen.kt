package com.example.mobileapplicationassignment.frontEndUi.pdfLists

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults.textFieldColors
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.mobileapplicationassignment.utils.showToast
import com.example.mobileapplicationassignment.frontEndUi.viewmodels.PdfViewModel
import com.example.mobileapplicationassignment.models.PdfEntity
import com.example.mobileapplicationassignment.utils.Resource
import com.example.mobileapplicationassignment.utils.copyPdfFileToAppDirectory
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
    // Function to parse size from String to Long
    fun parseSize(size: String): Long {
        val regex = "(\\d+)\\s*(KB|MB|GB|bytes)?".toRegex()
        val matchResult = regex.find(size.trim())

        return when {
            matchResult != null -> {
                val value = matchResult.groupValues[1].toLongOrNull() ?: 0L
                val unit = matchResult.groupValues[2].uppercase()

                when (unit) {
                    "KB" -> value * 1024 // Convert KB to bytes
                    "MB" -> value * 1024 * 1024 // Convert MB to bytes
                    "GB" -> value * 1024 * 1024 * 1024 // Convert GB to bytes
                    else -> value // Assume bytes if no unit is specified
                }
            }
            else -> 0L // Default to 0 if no match
        }
    }


    LoadingScreen(pdfViewModel = pdfViewModel)
    RenameDeleteDialog(pdfViewModel = pdfViewModel)

    val activity = LocalContext.current as Activity
    val context = LocalContext.current

    val pdfState by pdfViewModel.pdfStateFlow.collectAsState()
    val message = pdfViewModel.message

    var searchQuery by remember { mutableStateOf("") }
    var filterExpanded by remember { mutableStateOf(false) }
    var orderExpanded by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("None") }
    var selectedOrder by remember { mutableStateOf("Ascending") }

    val filterOptions = listOf("None", "Date & Time", "Alphabet", "Size")
    val orderOptions = listOf("Ascending", "Descending")

    var originalPdfList by remember { mutableStateOf<List<PdfEntity>>(emptyList()) }

    // Scanner launcher (omitted for brevity)
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_drop_down_24),
                            contentDescription = "Dropdown",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "All Files",
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp)
                        )
                    }
                }
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
                colors = textFieldColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(12.dp)
            )

            // Filter Dropdown
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Filter by: $selectedFilter")
                TextButton(onClick = { filterExpanded = true }) {
                    Text("Select Filter")
                }
                androidx.compose.material3.DropdownMenu(
                    expanded = filterExpanded,
                    onDismissRequest = { filterExpanded = false }
                ) {
                    filterOptions.forEach { filterOption ->
                        androidx.compose.material3.DropdownMenuItem(
                            text = { Text(filterOption) },
                            onClick = {
                                selectedFilter = filterOption
                                filterExpanded = false
                            }
                        )
                    }
                }
            }

            // Order Dropdown (Ascending/Descending)
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Order: $selectedOrder")
                TextButton(onClick = { orderExpanded = true }) {
                    Text("Select Order")
                }
                androidx.compose.material3.DropdownMenu(
                    expanded = orderExpanded,
                    onDismissRequest = { orderExpanded = false }
                ) {
                    orderOptions.forEach { orderOption ->
                        androidx.compose.material3.DropdownMenuItem(
                            text = { Text(orderOption) },
                            onClick = {
                                selectedOrder = orderOption
                                orderExpanded = false
                            }
                        )
                    }
                }
            }

            // Display Result
            pdfState.DisplayResult(
                onLoading = {
                    // Loading UI (if needed)
                },
                onSuccess = { pdfList ->

                    // Store original list
                    if (originalPdfList.isEmpty()) {
                        originalPdfList = pdfList
                    }

                    // Filter and search the list
                    val filteredList = if (searchQuery.isEmpty()) {
                        originalPdfList
                    } else {
                        originalPdfList.filter { it.name.contains(searchQuery, ignoreCase = true) }
                    }

                    // Apply sorting based on selected filter and order
                    val sortedList = when (selectedFilter) {
                        "Date & Time" -> if (selectedOrder == "Ascending") {
                            filteredList.sortedBy { it.lastModifiedTime }
                        } else {
                            filteredList.sortedByDescending { it.lastModifiedTime }
                        }
                        "Alphabet" -> if (selectedOrder == "Ascending") {
                            filteredList.sortedBy { it.name }
                        } else {
                            filteredList.sortedByDescending { it.name }
                        }
                        "Size" -> if (selectedOrder == "Ascending") {
                            filteredList.sortedBy { parseSize(it.size) }
                        } else {
                            filteredList.sortedByDescending { parseSize(it.size) }
                        }
                        else -> filteredList
                    }

                    // Display sorted results
                    if (sortedList.isEmpty()) {
                        ErrorScreen(message = "No PDF found")
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                                .padding(top = 8.dp)
                        ) {
                            items(items = sortedList, key = { pdfEntity -> pdfEntity.id }) { pdfEntity ->
                                PdfLayout(pdfEntity = pdfEntity, pdfViewModel = pdfViewModel)
                            }
                            // Add a large space at the bottom
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
}




