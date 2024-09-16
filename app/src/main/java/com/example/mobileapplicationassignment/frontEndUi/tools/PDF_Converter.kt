package com.example.mobileapplicationassignment.frontEndUi.tools

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mobileapplicationassignment.R
import com.example.mobileapplicationassignment.ui.theme.MobileApplicationAssignmentTheme
import java.io.IOException
import java.io.OutputStream

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun PdfConverter(navController: NavController) {
    val context = LocalContext.current
    var selectedImageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var pdfFilePath by remember { mutableStateOf<String?>(null) }
    var permissionGranted by remember { mutableStateOf(false) }

    // Image picker launchers
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> uri?.let { selectedImageUris = listOf(it) } }
    )

    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris -> selectedImageUris = uris }
    )

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionGranted = isGranted
        if (isGranted && pdfFilePath != null) {
            Toast.makeText(context, "PDF saved at $pdfFilePath", Toast.LENGTH_LONG).show()
            showNotification(context, "PDF Saved", "PDF has been saved successfully.")
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Display the drawable as an image on the left side of the title
                        Image (
                            painter = painterResource(id = R.drawable.pdf_converter), // Replace with your drawable resource ID
                            contentDescription = "PDF Converter",
                            modifier = Modifier.size(40.dp) // Adjust the size as necessary
                        )
                        Text(
                            text = "PDF Converter",
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                actions = {
                    // Set the back button icon to white
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_back_24), // Replace with your drawable resource ID
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary // Set the tint to white or any desired color
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .padding(bottom = 100.dp) // Adjust this value to move the button up or down
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                FloatingActionButton(
                    onClick = {
                        if (selectedImageUris.isNotEmpty()) {
                            saveImagesAsPdf(context, selectedImageUris) { path ->
                                pdfFilePath = path
                                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            }
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primary, // Match the background color theme
                    contentColor = MaterialTheme.colorScheme.background, // Match the content color theme
                    elevation = FloatingActionButtonDefaults.elevation(8.dp) // Optional: add elevation for shadow
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_save_24), // Replace with your drawable resource ID
                        contentDescription = "Save as PDF",
                        tint = MaterialTheme.colorScheme.background // Match the icon color theme
                    )
                }
            }
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                    ) {
                        Button(onClick = {
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }) {
                            Text(text = "Pick one photo", fontSize = 16.sp)
                        }
                        Button(onClick = {
                            multiplePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }) {
                            Text(text = "Pick multiple photos", fontSize = 16.sp)
                        }
                    }
                }

                items(selectedImageUris) { uri ->
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    )
}


@RequiresApi(Build.VERSION_CODES.Q)
private fun saveImagesAsPdf(
    context: Context,
    imageUris: List<Uri>,
    onPdfSaved: (String) -> Unit
) {
    val pdfDocument = PdfDocument()

    imageUris.forEachIndexed { index, uri ->
        try {
            val bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri))
            val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, index + 1).create()
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas
            canvas.drawBitmap(bitmap, 0f, 0f, null)
            pdfDocument.finishPage(page)
        } catch (e: IOException) {
            Log.e("PDF_CREATION", "Error while creating PDF: ${e.message}")
        }
    }

    // Save PDF file to the MediaStore
    val resolver = context.contentResolver
    val pdfFileName = "images.pdf"
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, pdfFileName)
        put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
        put(MediaStore.MediaColumns.RELATIVE_PATH, "Documents/PDFs/")
    }

    val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
    uri?.let {
        try {
            val outputStream: OutputStream? = resolver.openOutputStream(it)
            outputStream?.use { stream ->
                pdfDocument.writeTo(stream)
            }
            pdfDocument.close()
            onPdfSaved(uri.toString())  // Notify that PDF is saved
            showNotification(context, "PDF Saved", "PDF has been saved successfully.")
            Toast.makeText(context, "PDF saved successfully", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            Log.e("PDF_CREATION", "Error while saving PDF: ${e.message}")
            Toast.makeText(context, "Failed to save PDF", Toast.LENGTH_LONG).show()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun showNotification(context: Context, title: String, message: String) {
    val channelId = "pdf_save_channel"
    val notificationId = 1

    // Create NotificationChannel for Android 8.0 (Oreo) and above
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, "PDF Save Notifications", importance).apply {
            description = "Notifications for PDF saving"
        }
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    // Create Notification
    val notificationBuilder = Notification.Builder(context, channelId)
        .setSmallIcon(android.R.drawable.ic_menu_save)
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(Notification.PRIORITY_DEFAULT)

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(notificationId, notificationBuilder.build())
}
