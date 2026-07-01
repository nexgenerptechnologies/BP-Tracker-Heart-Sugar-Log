package com.example.bptracker.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.bptracker.AppContainer
import com.example.bptracker.data.VitalRecord
import com.example.bptracker.theme.HighOrange
import com.example.bptracker.theme.NormalGreen
import com.example.bptracker.theme.PrimaryBlue
import com.example.bptracker.theme.SurfaceDark
import com.example.bptracker.theme.TextSecondary
import com.example.bptracker.ui.components.CircularGauge
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeasureHeartRateScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
        }
    )

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    var currentBpm by remember { mutableIntStateOf(0) }
    var isMeasuring by remember { mutableStateOf(false) }
    var measurementProgress by remember { mutableFloatStateOf(0f) }

    // Pulsing Animation for the Heart icon inside the gauge
    val infiniteTransition = rememberInfiniteTransition()
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isMeasuring) 1.15f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Measure", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Help */ }) {
                        Icon(Icons.Default.HelpOutline, contentDescription = "Help", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1E2235))
            )
        },
        bottomBar = {
            if (currentBpm > 0 && !isMeasuring) {
                Box(modifier = Modifier.fillMaxWidth().background(Color(0xFF1E2235)).navigationBarsPadding().padding(16.dp)) {
                    Button(
                        onClick = {
                            val record = VitalRecord(
                                timestamp = System.currentTimeMillis(),
                                heartRate = currentBpm,
                                heartRateState = "Resting"
                            )
                            coroutineScope.launch {
                                AppContainer.repository.addRecord(record)
                                Toast.makeText(context, "Heart Rate Saved!", Toast.LENGTH_SHORT).show()
                                onBack()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = HighOrange),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Save Measurement", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                }
            } else if (!isMeasuring) {
                Box(modifier = Modifier.fillMaxWidth().background(Color(0xFF1E2235)).navigationBarsPadding().padding(16.dp)) {
                    Button(
                        onClick = {
                            isMeasuring = true
                            measurementProgress = 0f
                            currentBpm = 0
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Start Measurement", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1E2235))
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Big Circular Gauge
            CircularGauge(
                progress = measurementProgress,
                color = HighOrange,
                modifier = Modifier.size(260.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = null,
                        tint = HighOrange,
                        modifier = Modifier.size(100.dp * pulseScale)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = if (currentBpm > 0) "$currentBpm" else "0",
                            color = Color.White,
                            style = MaterialTheme.typography.displayLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "BPM",
                            color = TextSecondary,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(64.dp))

            // Camera Window and Status Text
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Small circular camera preview
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(SurfaceDark)
                ) {
                    if (hasCameraPermission && isMeasuring) {
                        AndroidView(
                            factory = { ctx ->
                                val previewView = PreviewView(ctx).apply {
                                    scaleType = PreviewView.ScaleType.FILL_CENTER
                                }

                                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                                val executor = ContextCompat.getMainExecutor(ctx)
                                val analysisExecutor = Executors.newSingleThreadExecutor()

                                cameraProviderFuture.addListener({
                                    val cameraProvider = cameraProviderFuture.get()
                                    val preview = Preview.Builder().build().also {
                                        it.setSurfaceProvider(previewView.surfaceProvider)
                                    }

                                    val imageAnalysis = ImageAnalysis.Builder()
                                        .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                        .build()
                                        .also {
                                            it.setAnalyzer(analysisExecutor, HeartRateAnalyzer { bpm ->
                                                if (isMeasuring && bpm > 40) {
                                                    currentBpm = bpm
                                                    measurementProgress += 0.005f
                                                    if (measurementProgress >= 1f) {
                                                        isMeasuring = false
                                                    }
                                                }
                                            })
                                        }

                                    try {
                                        cameraProvider.unbindAll()
                                        val camera = cameraProvider.bindToLifecycle(
                                            lifecycleOwner,
                                            CameraSelector.DEFAULT_BACK_CAMERA,
                                            preview,
                                            imageAnalysis
                                        )
                                        if (camera.cameraInfo.hasFlashUnit()) {
                                            camera.cameraControl.enableTorch(true)
                                        }
                                    } catch (exc: Exception) {
                                        Log.e("CameraPPG", "Use case binding failed", exc)
                                    }
                                }, executor)

                                previewView
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(24.dp))
                
                Column {
                    if (isMeasuring) {
                        Text("Measuring. (${(measurementProgress * 100).roundToInt()}%)", color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("Place your finger on camera", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                    } else if (currentBpm > 0) {
                        Text("Measurement Complete", color = NormalGreen, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("Save your reading below", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                    } else {
                        Text("Ready to Measure", color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("Tap Start below", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
