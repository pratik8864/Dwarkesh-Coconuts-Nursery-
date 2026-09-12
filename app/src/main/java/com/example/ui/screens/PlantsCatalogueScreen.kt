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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProductEntity
import com.example.ui.AppLanguage
import com.example.ui.AppText
import com.example.ui.components.DwarkeshFooter
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.CreamSurface
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.FreshGreen
import com.example.ui.theme.GoldAccent
import com.example.viewmodel.MainViewModel

@Composable
fun PlantsCatalogueScreen(
    viewModel: MainViewModel,
    onPlantClick: (Long) -> Unit,
    onBookClick: (ProductEntity) -> Unit,
    onAdminClick: () -> Unit
) {
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val settings by viewModel.settingsMap.collectAsState()

    val categories = listOf("All", "Dwarf Varieties", "Special Hybrids", "Coconut Plants")

    val filteredProducts = allProducts.filter { plant ->
        val matchesCategory = selectedCategory == "All" || plant.category.equals(selectedCategory, ignoreCase = true)
        val matchesSearch = searchQuery.isBlank() ||
                plant.name.contains(searchQuery, ignoreCase = true) ||
                plant.gujaratiName.contains(searchQuery, ignoreCase = true) ||
                plant.description.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
            .testTag("plant_catalogue_screen"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Header & Search
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ForestGreenDark)
                    .padding(16.dp)
            ) {
                Text(
                    text = "🌱 " + AppText("latest_plants"),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = "High-Yield Certified Dwarf & Hybrid Coconut Saplings",
                    color = GoldAccent,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
                )

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    placeholder = {
                        Text(
                            text = AppText("search_hint"),
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = ForestGreen)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Color.Gray)
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = GoldAccent,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("plant_search_field"),
                    singleLine = true
                )
            }
        }

        // Category Filter Chips
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) ForestGreen else Color.White,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) ForestGreen else Color.LightGray
                        ),
                        modifier = Modifier
                            .clickable { viewModel.selectCategory(category) }
                            .testTag("filter_chip_$category")
                    ) {
                        Text(
                            text = if (category == "All") AppText("all") else category,
                            color = if (isSelected) Color.White else ForestGreenDark,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                        )
                    }
                }
            }
        }

        // Product Count Summary
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Showing ${filteredProducts.size} Varieties",
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Plants List
        if (filteredProducts.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🔍", fontSize = 40.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No plants found matching '$searchQuery'",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        } else {
            items(filteredProducts) { plant ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    PlantCard(
                        plant = plant,
                        currentLanguage = currentLanguage,
                        onClick = { onPlantClick(plant.id) },
                        onBookClick = { onBookClick(plant) }
                    )
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
