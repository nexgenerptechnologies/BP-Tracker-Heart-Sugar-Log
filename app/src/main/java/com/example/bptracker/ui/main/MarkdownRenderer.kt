package com.example.bptracker.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MarkdownRenderer(text: String, modifier: Modifier = Modifier) {
    val blocks = text.split("\n\n")

    Column(modifier = modifier) {
        blocks.forEach { block ->
            val trimmed = block.trim()
            if (trimmed.isEmpty()) return@forEach

            when {
                trimmed.startsWith("### ") -> {
                    Text(
                        text = trimmed.removePrefix("### "),
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }
                trimmed.startsWith("## ") -> {
                    Text(
                        text = trimmed.removePrefix("## "),
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 24.dp, bottom = 12.dp)
                    )
                }
                trimmed.startsWith("# ") -> {
                    Text(
                        text = trimmed.removePrefix("# "),
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
                    )
                }
                else -> {
                    // Check if block is a list
                    if (trimmed.startsWith("- ") || trimmed.startsWith("* ")) {
                        val items = trimmed.split("\n")
                        Column(modifier = Modifier.padding(bottom = 12.dp)) {
                            items.forEach { item ->
                                val cleanItem = item.trim().removePrefix("- ").removePrefix("* ")
                                if (cleanItem.isNotEmpty()) {
                                    Row(modifier = Modifier.padding(bottom = 8.dp, start = 8.dp)) {
                                        Text("•", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 8.dp))
                                        Text(
                                            text = parseInlineMarkdown(cleanItem),
                                            color = Color.White,
                                            style = MaterialTheme.typography.bodyLarge,
                                            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.5
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        // Regular paragraph
                        Text(
                            text = parseInlineMarkdown(trimmed),
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge,
                            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.5,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }
                }
            }
        }
    }
}

fun parseInlineMarkdown(text: String) = buildAnnotatedString {
    var currentIndex = 0
    val boldRegex = Regex("\\*\\*(.*?)\\*\\*")
    val matches = boldRegex.findAll(text)

    for (match in matches) {
        // Append normal text before the bold segment
        append(text.substring(currentIndex, match.range.first))
        
        // Append bold text
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(match.groupValues[1])
        }
        
        currentIndex = match.range.last + 1
    }
    
    // Append remaining normal text
    if (currentIndex < text.length) {
        append(text.substring(currentIndex))
    }
}
