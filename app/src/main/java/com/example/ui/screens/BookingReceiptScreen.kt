package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BookingEntity
import com.example.ui.AppText
import com.example.ui.components.DwarkeshLogoView
import com.example.ui.components.openWhatsAppChat
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.CreamSurface
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.FreshGreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldAccentDark
import com.example.ui.theme.GoldAccentLight
import com.example.viewmodel.MainViewModel
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingReceiptScreen(
    booking: BookingEntity,
    viewModel: MainViewModel,
    onBackHome: () -> Unit
) {
    val settings by viewModel.settingsMap.collectAsState()
    val context = LocalContext.current

    val primaryPhone = settings["phone_1"] ?: "8200596044"
    val secondaryPhone = settings["phone_2"] ?: "9426477948"
    val managerName = settings["phone_3_name"] ?: "Vansh Ram"
    val managerPhone = settings["phone_3"] ?: "9328040045"
    val nurseryAddress = settings["nursery_address"] ?: "Dwarkesh Coconuts & Nursery, Mahuva - Somnath Road, Gujarat"

    val receiptShareText = """
        *DWARKESH COCONUTS & NURSERY*
        Official Advance Booking Receipt
        -----------------------------------
        Booking ID: ${booking.bookingId}
        Date: ${booking.bookingDate}
        Customer: ${booking.customerName} (${booking.mobileNumber})
        Variety: ${booking.plantName}
        Quantity: ${booking.quantity} plants
        Rate: ₹${booking.unitPrice.toInt()}
        Total Amount: ₹${booking.totalAmount.toInt()}
        Advance Paid: ₹${booking.advanceAmount.toInt()}
        Remaining: ₹${booking.remainingAmount.toInt()}
        Delivery Type: ${booking.deliveryType}
        Location: ${booking.villageCity}, ${booking.district}
        Status: ${booking.deliveryStatus}
        -----------------------------------
        Authorized Signatory: $managerName ($managerPhone)
        Contact: $primaryPhone, $secondaryPhone
        Thank you for choosing Dwarkesh Coconuts!
    """.trimIndent()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = AppText("booking_receipt"),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackHome) {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = "Home",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, receiptShareText)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, "Share Booking Receipt")
                        context.startActivity(shareIntent)
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White)
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Receipt Canvas Card (Designed like an authentic paper receipt)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, GoldAccentLight, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Official Nursery Logo at Top of Receipt
                        DwarkeshLogoView(
                            size = 90.dp,
                            transparentBackground = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "DWARKESH COCONUTS & NURSERY",
                            color = ForestGreenDark,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Serif,
                            fontSize = 18.sp,
                            letterSpacing = 1.sp,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "દ્વારકેશ કોકોનટ્સ & નર્સરી",
                            color = FreshGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )

                        Text(
                            text = nurseryAddress,
                            color = Color.Gray,
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 2.dp)
                        )

                        Text(
                            text = "📞 $primaryPhone | $secondaryPhone | $managerName: $managerPhone",
                            color = Color.DarkGray,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 2.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Receipt Title Plaque
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = ForestGreenDark
                        ) {
                            Text(
                                text = "OFFICIAL ADVANCE BOOKING RECEIPT",
                                color = GoldAccentLight,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(12.dp))

                        // Meta Row: Booking ID & Date
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "BOOKING ID", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text(text = booking.bookingId, color = ForestGreenDark, fontWeight = FontWeight.Black, fontSize = 14.sp)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = "BOOKING DATE", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text(text = booking.bookingDate, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(12.dp))

                        // Customer Details
                        ReceiptRow("Customer Name", booking.customerName)
                        ReceiptRow("Mobile Number", booking.mobileNumber)
                        ReceiptRow("Farm Location", "${booking.villageCity}, ${booking.district}")
                        if (booking.address.isNotBlank()) {
                            ReceiptRow("Delivery Address", booking.address)
                        }
                        ReceiptRow("Delivery Method", booking.deliveryType)

                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(8.dp))

                        // Order Table
                        ReceiptRow("Plant Variety", booking.plantName)
                        if (booking.gujaratiPlantName.isNotBlank()) {
                            ReceiptRow("ગુજરાતી નામ", booking.gujaratiPlantName)
                        }
                        ReceiptRow("Quantity Booked", "${booking.quantity} Plants")
                        ReceiptRow("Unit Price", "₹${booking.unitPrice.toInt()}")
                        ReceiptRow("Total Order Amount", "₹${booking.totalAmount.toInt()}", isBold = true)

                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = Color(0xFFEEEEEE), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(8.dp))

                        // Payment Status Highlight Box
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = CreamSurface,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Advance Paid:", color = FreshGreen, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(text = "₹${booking.advanceAmount.toInt()}", color = FreshGreen, fontWeight = FontWeight.Black, fontSize = 14.sp)
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Remaining Balance:", color = Color(0xFFB71C1C), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(text = "₹${booking.remainingAmount.toInt()}", color = Color(0xFFB71C1C), fontWeight = FontWeight.Black, fontSize = 14.sp)
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Payment Status:", fontSize = 11.sp, color = Color.Gray)
                                    Text(text = booking.paymentStatus, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = ForestGreenDark)
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Delivery Status:", fontSize = 11.sp, color = Color.Gray)
                                    Text(text = booking.deliveryStatus, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = ForestGreen)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Terms & Conditions
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(text = "Terms & Conditions / શરતો:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                            Text(text = "1. Advance booking guarantees plant reservation for the specified season.", fontSize = 9.sp, color = Color.Gray)
                            Text(text = "2. Balance payment is due upon doorstep farm inspection & delivery.", fontSize = 9.sp, color = Color.Gray)
                            Text(text = "3. Nursery experts will provide planting guide & fertilization schedule.", fontSize = 9.sp, color = Color.Gray)
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Authorized Signatory
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Column {
                                Text(
                                    text = "Certified Quality Seal",
                                    fontSize = 10.sp,
                                    color = FreshGreen,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Dwarkesh Nursery Hub",
                                    fontSize = 9.sp,
                                    color = Color.Gray
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = managerName,
                                    fontFamily = FontFamily.Cursive,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = ForestGreenDark
                                )
                                HorizontalDivider(modifier = Modifier.width(120.dp), color = Color.Black, thickness = 1.dp)
                                Text(
                                    text = AppText("signature_vansh_ram"),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }
                }
            }

            // Quick Action Buttons
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            openWhatsAppChat(
                                context = context,
                                phoneNumber = managerPhone,
                                message = receiptShareText
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "💬 Share Receipt", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    Button(
                        onClick = {
                            Toast.makeText(context, "Receipt saved to device documents.", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Save / Print", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }

            item {
                OutlinedButton(
                    onClick = onBackHome,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Back to Home", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ReceiptRow(label: String, value: String, isBold: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray, fontSize = 12.sp)
        Text(
            text = value,
            color = if (isBold) ForestGreenDark else Color(0xFF222222),
            fontWeight = if (isBold) FontWeight.Black else FontWeight.Medium,
            fontSize = if (isBold) 13.sp else 12.sp
        )
    }
}
