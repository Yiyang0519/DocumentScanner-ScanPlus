@file:Suppress("DEPRECATION")
@file:OptIn(ExperimentalPermissionsApi::class)

package com.example.mobileapplicationassignment.frontEndUi.tools

import android.util.Size
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.mobileapplicationassignment.R
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.PermissionState


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun MainScreen(navController: NavController) {
    // Remember the camera permission state
    val cameraPermissionState: PermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    MainContent(
        hasPermission = cameraPermissionState.hasPermission,
        onRequestPermission = cameraPermissionState::launchPermissionRequest,
        navController = navController
    )
}

@Composable
private fun MainContent(
    hasPermission: Boolean,
    onRequestPermission: () -> Unit,
    navController: NavController
) {
    // If permission is granted, show the TextRecognition camera screen
    if (hasPermission) {
        TextRecognition(navController = navController)
    } else {
        // Show a screen asking for permission
        NoPermissionScreen(onRequestPermission)
    }
}

@Composable
fun NoPermissionScreen(onRequestPermission: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Camera permission is required to use this feature.")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRequestPermission) {
            Text("Grant Permission")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextRecognition(navController: NavController) {
    val context = LocalContext.current
    var isUsingCamera by remember { mutableStateOf(false) }
    var detectedText by remember { mutableStateOf("No text detected yet...") }
    val bitmapState: MutableState<Bitmap?> = remember { mutableStateOf(null) }
    val recognizedText = remember { mutableStateOf("") }

    // Image picker for selecting from gallery
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            bitmapState.value = bitmap
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Text Recognition") },
                actions = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(painterResource(id = R.drawable.baseline_arrow_back_24), contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isUsingCamera) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Top
                ) {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        CameraPreview(
                            detectedText = detectedText,
                            onTextUpdated = { updatedText -> detectedText = updatedText }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = "Real-time Detected Text: $detectedText",
                            color = Color.Black,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }
                }
            } else if (bitmapState.value != null) {
                Image(
                    bitmap = bitmapState.value!!.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(250.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val image = InputImage.fromBitmap(bitmapState.value!!, 0)
                        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                        recognizer.process(image)
                            .addOnSuccessListener { visionText ->
                                recognizedText.value = visionText.text
                            }
                            .addOnFailureListener { e ->
                                recognizedText.value = "Failed to recognize text: ${e.message}"
                            }
                    }
                ) {
                    Text(text = "Recognize Text")
                }
            } else {
                Button(onClick = { isUsingCamera = true }) {
                    Text("Open Camera for Real-time Recognition")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                    Text("Upload Image")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = recognizedText.value, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun CameraPreview(
    detectedText: String,
    onTextUpdated: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            factory = { context ->
                PreviewView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    setBackgroundColor(android.graphics.Color.BLACK)
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    scaleType = PreviewView.ScaleType.FILL_START
                }.also { previewView ->
                    startTextRecognition(
                        context = context,
                        cameraController = cameraController,
                        lifecycleOwner = lifecycleOwner,
                        previewView = previewView,
                        onDetectedTextUpdated = onTextUpdated
                    )
                }
            }
        )
    }
}

private fun startTextRecognition(
    context: Context,
    cameraController: LifecycleCameraController,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    onDetectedTextUpdated: (String) -> Unit
) {
    cameraController.imageAnalysisTargetSize = CameraController.OutputSize(Size(250, 250))
    cameraController.setImageAnalysisAnalyzer(
        ContextCompat.getMainExecutor(context),
        TextRecognitionAnalyzer(onDetectedTextUpdated)
    )
    cameraController.bindToLifecycle(lifecycleOwner)
    previewView.controller = cameraController
}

class TextRecognitionAnalyzer(
    private val onDetectedTextUpdated: (String) -> Unit
) : ImageAnalysis.Analyzer {

    companion object {
        const val THROTTLE_TIMEOUT_MS = 500L
    }

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    @androidx.camera.core.ExperimentalGetImage
    override fun analyze(imageProxy: ImageProxy) {
        scope.launch {
            val mediaImage = imageProxy.image ?: run {
                imageProxy.close()
                return@launch
            }
            val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            suspendCoroutine { continuation ->
                textRecognizer.process(inputImage)
                    .addOnSuccessListener { visionText ->
                        val detectedText = visionText.text
                        if (detectedText.isNotBlank()) {
                            onDetectedTextUpdated(detectedText)
                        }
                    }
                    .addOnCompleteListener {
                        continuation.resume(Unit)
                    }
            }

            delay(THROTTLE_TIMEOUT_MS)
        }.invokeOnCompletion { exception ->
            exception?.printStackTrace()
            imageProxy.close()
        }
    }
}