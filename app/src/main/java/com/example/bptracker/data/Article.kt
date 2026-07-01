package com.example.bptracker.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class ArticleCategory {
    BLOOD_PRESSURE,
    BLOOD_SUGAR,
    HEART_HEALTH
}

data class Article(
    val title: String,
    val content: String,
    val color: Color,
    val icon: ImageVector,
    val category: ArticleCategory
)
