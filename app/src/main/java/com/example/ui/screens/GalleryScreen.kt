package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppText
import com.example.ui.components.DwarkeshFooter
import com.example.ui.components.DwarkeshLogoView
import com.example.ui.components.PlantVisualCard
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.CreamSurface
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.FreshGreen
import com.example.ui.theme.GoldAccent
import com.example.viewmodel.MainViewModel

@Composable
fun GalleryScreen(
    viewModel: MainViewModel,
    onAdminClick: () -> Unit
) {
    val galleryItems by viewModel.activeGalleryItems.collectAsState()
    val settings by viewModel.settingsMap.collectAsState()

    val categories = listOf("All", "Nursery", "Coconut Plants", "Plant Delivery", "Plantation", "Customers", "Farm", "Videos")
    var selectedCategory by remember { mutableStateOf("All") }
    var previewItem by remember { mutableStateOf<com.example.data.GalleryItemEntity?>(null) }

    val filteredItems = if (selectedCategory == "All") {
        galleryItems
    } else {
        galleryItems.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }

    if (previewItem != null) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { previewItem = null },
            confirmButton = {
                androidx.compose.material3.TextButton(onClick = { previewItem = null }) {
                    Text("Close", color = ForestGreenDark, fontWeight = FontWeight.Bold)
                }
            },
            title = {
                Text(
                    text = previewItem?.title ?: "Photo Preview",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = ForestGreenDark
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        PlantVisualCard(
                            imageKey = previewItem?.imageUrl ?: "",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Category: ${previewItem?.category}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
            .testTag("gallery_screen"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ForestGreenDark)
                    .padding(20.dp)
            ) {
                Text(
                    text = "📸 " + AppText("nursery_gallery"),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = "Authentic photos of nursery beds, mother palms & farmer deliveries",
                    color = GoldAccent,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Category Chips
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    val isSelected = selectedCategory == cat
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) ForestGreen else Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) ForestGreen else Color.LightGray),
                        modifier = Modifier.clickable { selectedCategory = cat }
                    ) {
                        Text(
                            text = cat,
                            color = if (isSelected) Color.White else ForestGreenDark,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                        )
                    }
                }
            }
        }

        if (filteredItems.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "🌿", fontSize = 36.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No gallery photos available in this category.",
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        // Gallery Items
        items(filteredItems) { item ->
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { previewItem = item },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(190.dp)
                        ) {
                            PlantVisualCard(
                                imageKey = item.imageUrl,
                                modifier = Modifier.fillMaxSize()
                            )
                            Surface(
                                shape = RoundedCornerShape(bottomEnd = 10.dp),
                                color = ForestGreenDark.copy(alpha = 0.9f),
                                modifier = Modifier.align(Alignment.TopStart)
                            ) {
                                Text(
                                    text = item.category,
                                    color = GoldAccent,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = item.title,
                                fontWeight = FontWeight.Bold,
                                color = ForestGreenDark,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        // Footer
        item {
            Spacer(modifier = Modifier.height(20.dp))
            DwarkeshFooter(
                primaryPhone = settings["phone_1"] ?: "8200596044",
                secondaryPhone = settings["phone_2"] ?: "9426477948",
                managerName = settings["phone_3_name"] ?: "Vansh Ram",
                managerPhone = settings["phone_3"] ?: "9328040045",
                onAdminClick = onAdminClick
            )
        }
    }
}
