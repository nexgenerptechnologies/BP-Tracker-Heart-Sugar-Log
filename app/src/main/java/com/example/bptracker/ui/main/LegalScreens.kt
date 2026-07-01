package com.example.bptracker.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bptracker.ui.theme.BackgroundDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Privacy Policy", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundDark)
            )
        },
        containerColor = BackgroundDark
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            LegalSectionTitle("Privacy Policy for BP Tracker")
            LegalSectionText("Effective Date: January 1, 2026")
            
            Spacer(modifier = Modifier.height(24.dp))

            LegalSectionTitle("1. Introduction")
            LegalSectionText("Welcome to BP Tracker. We deeply respect your privacy and are committed to protecting your personal health data. This Privacy Policy explains how we collect, use, and safeguard your information when you use our mobile application.")

            Spacer(modifier = Modifier.height(16.dp))

            LegalSectionTitle("2. Data Collection and Local Storage")
            LegalSectionText("BP Tracker is designed with a privacy-first approach. All of your health metrics—including blood pressure readings, blood sugar logs, heart rate data, and medication reminders—are stored locally on your device using a local database (Room DB). We do not transmit, sell, or store your personal health data on external servers.")

            Spacer(modifier = Modifier.height(16.dp))

            LegalSectionTitle("3. Information You Provide")
            LegalSectionText("You may choose to input personal health information into the app for tracking purposes. This information remains solely on your device. We do not require you to create an account, and we do not collect personally identifiable information (PII) such as your name, email, or phone number.")

            Spacer(modifier = Modifier.height(16.dp))

            LegalSectionTitle("4. Usage Data and Analytics")
            LegalSectionText("We may collect anonymous, non-identifying usage data (such as app crashes and feature interactions) to help us improve the app's performance and user experience. This data cannot be traced back to you and contains no health information.")

            Spacer(modifier = Modifier.height(16.dp))

            LegalSectionTitle("5. Third-Party Services")
            LegalSectionText("Our application does not share your health data with third-party advertisers or data brokers. If you use external backup solutions provided by your device (such as Google Drive or iCloud), those backups are subject to the privacy policies of those respective services.")

            Spacer(modifier = Modifier.height(16.dp))

            LegalSectionTitle("6. Contact Us")
            LegalSectionText("If you have any questions about this Privacy Policy or how your data is handled, please contact us at support@bptracker.com.")
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsOfUseScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Terms of Use", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundDark)
            )
        },
        containerColor = BackgroundDark
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            LegalSectionTitle("Terms of Use")
            LegalSectionText("Effective Date: January 1, 2026")
            
            Spacer(modifier = Modifier.height(24.dp))

            LegalSectionTitle("1. Acceptance of Terms")
            LegalSectionText("By downloading, accessing, or using BP Tracker, you agree to be bound by these Terms of Use. If you do not agree with any part of these terms, please do not use the application.")

            Spacer(modifier = Modifier.height(16.dp))

            LegalSectionTitle("2. Not Medical Advice")
            LegalSectionText("IMPORTANT: BP Tracker is a tool designed to help you log and monitor your personal health metrics. IT IS NOT A SUBSTITUTE FOR PROFESSIONAL MEDICAL ADVICE, DIAGNOSIS, OR TREATMENT. Always seek the advice of your physician or other qualified health provider with any questions you may have regarding a medical condition.")

            Spacer(modifier = Modifier.height(16.dp))

            LegalSectionTitle("3. User Responsibilities")
            LegalSectionText("You are solely responsible for the accuracy of the data you enter into the app. BP Tracker relies on the numbers you input to generate charts and statistics. We do not verify this data and cannot be held liable for any decisions made based on inaccurate entries.")

            Spacer(modifier = Modifier.height(16.dp))

            LegalSectionTitle("4. Prohibited Uses")
            LegalSectionText("You agree not to use the app in any way that violates applicable laws or regulations. You may not attempt to reverse engineer, decompile, or extract the source code of the application.")

            Spacer(modifier = Modifier.height(16.dp))

            LegalSectionTitle("5. Limitation of Liability")
            LegalSectionText("To the maximum extent permitted by law, the creators of BP Tracker shall not be liable for any direct, indirect, incidental, or consequential damages resulting from your use or inability to use the application.")

            Spacer(modifier = Modifier.height(16.dp))

            LegalSectionTitle("6. Modifications to Terms")
            LegalSectionText("We reserve the right to modify these Terms of Use at any time. Continued use of the app following any changes indicates your acceptance of the new terms.")

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun LegalSectionTitle(title: String) {
    Text(
        text = title,
        color = Color.White,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun LegalSectionText(text: String) {
    Text(
        text = text,
        color = Color.LightGray,
        fontSize = 15.sp,
        lineHeight = 22.sp
    )
}
