package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BookingEntity
import com.example.data.ProductEntity
import com.example.ui.AppLanguage
import com.example.ui.AppText
import com.example.ui.components.DwarkeshLogoView
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
fun BookingScreen(
    initialPlant: ProductEntity?,
    viewModel: MainViewModel,
    onBackClick: () -> Unit,
    onBookingSuccess: (BookingEntity) -> Unit
) {
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()
    val context = LocalContext.current

    var selectedPlant by remember { mutableStateOf(initialPlant ?: allProducts.firstOrNull()) }
    var customerName by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var quantity by remember { mutableIntStateOf(selectedPlant?.minOrderQuantity ?: 10) }
    var customAdvance by remember { mutableStateOf("") }
    var deliveryType by remember { mutableStateOf("Home Delivery") }
    var address by remember { mutableStateOf("") }
    var villageCity by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("Gir Somnath") }
    var notes by remember { mutableStateOf("") }

    val unitPrice = selectedPlant?.price ?: 150.0
    val totalAmount = unitPrice * quantity

    // Suggested minimum advance (25%) or custom input
    val defaultAdvance = (totalAmount * 0.25).toInt().toDouble()
    val parsedAdvance = customAdvance.toDoubleOrNull() ?: defaultAdvance
    val advanceAmount = parsedAdvance.coerceIn(0.0, totalAmount)
    val remainingAmount = (totalAmount - advanceAmount).coerceAtLeast(0.0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "🌱 " + AppText("advance_booking"),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
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
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(CreamBackground)
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Selected Plant Summary Header
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DwarkeshLogoView(
                            size = 56.dp,
                            transparentBackground = true
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = selectedPlant?.name ?: "Select Plant",
                                fontWeight = FontWeight.Bold,
                                color = ForestGreenDark,
                                fontSize = 15.sp
                            )
                            if (selectedPlant?.gujaratiName?.isNotBlank() == true) {
                                Text(
                                    text = selectedPlant!!.gujaratiName,
                                    color = FreshGreen,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Text(
                                text = "Rate: ₹${unitPrice.toInt()} per sapling",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // Customer Details Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "👤 Farmer / Customer Information",
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenDark,
                            fontSize = 15.sp
                        )

                        OutlinedTextField(
                            value = customerName,
                            onValueChange = { customerName = it },
                            label = { Text(AppText("customer_name")) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("booking_customer_name"),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = mobileNumber,
                            onValueChange = { if (it.length <= 10) mobileNumber = it },
                            label = { Text(AppText("mobile_number") + " (10 Digits)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("booking_mobile_number"),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )
                    }
                }
            }

            // Quantity & Live Price Calculator
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "🔢 Quantity & Live Calculations",
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenDark,
                            fontSize = 15.sp
                        )

                        // Quantity Counter
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${AppText("quantity")}:",
                                fontWeight = FontWeight.SemiBold,
                                color = Color.DarkGray
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = {
                                        val min = selectedPlant?.minOrderQuantity ?: 5
                                        if (quantity > min) quantity -= 5
                                    },
                                    modifier = Modifier
                                        .background(CreamSurface, RoundedCornerShape(8.dp))
                                        .size(36.dp)
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = "Decrease")
                                }

                                Text(
                                    text = "$quantity",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 18.sp,
                                    color = ForestGreenDark,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )

                                IconButton(
                                    onClick = { quantity += 5 },
                                    modifier = Modifier
                                        .background(CreamSurface, RoundedCornerShape(8.dp))
                                        .size(36.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Increase")
                                }
                            }
                        }

                        Text(
                            text = "Min order requirement: ${selectedPlant?.minOrderQuantity ?: 5} plants",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Advance Amount Input
                        OutlinedTextField(
                            value = customAdvance,
                            onValueChange = { customAdvance = it },
                            label = { Text("${AppText("advance_amount")} (Default 25%: ₹${defaultAdvance.toInt()})") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("booking_advance_input"),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )

                        // Calculation Breakdown Box
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = CreamSurface,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Total ($quantity × ₹${unitPrice.toInt()}):", fontSize = 13.sp)
                                    Text(text = "₹${totalAmount.toInt()}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = AppText("advance_amount") + ":", color = FreshGreen, fontSize = 13.sp)
                                    Text(text = "₹${advanceAmount.toInt()}", color = FreshGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                                androidx.compose.material3.HorizontalDivider(color = Color.LightGray)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = AppText("remaining_amount") + ":", fontWeight = FontWeight.Bold, color = ForestGreenDark, fontSize = 14.sp)
                                    Text(text = "₹${remainingAmount.toInt()}", fontWeight = FontWeight.Black, color = ForestGreenDark, fontSize = 16.sp)
                                }
                            }
                        }
                    }
                }
            }

            // Delivery Option & Address
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "🚚 Delivery Preferences",
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenDark,
                            fontSize = 15.sp
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = deliveryType == "Home Delivery",
                                onClick = { deliveryType = "Home Delivery" },
                                colors = RadioButtonDefaults.colors(selectedColor = ForestGreen)
                            )
                            Text(
                                text = AppText("home_delivery"),
                                modifier = Modifier.clickable { deliveryType = "Home Delivery" },
                                fontSize = 13.sp
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            RadioButton(
                                selected = deliveryType == "Nursery Pickup",
                                onClick = { deliveryType = "Nursery Pickup" },
                                colors = RadioButtonDefaults.colors(selectedColor = ForestGreen)
                            )
                            Text(
                                text = AppText("nursery_pickup"),
                                modifier = Modifier.clickable { deliveryType = "Nursery Pickup" },
                                fontSize = 13.sp
                            )
                        }

                        OutlinedTextField(
                            value = villageCity,
                            onValueChange = { villageCity = it },
                            label = { Text(AppText("village_city") + " / ગામ") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = district,
                            onValueChange = { district = it },
                            label = { Text(AppText("district") + " / જિલ્લો") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = address,
                            onValueChange = { address = it },
                            label = { Text("Farm Plot / Land Details / સરનામું") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            maxLines = 2
                        )

                        OutlinedTextField(
                            value = notes,
                            onValueChange = { notes = it },
                            label = { Text(AppText("notes") + " (Optional)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            maxLines = 2
                        )
                    }
                }
            }

            // Submit Button
            item {
                Button(
                    onClick = {
                        if (customerName.isBlank()) {
                            Toast.makeText(context, "Please enter Customer Name", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (mobileNumber.length < 10) {
                            Toast.makeText(context, "Please enter valid 10-digit mobile number", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        val plantToBook = selectedPlant
                        if (plantToBook == null) {
                            Toast.makeText(context, "Please select a plant", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        viewModel.createBooking(
                            customerName = customerName,
                            mobileNumber = mobileNumber,
                            plant = plantToBook,
                            quantity = quantity,
                            advanceAmount = advanceAmount,
                            deliveryType = deliveryType,
                            address = address,
                            villageCity = villageCity,
                            district = district,
                            notes = notes,
                            onSuccess = { booking ->
                                Toast.makeText(context, "Advance Booking Confirmed: ${booking.bookingId}", Toast.LENGTH_LONG).show()
                                onBookingSuccess(booking)
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("confirm_booking_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "✅ " + AppText("confirm_booking"),
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}
