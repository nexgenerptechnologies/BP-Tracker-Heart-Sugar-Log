package com.example.bptracker.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bptracker.theme.*
import com.example.bptracker.data.*

val articles = bpArticles + sugarArticles + heartArticles + allBlogArticles.map { 
    Article(
        title = it.title,
        content = it.content,
        color = it.categoryColor,
        icon = androidx.compose.material.icons.Icons.Default.Info,
        category = ArticleCategory.BLOOD_PRESSURE
    )
}

@Composable
fun InfoScreen(preSelectedArticle: Article? = null, onBackFromArticle: () -> Unit = {}) {
    var selectedArticle by remember(preSelectedArticle) { mutableStateOf<Article?>(preSelectedArticle) }

    if (selectedArticle != null) {
        ArticleDetailScreen(article = selectedArticle!!, onBack = { 
            selectedArticle = null
            onBackFromArticle()
        })
    } else {
        InfoListScreen(onArticleClick = { selectedArticle = it })
    }
}

@Composable
fun CategoryChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .background(
                color = if (isSelected) PrimaryBlue else Color(0xFF333856),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text, color = if (isSelected) Color.White else TextSecondary, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun InfoListScreen(onArticleClick: (Article) -> Unit) {
    var selectedCategory by remember { mutableStateOf<ArticleCategory?>(null) }
    val filteredArticles = if (selectedCategory == null) articles else articles.filter { it.category == selectedCategory }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "INFO & KNOWLEDGE",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CategoryChip("All", selectedCategory == null) { selectedCategory = null }
            CategoryChip("BP", selectedCategory == ArticleCategory.BLOOD_PRESSURE) { selectedCategory = ArticleCategory.BLOOD_PRESSURE }
            CategoryChip("Sugar", selectedCategory == ArticleCategory.BLOOD_SUGAR) { selectedCategory = ArticleCategory.BLOOD_SUGAR }
            CategoryChip("Heart", selectedCategory == ArticleCategory.HEART_HEALTH) { selectedCategory = ArticleCategory.HEART_HEALTH }
        }

        filteredArticles.forEach { article ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp).clickable { onArticleClick(article) }.height(100.dp),
                colors = CardDefaults.cardColors(containerColor = article.color),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 24.dp).fillMaxSize(), 
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = article.title, 
                        color = if (article.color == ElevatedYellow) Color.Black else Color.White, 
                        style = MaterialTheme.typography.titleLarge, 
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f).padding(end = 16.dp)
                    )
                    Icon(
                        article.icon, 
                        contentDescription = null, 
                        tint = if (article.color == ElevatedYellow) Color.Black else Color.White, 
                        modifier = Modifier.size(56.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun ArticleDetailScreen(article: Article, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF25294A)) // Solid dark blue background matching screenshot
    ) {
        // Top Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(article.color)
                .padding(top = 16.dp, bottom = 40.dp, start = 16.dp, end = 16.dp)
        ) {
            Column {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = if (article.color == ElevatedYellow) Color.Black else Color.White)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                    Text(
                        text = article.title,
                        color = if (article.color == ElevatedYellow) Color.Black else Color.White,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f).padding(end = 16.dp)
                    )
                    Icon(
                        article.icon,
                        contentDescription = null,
                        tint = if (article.color == ElevatedYellow) Color.Black else Color.White,
                        modifier = Modifier.size(64.dp)
                    )
                }
            }
        }

        // Content Body
        Card(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = (-20).dp)
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                MarkdownRenderer(
                    text = article.content,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                // Medical Disclaimer
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E213A)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Disclaimer: This app is for informational purposes only and is not a substitute for professional medical advice, diagnosis, or treatment. Always consult with a doctor.",
                            color = TextSecondary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}
