package com.example.bptracker.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ComposeWheelPicker(
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit
) {
    val items = range.toList()
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = items.indexOf(value).coerceAtLeast(0))
    val coroutineScope = rememberCoroutineScope()
    
    // Snap behavior for the wheel
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    
    // Update value when scrolling stops
    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            val centerItemIndex = listState.firstVisibleItemIndex + 1 // +1 because we show 3 items and want the middle one
            if (centerItemIndex in items.indices) {
                onValueChange(items[centerItemIndex])
            }
        }
    }

    Box(
        modifier = Modifier
            .width(80.dp)
            .height(150.dp),
        contentAlignment = Alignment.Center
    ) {
        // Selection highlight lines
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.2f)))
            Spacer(modifier = Modifier.height(40.dp))
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.2f)))
        }

        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 55.dp) // Offset to allow first/last items to reach center
        ) {
            items(items.size) { index ->
                val itemValue = items[index]
                val isSelected = itemValue == value
                
                Box(
                    modifier = Modifier.height(40.dp).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = itemValue.toString(),
                        color = if (isSelected) Color.White else Color.White.copy(alpha = 0.3f),
                        style = if (isSelected) MaterialTheme.typography.titleLarge else MaterialTheme.typography.titleMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
