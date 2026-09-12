package com.example.ui.screens.admin

import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BookingEntity
import com.example.data.OfferEntity
import com.example.data.ProductEntity
import com.example.data.ReviewEntity
import com.example.ui.AppText
import com.example.ui.components.DwarkeshLogoView
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.CreamSurface
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.FreshGreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldAccentDark
import com.example.ui.theme.GoldAccentLight
import com.example.viewmodel.MainViewModel

enum class AdminTab {
    DASHBOARD,
    PRODUCTS,
    BOOKINGS,
    GALLERY,
    BRANDING,
    REVIEWS,
    OFFERS,
    SETTINGS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    viewModel: MainViewModel,
    onViewReceipt: (BookingEntity) -> Unit,
    onLogout: () -> Unit
) {
    val products by viewModel.allProducts.collectAsState()
    val bookings by viewModel.allBookings.collectAsState()
    val reviews by viewModel.allReviews.collectAsState()
    val offers by viewModel.allOffers.collectAsState()
    val settings by viewModel.settingsMap.collectAsState()

    var selectedTab by remember { mutableStateOf(AdminTab.DASHBOARD) }
    var editingProduct by remember { mutableStateOf<ProductEntity?>(null) }
    var isAddingProduct by remember { mutableStateOf(false) }
    var editingOffer by remember { mutableStateOf<OfferEntity?>(null) }
    var isAddingOffer by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        DwarkeshLogoView(size = 36.dp, transparentBackground = true)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Admin Hub",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.adminLogout()
                            onLogout()
                        }
                    ) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Log Out", tint = GoldAccentLight)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ForestGreenDark)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CreamBackground)
                .padding(paddingValues)
        ) {
            // Admin Module Tabs
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF071F13))
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val tabs = listOf(
                    Pair(AdminTab.DASHBOARD, "📊 Overview"),
                    Pair(AdminTab.PRODUCTS, "🌱 Plants (${products.size})"),
                    Pair(AdminTab.BOOKINGS, "📦 Orders (${bookings.size})"),
                    Pair(AdminTab.GALLERY, "📸 Gallery"),
                    Pair(AdminTab.BRANDING, "🎨 Branding"),
                    Pair(AdminTab.REVIEWS, "💬 Reviews (${reviews.size})"),
                    Pair(AdminTab.OFFERS, "🔥 Offers"),
                    Pair(AdminTab.SETTINGS, "⚙️ Contacts")
                )
                items(tabs) { (tab, label) ->
                    val isSelected = selectedTab == tab
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) GoldAccent else Color.Transparent,
                        modifier = Modifier
                            .clickable { selectedTab = tab }
                            .testTag("admin_tab_${tab.name.lowercase()}")
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected) ForestGreenDark else Color.White,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // Tab Content
            when (selectedTab) {
                AdminTab.DASHBOARD -> AdminOverviewContent(
                    products = products,
                    bookings = bookings,
                    reviews = reviews,
                    onNavigateTab = { selectedTab = it }
                )
                AdminTab.PRODUCTS -> AdminProductsContent(
                    products = products,
                    onAddProduct = { isAddingProduct = true },
                    onEditProduct = { editingProduct = it },
                    onDeleteProduct = { viewModel.deleteProduct(it) }
                )
                AdminTab.BOOKINGS -> AdminBookingsContent(
                    bookings = bookings,
                    onUpdateStatus = { id, status -> viewModel.updateBookingStatus(id, status) },
                    onUpdatePayment = { id, status, adv, rem -> viewModel.updateBookingPayment(id, status, adv, rem) },
                    onViewReceipt = onViewReceipt,
                    onDeleteBooking = { viewModel.deleteBooking(it) }
                )
                AdminTab.GALLERY -> AdminGalleryManagementContent(
                    viewModel = viewModel
                )
                AdminTab.BRANDING -> AdminBrandingManagementContent(
                    viewModel = viewModel
                )
                AdminTab.REVIEWS -> AdminReviewsContent(
                    reviews = reviews,
                    onApprove = { id, approved -> viewModel.setReviewApproval(id, approved) },
                    onDelete = { viewModel.deleteReview(it) }
                )
                AdminTab.OFFERS -> AdminOffersContent(
                    offers = offers,
                    onAddOffer = { isAddingOffer = true },
                    onEditOffer = { editingOffer = it },
                    onDeleteOffer = { viewModel.deleteOffer(it) }
                )
                AdminTab.SETTINGS -> AdminSettingsContent(
                    settings = settings,
                    onSave = { key, value ->
                        viewModel.updateSetting(key, value)
                        Toast.makeText(context, "Setting updated: $key", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    // Product Add / Edit Dialog
    if (isAddingProduct || editingProduct != null) {
        val initial = editingProduct ?: ProductEntity(
            name = "",
            gujaratiName = "",
            description = "High yielding coconut plant nurtured from verified seed nut.",
            benefits = "Early yield, heavy bearing, high water volume.",
            age = "12 Months",
            price = 150.0,
            oldPrice = 180.0,
            discountPercent = 15,
            stockQuantity = 500,
            isAvailable = true,
            minOrderQuantity = 5,
            mainImage = "plant_green_dwarf",
            category = "Dwarf Varieties"
        )
        ProductEditDialog(
            product = initial,
            isNew = isAddingProduct,
            onDismiss = {
                isAddingProduct = false
                editingProduct = null
            },
            onSave = { saved ->
                viewModel.saveProduct(saved) {
                    Toast.makeText(context, "Product saved successfully!", Toast.LENGTH_SHORT).show()
                }
                isAddingProduct = false
                editingProduct = null
            }
        )
    }

    // Offer Add / Edit Dialog
    if (isAddingOffer || editingOffer != null) {
        val initial = editingOffer ?: OfferEntity(
            title = "",
            gujaratiTitle = "",
            description = "",
            discountText = "Flat ₹20 OFF / Plant",
            isActive = true
        )
        OfferEditDialog(
            offer = initial,
            isNew = isAddingOffer,
            onDismiss = {
                isAddingOffer = false
                editingOffer = null
            },
            onSave = { saved ->
                viewModel.saveOffer(saved) {
                    Toast.makeText(context, "Offer saved!", Toast.LENGTH_SHORT).show()
                }
                isAddingOffer = false
                editingOffer = null
            }
        )
    }
}

/**
 * Overview Metrics Content
 */
@Composable
fun AdminOverviewContent(
    products: List<ProductEntity>,
    bookings: List<BookingEntity>,
    reviews: List<ReviewEntity>,
    onNavigateTab: (AdminTab) -> Unit
) {
    val totalRevenue = bookings.sumOf { it.totalAmount }
    val advanceReceived = bookings.sumOf { it.advanceAmount }
    val totalPlantsBooked = bookings.sumOf { it.quantity }
    val pendingBookings = bookings.count { it.deliveryStatus != "Delivered" && it.deliveryStatus != "Cancelled" }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "📊 Nursery Business Dashboard",
                fontWeight = FontWeight.Bold,
                color = ForestGreenDark,
                fontSize = 18.sp
            )
        }

        // 4 KPI Cards Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = "Total Bookings",
                    value = "${bookings.size}",
                    sub = "$totalPlantsBooked plants ordered",
                    color = ForestGreen,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateTab(AdminTab.BOOKINGS) }
                )
                MetricCard(
                    title = "Pending Deliveries",
                    value = "$pendingBookings",
                    sub = "Awaiting dispatch",
                    color = Color(0xFFE65100),
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateTab(AdminTab.BOOKINGS) }
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard(
                    title = "Advance Received",
                    value = "₹${advanceReceived.toInt()}",
                    sub = "Total Order: ₹${totalRevenue.toInt()}",
                    color = FreshGreen,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateTab(AdminTab.BOOKINGS) }
                )
                MetricCard(
                    title = "Plant Varieties",
                    value = "${products.size}",
                    sub = "Total in catalogue",
                    color = ForestGreenDark,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateTab(AdminTab.PRODUCTS) }
                )
            }
        }

        // Quick Actions
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "⚡ Quick Actions",
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenDark,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onNavigateTab(AdminTab.PRODUCTS) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
                        ) {
                            Text("+ New Plant", fontSize = 12.sp)
                        }
                        Button(
                            onClick = { onNavigateTab(AdminTab.OFFERS) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = FreshGreen)
                        ) {
                            Text("+ New Offer", fontSize = 12.sp)
                        }
                        Button(
                            onClick = { onNavigateTab(AdminTab.SETTINGS) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5D4037))
                        ) {
                            Text("Contacts", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onNavigateTab(AdminTab.GALLERY) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = ForestGreenDark)
                        ) {
                            Text("📸 Gallery", fontSize = 12.sp)
                        }
                        Button(
                            onClick = { onNavigateTab(AdminTab.BRANDING) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = GoldAccentDark)
                        ) {
                            Text("🎨 App Branding", fontSize = 12.sp, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    sub: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = title, color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, color = color, fontSize = 20.sp, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = sub, color = Color.DarkGray, fontSize = 10.sp)
        }
    }
}

/**
 * Product Management Content (CRUD)
 */
@Composable
fun AdminProductsContent(
    products: List<ProductEntity>,
    onAddProduct: () -> Unit,
    onEditProduct: (ProductEntity) -> Unit,
    onDeleteProduct: (ProductEntity) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🌱 Plant Inventory (${products.size})",
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenDark,
                    fontSize = 17.sp
                )
                Button(
                    onClick = onAddProduct,
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Plant", fontSize = 12.sp)
                }
            }
        }

        items(products) { plant ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = plant.name,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenDark,
                            fontSize = 14.sp
                        )
                        if (plant.gujaratiName.isNotBlank()) {
                            Text(text = plant.gujaratiName, color = FreshGreen, fontSize = 12.sp)
                        }
                        Text(
                            text = "Price: ₹${plant.price.toInt()} | Stock: ${plant.stockQuantity} | Age: ${plant.age}",
                            color = Color.DarkGray,
                            fontSize = 11.sp
                        )
                    }

                    Row {
                        IconButton(onClick = { onEditProduct(plant) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = ForestGreen)
                        }
                        IconButton(onClick = { onDeleteProduct(plant) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Product Edit Dialog
 */
@Composable
fun ProductEditDialog(
    product: ProductEntity,
    isNew: Boolean,
    onDismiss: () -> Unit,
    onSave: (ProductEntity) -> Unit
) {
    var name by remember { mutableStateOf(product.name) }
    var gujaratiName by remember { mutableStateOf(product.gujaratiName) }
    var priceStr by remember { mutableStateOf("${product.price.toInt()}") }
    var oldPriceStr by remember { mutableStateOf("${product.oldPrice.toInt()}") }
    var stockStr by remember { mutableStateOf("${product.stockQuantity}") }
    var age by remember { mutableStateOf(product.age) }
    var minOrderStr by remember { mutableStateOf("${product.minOrderQuantity}") }
    var category by remember { mutableStateOf(product.category) }
    var description by remember { mutableStateOf(product.description) }
    var benefits by remember { mutableStateOf(product.benefits) }
    var isAvailable by remember { mutableStateOf(product.isAvailable) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isNew) "Add New Plant Variety" else "Edit Plant Variety") },
        text = {
            LazyColumn(
                modifier = Modifier.height(400.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Plant Name (English)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = gujaratiName,
                        onValueChange = { gujaratiName = it },
                        label = { Text("રોપાનું નામ (ગુજરાતી)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = priceStr,
                            onValueChange = { priceStr = it },
                            label = { Text("Price (₹)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = oldPriceStr,
                            onValueChange = { oldPriceStr = it },
                            label = { Text("Old Price (₹)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = stockStr,
                            onValueChange = { stockStr = it },
                            label = { Text("Stock Qty") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = minOrderStr,
                            onValueChange = { minOrderStr = it },
                            label = { Text("Min Order") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                item {
                    OutlinedTextField(
                        value = age,
                        onValueChange = { age = it },
                        label = { Text("Age (e.g. 12 Months)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Category (Dwarf / Hybrid / Tall)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )
                }
                item {
                    OutlinedTextField(
                        value = benefits,
                        onValueChange = { benefits = it },
                        label = { Text("Key Benefits") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2
                    )
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Available for Booking")
                        Switch(
                            checked = isAvailable,
                            onCheckedChange = { isAvailable = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = FreshGreen)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val p = priceStr.toDoubleOrNull() ?: 150.0
                    val op = oldPriceStr.toDoubleOrNull() ?: p
                    val discount = if (op > p) (((op - p) / op) * 100).toInt() else 0
                    val updated = product.copy(
                        name = name,
                        gujaratiName = gujaratiName,
                        price = p,
                        oldPrice = op,
                        discountPercent = discount,
                        stockQuantity = stockStr.toIntOrNull() ?: 100,
                        minOrderQuantity = minOrderStr.toIntOrNull() ?: 5,
                        age = age,
                        category = category,
                        description = description,
                        benefits = benefits,
                        isAvailable = isAvailable
                    )
                    onSave(updated)
                },
                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

/**
 * Booking Management Content
 */
@Composable
fun AdminBookingsContent(
    bookings: List<BookingEntity>,
    onUpdateStatus: (String, String) -> Unit,
    onUpdatePayment: (String, String, Double, Double) -> Unit,
    onViewReceipt: (BookingEntity) -> Unit,
    onDeleteBooking: (BookingEntity) -> Unit
) {
    val statuses = listOf("Pending", "Order Confirmed", "Plant Ready", "Out for Delivery", "Delivered", "Cancelled")
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatusFilter by remember { mutableStateOf("All") }

    val filtered = bookings.filter {
        val matchesStatus = selectedStatusFilter == "All" || it.deliveryStatus == selectedStatusFilter
        val matchesSearch = searchQuery.isBlank() ||
                it.customerName.contains(searchQuery, ignoreCase = true) ||
                it.mobileNumber.contains(searchQuery) ||
                it.bookingId.contains(searchQuery, ignoreCase = true)
        matchesStatus && matchesSearch
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "📦 Customer Bookings Management",
                fontWeight = FontWeight.Bold,
                color = ForestGreenDark,
                fontSize = 17.sp
            )
        }

        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search by ID, name or phone") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )
        }

        items(filtered) { booking ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = booking.bookingId, fontWeight = FontWeight.Bold, color = ForestGreenDark)
                        Text(text = booking.bookingDate, color = Color.Gray, fontSize = 11.sp)
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(text = "${booking.customerName} (${booking.mobileNumber})", fontWeight = FontWeight.SemiBold)
                    Text(text = "Order: ${booking.quantity} × ${booking.plantName} = ₹${booking.totalAmount.toInt()}", fontSize = 12.sp)
                    Text(text = "Advance: ₹${booking.advanceAmount.toInt()} | Rem: ₹${booking.remainingAmount.toInt()} (${booking.paymentStatus})", fontSize = 12.sp, color = FreshGreen)
                    Text(text = "Location: ${booking.villageCity}, ${booking.district} (${booking.deliveryType})", fontSize = 11.sp, color = Color.Gray)

                    Spacer(modifier = Modifier.height(8.dp))

                    // Status Chips Selector
                    Text(text = "Update Status:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        items(statuses) { status ->
                            val isCurr = booking.deliveryStatus == status
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (isCurr) ForestGreen else CreamSurface,
                                modifier = Modifier.clickable { onUpdateStatus(booking.bookingId, status) }
                            ) {
                                Text(
                                    text = status,
                                    fontSize = 10.sp,
                                    fontWeight = if (isCurr) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isCurr) Color.White else Color.Black,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { onViewReceipt(booking) },
                            colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text("Official Receipt", fontSize = 11.sp)
                        }

                        IconButton(onClick = { onDeleteBooking(booking) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Review Moderation Content
 */
@Composable
fun AdminReviewsContent(
    reviews: List<ReviewEntity>,
    onApprove: (Long, Boolean) -> Unit,
    onDelete: (ReviewEntity) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "💬 Customer Review Moderation (${reviews.size})",
                fontWeight = FontWeight.Bold,
                color = ForestGreenDark,
                fontSize = 17.sp
            )
        }

        items(reviews) { review ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "${review.customerName} (${review.villageOrCity})", fontWeight = FontWeight.Bold)
                        Row {
                            repeat(review.rating) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                    Text(text = review.reviewText, fontSize = 12.sp, modifier = Modifier.padding(vertical = 4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (review.isApproved) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                        ) {
                            Text(
                                text = if (review.isApproved) "Approved & Visible" else "Pending / Hidden",
                                color = if (review.isApproved) FreshGreen else Color.Red,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Row {
                            OutlinedButton(
                                onClick = { onApprove(review.id, !review.isApproved) },
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(if (review.isApproved) "Hide" else "Approve", fontSize = 11.sp)
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            IconButton(onClick = { onDelete(review) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Offer Management Content
 */
@Composable
fun AdminOffersContent(
    offers: List<OfferEntity>,
    onAddOffer: () -> Unit,
    onEditOffer: (OfferEntity) -> Unit,
    onDeleteOffer: (OfferEntity) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🔥 Seasonal Offers Management",
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenDark,
                    fontSize = 17.sp
                )
                Button(
                    onClick = onAddOffer,
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("+ Add Offer", fontSize = 12.sp)
                }
            }
        }

        items(offers) { offer ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = offer.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = offer.discountText, color = Color(0xFFC62828), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text(text = offer.description, color = Color.Gray, fontSize = 11.sp)
                    }
                    Row {
                        IconButton(onClick = { onEditOffer(offer) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = ForestGreen)
                        }
                        IconButton(onClick = { onDeleteOffer(offer) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OfferEditDialog(
    offer: OfferEntity,
    isNew: Boolean,
    onDismiss: () -> Unit,
    onSave: (OfferEntity) -> Unit
) {
    var title by remember { mutableStateOf(offer.title) }
    var gujaratiTitle by remember { mutableStateOf(offer.gujaratiTitle) }
    var discountText by remember { mutableStateOf(offer.discountText) }
    var description by remember { mutableStateOf(offer.description) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isNew) "Add Special Offer" else "Edit Special Offer") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Offer Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = gujaratiTitle,
                    onValueChange = { gujaratiTitle = it },
                    label = { Text("ઓફર શીર્ષક (ગુજરાતી)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = discountText,
                    onValueChange = { discountText = it },
                    label = { Text("Badge / Discount Text") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Offer Details & Terms") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        offer.copy(
                            title = title,
                            gujaratiTitle = gujaratiTitle,
                            discountText = discountText,
                            description = description
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

/**
 * Nursery Contact & System Settings Content
 */
@Composable
fun AdminSettingsContent(
    settings: Map<String, String>,
    onSave: (String, String) -> Unit
) {
    var p1 by remember(settings) { mutableStateOf(settings["phone_1"] ?: "8200596044") }
    var p2 by remember(settings) { mutableStateOf(settings["phone_2"] ?: "9426477948") }
    var p3Name by remember(settings) { mutableStateOf(settings["phone_3_name"] ?: "Vansh Ram") }
    var p3 by remember(settings) { mutableStateOf(settings["phone_3"] ?: "9328040045") }
    var pin by remember(settings) { mutableStateOf(settings["admin_pin"] ?: "1234") }
    var address by remember(settings) { mutableStateOf(settings["nursery_address"] ?: "Dwarkesh Coconuts & Nursery, Mahuva - Somnath Highway, Gujarat") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "⚙️ Nursery Contact & Business Settings",
                fontWeight = FontWeight.Bold,
                color = ForestGreenDark,
                fontSize = 17.sp
            )
            Text(
                text = "These numbers are used for Call, WhatsApp & Official Booking Receipts across the entire app.",
                color = Color.Gray,
                fontSize = 11.sp
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = "Official Phone Numbers", fontWeight = FontWeight.Bold, color = ForestGreenDark)

                    OutlinedTextField(
                        value = p1,
                        onValueChange = { p1 = it },
                        label = { Text("Primary Phone 1") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = p2,
                        onValueChange = { p2 = it },
                        label = { Text("Secondary Phone 2") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = p3Name,
                        onValueChange = { p3Name = it },
                        label = { Text("Key Person Name (e.g. Vansh Ram)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = p3,
                        onValueChange = { p3 = it },
                        label = { Text("Key Person Phone / WhatsApp") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            onSave("phone_1", p1)
                            onSave("phone_2", p2)
                            onSave("phone_3_name", p3Name)
                            onSave("phone_3", p3)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save Phone Numbers")
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = "Security PIN & Nursery Address", fontWeight = FontWeight.Bold, color = ForestGreenDark)

                    OutlinedTextField(
                        value = pin,
                        onValueChange = { pin = it },
                        label = { Text("Admin 4-digit Security PIN") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Nursery Physical Address") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2
                    )

                    Button(
                        onClick = {
                            onSave("admin_pin", pin)
                            onSave("nursery_address", address)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save Security & Address")
                    }
                }
            }
        }
    }
}
