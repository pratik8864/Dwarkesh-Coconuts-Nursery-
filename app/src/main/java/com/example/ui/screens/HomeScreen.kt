package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.NaturePeople
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.OfferEntity
import com.example.data.ProductEntity
import com.example.data.ReviewEntity
import com.example.ui.AppLanguage
import com.example.ui.AppText
import com.example.ui.LocalAppLanguage
import com.example.ui.components.DwarkeshFooter
import com.example.ui.components.DwarkeshLogoView
import com.example.ui.components.PlantVisualCard
import com.example.ui.components.QuickContactButtons
import com.example.ui.components.dialPhoneNumber
import com.example.ui.components.openWhatsAppChat
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.CreamSurface
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.FreshGreen
import com.example.ui.theme.FreshGreenLight
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldAccentDark
import com.example.ui.theme.GoldAccentLight
import com.example.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToPlants: () -> Unit,
    onNavigateToPlantDetail: (Long) -> Unit,
    onNavigateToBooking: (ProductEntity) -> Unit,
    onNavigateToGallery: () -> Unit,
    onNavigateToAdmin: () -> Unit
) {
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val featuredProducts by viewModel.featuredProducts.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()
    val offers by viewModel.activeOffers.collectAsState()
    val reviews by viewModel.approvedReviews.collectAsState()
    val settings by viewModel.settingsMap.collectAsState()

    val primaryPhone = settings["phone_1"] ?: "8200596044"
    val secondaryPhone = settings["phone_2"] ?: "9426477948"
    val managerName = settings["phone_3_name"] ?: "Vansh Ram"
    val managerPhone = settings["phone_3"] ?: "9328040045"

    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
            .testTag("home_screen_list"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // 1. HERO SECTION WITH RICH BRANDING & CALL TO ACTION
        item {
            HeroSection(
                currentLanguage = currentLanguage,
                onExploreClick = onNavigateToPlants,
                onAdvanceBookingClick = {
                    val defaultPlant = featuredProducts.firstOrNull() ?: allProducts.firstOrNull()
                    if (defaultPlant != null) {
                        onNavigateToBooking(defaultPlant)
                    } else {
                        onNavigateToPlants()
                    }
                },
                primaryPhone = primaryPhone,
                managerPhone = managerPhone
            )
        }

        // 2. QUICK STATS / TRUST BADGES
        item {
            TrustBadgesSection()
        }

        // 3. SPECIAL OFFERS BANNER (IF ACTIVE)
        if (offers.isNotEmpty()) {
            item {
                SpecialOffersSection(offers = offers, currentLanguage = currentLanguage)
            }
        }

        // 4. FEATURED COCONUT PLANTS
        item {
            FeaturedPlantsSection(
                plants = if (featuredProducts.isNotEmpty()) featuredProducts else allProducts.take(4),
                currentLanguage = currentLanguage,
                onViewAllClick = onNavigateToPlants,
                onPlantClick = onNavigateToPlantDetail,
                onBookClick = onNavigateToBooking
            )
        }

        // 5. WHY CHOOSE DWARKESH COCONUTS & NURSERY
        item {
            WhyChooseUsSection(currentLanguage = currentLanguage)
        }

        // 6. HOW TO BOOK COCONUT PLANTS (4 EASY STEPS)
        item {
            HowToBookSection(currentLanguage = currentLanguage)
        }

        // 7. FARMER & CUSTOMER REVIEWS
        item {
            FarmerReviewsSection(
                reviews = reviews,
                currentLanguage = currentLanguage,
                onWriteReview = {
                    // Quick dialog or call action
                    openWhatsAppChat(context, managerPhone, "Hello Vansh Ram bhai, I want to give feedback for my coconut plants order.")
                }
            )
        }

        // 8. GALLERY PREVIEW
        item {
            GalleryPreviewSection(onViewAllGallery = onNavigateToGallery)
        }

        // 9. FOOTER
        item {
            Spacer(modifier = Modifier.height(20.dp))
            DwarkeshFooter(
                primaryPhone = primaryPhone,
                secondaryPhone = secondaryPhone,
                managerName = managerName,
                managerPhone = managerPhone,
                onAdminClick = onNavigateToAdmin
            )
        }
    }
}

/**
 * Premium Hero Section
 */
@Composable
fun HeroSection(
    currentLanguage: AppLanguage,
    onExploreClick: () -> Unit,
    onAdvanceBookingClick: () -> Unit,
    primaryPhone: String,
    managerPhone: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        ForestGreenDark,
                        ForestGreen,
                        Color(0xFF134E2D)
                    )
                )
            )
            .padding(horizontal = 20.dp, vertical = 28.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Hero Emblem with delicate golden glow
            DwarkeshLogoView(
                size = 140.dp,
                showGlow = true,
                transparentBackground = true,
                animatedLeaves = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tagline Badge
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0x33FFD54F),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldAccent)
            ) {
                Text(
                    text = "🌱 " + AppText("app_tagline"),
                    color = GoldAccentLight,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main Hero Headline
            Text(
                text = AppText("hero_heading"),
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Serif,
                fontSize = 22.sp,
                lineHeight = 30.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Gujarati / English Subheading
            Text(
                text = AppText("hero_subheading"),
                color = Color(0xFFE2EFE5),
                fontSize = 13.sp,
                lineHeight = 20.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(22.dp))

            // Primary Call to Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onExploreClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("hero_explore_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldAccent,
                        contentColor = ForestGreenDark
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = AppText("explore_plants"),
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp
                    )
                }

                Button(
                    onClick = onAdvanceBookingClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("hero_booking_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FreshGreen,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = AppText("advance_booking"),
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Contact Direct Actions
            QuickContactButtons(
                primaryPhone = primaryPhone,
                whatsappPhone = managerPhone
            )
        }
    }
}

/**
 * Trust Highlights Badges
 */
@Composable
fun TrustBadgesSection() {
    val badges = listOf(
        Pair("100% Genuine", "Certified pedigree seed nuts"),
        Pair("High Yielding", "200-250+ nuts per tree"),
        Pair("Disease-Free", "Vigorous root system"),
        Pair("Farm Delivery", "Doorstep across Gujarat")
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(badges) { (title, subtitle) ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.width(160.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = FreshGreen,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenDark,
                        fontSize = 13.sp
                    )
                    Text(
                        text = subtitle,
                        color = Color.Gray,
                        fontSize = 11.sp,
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }
}

/**
 * Special Offers Banner
 */
@Composable
fun SpecialOffersSection(
    offers: List<OfferEntity>,
    currentLanguage: AppLanguage
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "🔥 " + AppText("special_offers"),
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = ForestGreenDark
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        offers.forEach { offer ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9E6)),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldAccent)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DwarkeshLogoView(
                        size = 50.dp,
                        transparentBackground = true
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (currentLanguage == AppLanguage.GUJARATI && offer.gujaratiTitle.isNotBlank()) {
                                offer.gujaratiTitle
                            } else offer.title,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenDark,
                            fontSize = 14.sp
                        )
                        Text(
                            text = offer.discountText,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFC62828),
                            fontSize = 13.sp
                        )
                        Text(
                            text = offer.description,
                            color = Color(0xFF5D4037),
                            fontSize = 11.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

/**
 * Featured Coconut Plants Cards
 */
@Composable
fun FeaturedPlantsSection(
    plants: List<ProductEntity>,
    currentLanguage: AppLanguage,
    onViewAllClick: () -> Unit,
    onPlantClick: (Long) -> Unit,
    onBookClick: (ProductEntity) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = AppText("featured_plants"),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenDark
                )
                Text(
                    text = "High Yielding Dwarf & Hybrid Varieties",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
            Text(
                text = "View All →",
                color = FreshGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                modifier = Modifier
                    .clickable(onClick = onViewAllClick)
                    .padding(4.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Grid of plant cards
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            plants.forEach { plant ->
                PlantCard(
                    plant = plant,
                    currentLanguage = currentLanguage,
                    onClick = { onPlantClick(plant.id) },
                    onBookClick = { onBookClick(plant) }
                )
            }
        }
    }
}

@Composable
fun PlantCard(
    plant: ProductEntity,
    currentLanguage: AppLanguage,
    onClick: () -> Unit,
    onBookClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .shadow(3.dp, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Plant Illustration / Image
            PlantVisualCard(
                imageKey = plant.mainImage,
                modifier = Modifier
                    .size(95.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Category & Stock Tag
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = CreamSurface
                    ) {
                        Text(
                            text = plant.category,
                            color = ForestGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    if (plant.isAvailable && plant.stockQuantity > 0) {
                        Text(
                            text = "● ${AppText("available")}",
                            color = FreshGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = "● ${AppText("out_of_stock")}",
                            color = Color.Red,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Plant Name (Gujarati / English dual display)
                Text(
                    text = if (currentLanguage == AppLanguage.GUJARATI && plant.gujaratiName.isNotBlank()) {
                        plant.gujaratiName
                    } else plant.name,
                    fontWeight = FontWeight.ExtraBold,
                    color = ForestGreenDark,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (plant.gujaratiName.isNotBlank() && currentLanguage != AppLanguage.GUJARATI) {
                    Text(
                        text = plant.gujaratiName,
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Age & Min Order
                Text(
                    text = "Age: ${plant.age} • Min: ${plant.minOrderQuantity} plants",
                    fontSize = 11.sp,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Price & Book Now button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "₹${plant.price.toInt()}",
                            fontWeight = FontWeight.Black,
                            color = ForestGreenDark,
                            fontSize = 17.sp
                        )
                        if (plant.oldPrice > plant.price) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "₹${plant.oldPrice.toInt()}",
                                textDecoration = TextDecoration.LineThrough,
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Button(
                        onClick = onBookClick,
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text(
                            text = AppText("book_now"),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

/**
 * Why Choose Us Section
 */
@Composable
fun WhyChooseUsSection(currentLanguage: AppLanguage) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F321E)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = "🌟 " + AppText("why_choose_us"),
                color = GoldAccentLight,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            val points = listOf(
                "100% Genuine Pedigree: Cultivated from certified seed coconuts with verified mother palm records.",
                "Fast Early Bearing: Dwarf varieties start yielding sweet tender coconuts in 3 to 4 years.",
                "Complete Agricultural Guidance: Soil analysis, pit digging spacing, organic fertilizer advice by nursery experts.",
                "Safe Farm Transportation: Direct delivery to farm gates across all districts in Gujarat."
            )

            points.forEach { point ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier
                            .size(18.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = point,
                        color = Color(0xFFE8F5E9),
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

/**
 * How to Book Section (4 Steps)
 */
@Composable
fun HowToBookSection(currentLanguage: AppLanguage) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "📋 " + AppText("how_to_book"),
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = ForestGreenDark
        )
        Spacer(modifier = Modifier.height(10.dp))

        val steps = listOf(
            Triple("1", "Select Plant Variety", "Choose your preferred dwarf or hybrid coconut saplings."),
            Triple("2", "Specify Quantity & Farm Details", "Enter number of plants and your farm/village location."),
            Triple("3", "Pay Token Advance", "Confirm your order with a small advance booking amount."),
            Triple("4", "Direct Farm Delivery", "Receive healthy, vigorous saplings directly at your farm gate!")
        )

        steps.forEach { (step, title, desc) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = GoldAccent,
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = step,
                            fontWeight = FontWeight.Black,
                            color = ForestGreenDark,
                            fontSize = 14.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenDark,
                        fontSize = 13.sp
                    )
                    Text(
                        text = desc,
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

/**
 * Farmer Reviews Section
 */
@Composable
fun FarmerReviewsSection(
    reviews: List<ReviewEntity>,
    currentLanguage: AppLanguage,
    onWriteReview: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "💬 " + AppText("customer_reviews"),
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = ForestGreenDark
            )
            Text(
                text = "+ Write Review",
                color = FreshGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.clickable(onClick = onWriteReview)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(reviews) { review ->
                Card(
                    modifier = Modifier.width(260.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            repeat(review.rating) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = GoldAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "\"${review.reviewText}\"",
                            fontSize = 12.sp,
                            color = Color(0xFF333333),
                            lineHeight = 17.sp,
                            maxLines = 4,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "- ${review.customerName} (${review.villageOrCity})",
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenDark,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * Gallery Preview Section
 */
@Composable
fun GalleryPreviewSection(
    onViewAllGallery: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CreamSurface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DwarkeshLogoView(
                size = 70.dp,
                transparentBackground = true
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "📸 " + AppText("nursery_gallery"),
                fontWeight = FontWeight.Bold,
                color = ForestGreenDark,
                fontSize = 16.sp
            )
            Text(
                text = "Take a virtual tour of our certified coconut beds, mother palms & farmer plantation trucks.",
                fontSize = 12.sp,
                color = Color.DarkGray,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(vertical = 6.dp)
            )
            Button(
                onClick = onViewAllGallery,
                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Open Full Gallery",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}
