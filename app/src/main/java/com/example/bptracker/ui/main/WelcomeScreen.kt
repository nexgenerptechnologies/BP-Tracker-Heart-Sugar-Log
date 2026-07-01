package com.example.bptracker.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import android.app.Activity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.bptracker.AppContainer
import com.example.bptracker.R
import com.example.bptracker.theme.PrimaryBlue
import com.example.bptracker.theme.SurfaceDark
import com.example.bptracker.theme.TextSecondary
import kotlinx.coroutines.launch

@Composable
fun WelcomeScreen(onFinish: () -> Unit) {
    var step by rememberSaveable { mutableStateOf(0) } // 0 = Language, 1 = Carousel

    if (step == 0) {
        LanguageSelectionScreen {
            step = 1
        }
    } else {
        OnboardingCarouselScreen(onFinish)
    }
}

@Composable
fun LanguageSelectionScreen(onLanguageSelected: () -> Unit) {
    val context = LocalContext.current
    val languages = listOf(
        "default" to "English",
        "es" to "Español",
        "hi" to "हिन्दी",
        "pt" to "Português",
        "fr" to "Français"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E213A))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Public,
            contentDescription = null,
            tint = PrimaryBlue,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "Select Your Language",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            "You can change this later in Settings.",
            color = TextSecondary,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(48.dp))

        languages.forEach { (code, name) ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        val currentLang = AppContainer.preferences.currentLanguage.value
                        if (currentLang != code) {
                            AppContainer.preferences.setLanguage(code)
                            onLanguageSelected()
                            (context as? Activity)?.recreate()
                        } else {
                            onLanguageSelected()
                        }
                    },
                color = SurfaceDark
            ) {
                Text(
                    text = name,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingCarouselScreen(onFinish: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

    val slides = listOf(
        Triple(
            R.drawable.ic_onboarding_health,
            "Track Your Vitals",
            "Log your Blood Pressure, Blood Sugar, and Heart Rate with precision."
        ),
        Triple(
            R.drawable.ic_onboarding_activity,
            "Monitor Your Activity",
            "Count your daily steps and track your hydration to stay perfectly healthy."
        ),
        Triple(
            R.drawable.ic_onboarding_reminders,
            "Never Miss a Beat",
            "Set custom alarms to remind you to log your vitals and take your medications."
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top bar with Skip
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = {
                AppContainer.preferences.setFirstLaunchCompleted()
                onFinish()
            }) {
                Text("Skip", color = Color.Gray)
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            val slide = slides[page]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = slide.first),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    text = slide.second,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E213A),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = slide.third,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Bottom section (Indicators and Next/Get Started)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Dots
            Row {
                repeat(slides.size) { iteration ->
                    val color = if (pagerState.currentPage == iteration) PrimaryBlue else Color.LightGray
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(10.dp)
                    )
                }
            }

            // Next / Get Started button
            Button(
                onClick = {
                    if (pagerState.currentPage < slides.size - 1) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        AppContainer.preferences.setFirstLaunchCompleted()
                        onFinish()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    if (pagerState.currentPage == slides.size - 1) "Get Started" else "Next",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}
