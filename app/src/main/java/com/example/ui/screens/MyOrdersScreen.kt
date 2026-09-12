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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BookingEntity
import com.example.ui.AppText
import com.example.ui.components.DwarkeshFooter
import com.example.ui.components.DwarkeshLogoView
import com.example.ui.components.openWhatsAppChat
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.CreamSurface
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.FreshGreen
import com.example.ui.theme.GoldAccent
import com.example.viewmodel.MainViewModel

@Composable
fun MyOrdersScreen(
    viewModel: MainViewModel,
    onViewReceipt: (BookingEntity) -> Unit,
    onAdminClick: () -> Unit
) {
    val bookings by viewModel.allBookings.collectAsState()
    val settings by viewModel.settingsMap.collectAsState()
    val context = LocalContext.current

    val managerPhone = settings["phone_3"] ?: "9328040045"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
            .testTag("my_orders_screen"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ForestGreenDark)
                    .padding(20.dp)
            ) {
                Text(
                    text = "📦 " + AppText("my_orders"),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = "Real-time tracking of sapling reservations & farm deliveries",
                    color = GoldAccent,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        if (bookings.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        DwarkeshLogoView(size = 80.dp, transparentBackground = true)
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "No bookings yet",
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenDark,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Explore our coconut varieties and place an advance booking.",
                            color = Color.Gray,
                            fontSize = 12.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(bookings) { booking ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onViewReceipt(booking) },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = booking.bookingId,
                                    fontWeight = FontWeight.Black,
                                    color = ForestGreenDark,
                                    fontSize = 14.sp
                                )
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = when (booking.deliveryStatus) {
                                        "Delivered" -> Color(0xFFE8F5E9)
                                        "Out for Delivery" -> Color(0xFFFFF3E0)
                                        else -> CreamSurface
                                    }
                                ) {
                                    Text(
                                        text = booking.deliveryStatus,
                                        color = when (booking.deliveryStatus) {
                                            "Delivered" -> FreshGreen
                                            "Out for Delivery" -> Color(0xFFE65100)
                                            else -> ForestGreen
                                        },
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = booking.plantName,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                fontSize = 15.sp
                            )
                            if (booking.gujaratiPlantName.isNotBlank()) {
                                Text(
                                    text = booking.gujaratiPlantName,
                                    color = FreshGreen,
                                    fontSize = 12.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Quantity: ${booking.quantity} plants • Booked: ${booking.bookingDate}",
                                fontSize = 12.sp,
                                color = Color.DarkGray
                            )

                            Text(
                                text = "Total: ₹${booking.totalAmount.toInt()} | Advance: ₹${booking.advanceAmount.toInt()} | Remaining: ₹${booking.remainingAmount.toInt()}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ForestGreenDark,
                                modifier = Modifier.padding(top = 2.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { onViewReceipt(booking) },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "View Receipt", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = {
                                        openWhatsAppChat(
                                            context = context,
                                            phoneNumber = managerPhone,
                                            message = "Hello Vansh Ram bhai, I would like an update on my booking ${booking.bookingId} (${booking.plantName} x ${booking.quantity})."
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("💬 WhatsApp", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
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
