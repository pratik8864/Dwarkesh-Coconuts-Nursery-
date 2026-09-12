package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ProductEntity
import com.example.ui.AppLanguage
import com.example.ui.AppText
import com.example.ui.components.DwarkeshFooter
import com.example.ui.components.PlantVisualCard
import com.example.ui.components.QuickContactButtons
import com.example.ui.components.dialPhoneNumber
import com.example.ui.components.openWhatsAppChat
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.CreamSurface
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.FreshGreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldAccentLight
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantDetailScreen(
    plantId: Long,
    viewModel: MainViewModel,
    onBackClick: () -> Unit,
    onBookClick: (ProductEntity) -> Unit,
    onAdminClick: () -> Unit
) {
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()
    val settings by viewModel.settingsMap.collectAsState()

    val plant = allProducts.find { it.id == plantId } ?: allProducts.firstOrNull()

    val primaryPhone = settings["phone_1"] ?: "8200596044"
    val secondaryPhone = settings["phone_2"] ?: "9426477948"
    val managerName = settings["phone_3_name"] ?: "Vansh Ram"
    val managerPhone = settings["phone_3"] ?: "9328040045"

    val context = LocalContext.current

    if (plant == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Plant not found")
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (currentLanguage == AppLanguage.GUJARATI && plant.gujaratiName.isNotBlank()) {
                            plant.gujaratiName
                        } else plant.name,
                        maxLines = 1,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ForestGreenDark,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            // Fixed Bottom Bar with Advance Booking Button
            Surface(
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "₹${plant.price.toInt()}",
                            fontWeight = FontWeight.Black,
                            color = ForestGreenDark,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "per plant",
                            color = Color.Gray,
                            fontSize = 11.sp
                        )
                    }

                    Button(
                        onClick = { onBookClick(plant) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("detail_book_now_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "🌱 " + AppText("book_now"),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(CreamBackground)
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Main Plant Visual Art
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                ) {
                    PlantVisualCard(
                        imageKey = plant.mainImage,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Title, Gujarati Name, Price & Stock
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = CreamSurface
                            ) {
                                Text(
                                    text = plant.category,
                                    color = ForestGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }

                            if (plant.isAvailable && plant.stockQuantity > 0) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFFE8F5E9)
                                ) {
                                    Text(
                                        text = "In Stock (${plant.stockQuantity} Available)",
                                        color = FreshGreen,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            } else {
                                Text(
                                    text = AppText("out_of_stock"),
                                    color = Color.Red,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Gujarati Plant Name prominently
                        if (plant.gujaratiName.isNotBlank()) {
                            Text(
                                text = plant.gujaratiName,
                                color = ForestGreenDark,
                                fontWeight = FontWeight.Black,
                                fontSize = 20.sp
                            )
                        }

                        // English Plant Name
                        Text(
                            text = plant.name,
                            color = Color(0xFF4A5568),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Price Row
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "₹${plant.price.toInt()}",
                                fontWeight = FontWeight.Black,
                                color = ForestGreenDark,
                                fontSize = 26.sp
                            )
                            if (plant.oldPrice > plant.price) {
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "₹${plant.oldPrice.toInt()}",
                                    textDecoration = TextDecoration.LineThrough,
                                    color = Color.Gray,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color(0xFFFFEBEE)
                                ) {
                                    Text(
                                        text = "${plant.discountPercent}% OFF",
                                        color = Color(0xFFD32F2F),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Quick Specs Grid
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Card(
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(containerColor = CreamSurface)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("Plant Age", fontSize = 11.sp, color = Color.Gray)
                                    Text(plant.age, fontWeight = FontWeight.Bold, color = ForestGreenDark, fontSize = 13.sp)
                                }
                            }
                            Card(
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(containerColor = CreamSurface)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("Min Order", fontSize = 11.sp, color = Color.Gray)
                                    Text("${plant.minOrderQuantity} Plants", fontWeight = FontWeight.Bold, color = ForestGreenDark, fontSize = 13.sp)
                                }
                            }
                            Card(
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(containerColor = CreamSurface)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("Delivery", fontSize = 11.sp, color = Color.Gray)
                                    Text("Farm Gate", fontWeight = FontWeight.Bold, color = ForestGreenDark, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
            }

            // Description & Benefits
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "📖 About Variety",
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenDark,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = plant.description,
                            color = Color(0xFF333333),
                            fontSize = 13.sp,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "✨ " + AppText("benefits"),
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenDark,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = plant.benefits,
                            color = FreshGreen,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp,
                            lineHeight = 19.sp
                        )
                    }
                }
            }

            // Direct WhatsApp Enquiry & Call
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Have questions about this variety?",
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenDark,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Chat directly with nursery owner $managerName for planting season & soil suitability.",
                            color = Color(0xFF2E7D32),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                        QuickContactButtons(
                            primaryPhone = primaryPhone,
                            whatsappPhone = managerPhone,
                            inquiryText = "Hello $managerName bhai, I am interested in inquiring about ${plant.name} (${plant.gujaratiName}) from Dwarkesh Coconuts & Nursery."
                        )
                    }
                }
            }

            // Footer
            item {
                Spacer(modifier = Modifier.height(20.dp))
                DwarkeshFooter(
                    primaryPhone = primaryPhone,
                    secondaryPhone = secondaryPhone,
                    managerName = managerName,
                    managerPhone = managerPhone,
                    onAdminClick = onAdminClick
                )
            }
        }
    }
}
