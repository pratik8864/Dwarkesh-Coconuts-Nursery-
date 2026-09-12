package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.Collections
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalFlorist
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppLanguage
import com.example.ui.AppText
import com.example.ui.LocalAppLanguage
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.CreamSurface
import com.example.ui.theme.DarkGreenBackground
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.FreshGreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldAccentDark
import com.example.ui.theme.GoldAccentLight

/**
 * Top App Bar with Logo, Business Name, Language Toggle, and Notification Bell
 */
@Composable
fun DwarkeshTopBar(
    currentLanguage: AppLanguage,
    onLanguageToggle: () -> Unit,
    notificationCount: Int = 0,
    onNotificationClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp),
        color = ForestGreenDark
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DwarkeshHeaderBrand(
                logoSize = 46.dp,
                textColor = Color.White
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Language Switcher: ગુજરાતી | English
                LanguageSwitcher(
                    currentLanguage = currentLanguage,
                    onToggle = onLanguageToggle
                )

                // Notification Icon with Badge
                IconButton(
                    onClick = onNotificationClick,
                    modifier = Modifier.testTag("notification_button")
                ) {
                    BadgedBox(
                        badge = {
                            if (notificationCount > 0) {
                                Badge(
                                    containerColor = GoldAccent,
                                    contentColor = ForestGreenDark
                                ) {
                                    Text(
                                        text = "$notificationCount",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Language Switcher: ગુજરાતી | English
 */
@Composable
fun LanguageSwitcher(
    currentLanguage: AppLanguage,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color(0x33FFFFFF),
        border = androidx.compose.foundation.BorderStroke(1.dp, GoldAccentLight.copy(alpha = 0.6f)),
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onToggle)
            .testTag("language_toggle_button")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "ગુજરાતી",
                fontSize = 12.sp,
                fontWeight = if (currentLanguage == AppLanguage.GUJARATI) FontWeight.Black else FontWeight.Normal,
                color = if (currentLanguage == AppLanguage.GUJARATI) GoldAccentLight else Color.White.copy(alpha = 0.7f)
            )
            Text(
                text = "|",
                fontSize = 12.sp,
                color = GoldAccentLight.copy(alpha = 0.5f)
            )
            Text(
                text = "English",
                fontSize = 12.sp,
                fontWeight = if (currentLanguage == AppLanguage.ENGLISH) FontWeight.Black else FontWeight.Normal,
                color = if (currentLanguage == AppLanguage.ENGLISH) GoldAccentLight else Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

/**
 * Intent helpers for Call and WhatsApp
 */
fun dialPhoneNumber(context: Context, phoneNumber: String) {
    try {
        val cleanNumber = phoneNumber.replace("[^0-9+]".toRegex(), "")
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$cleanNumber")
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Could not open dialer: $phoneNumber", Toast.LENGTH_SHORT).show()
    }
}

fun openWhatsAppChat(context: Context, phoneNumber: String, message: String) {
    try {
        val cleanNumber = phoneNumber.replace("[^0-9]".toRegex(), "")
        val fullNumber = if (cleanNumber.length == 10) "91$cleanNumber" else cleanNumber
        val uri = Uri.parse("https://api.whatsapp.com/send?phone=$fullNumber&text=${Uri.encode(message)}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Could not open WhatsApp", Toast.LENGTH_SHORT).show()
    }
}

/**
 * Modern Quick Contact Action Bar (Call Now & WhatsApp)
 */
@Composable
fun QuickContactButtons(
    primaryPhone: String = "8200596044",
    whatsappPhone: String = "9328040045",
    inquiryText: String = "Hello 👋, I want to enquire about coconut plants from Dwarkesh Coconuts & Nursery.",
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Button(
            onClick = { dialPhoneNumber(context, primaryPhone) },
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .testTag("call_now_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = ForestGreen
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Call,
                contentDescription = "Call",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = AppText("call_now"),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Button(
            onClick = { openWhatsAppChat(context, whatsappPhone, inquiryText) },
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .testTag("whatsapp_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF25D366) // Official WhatsApp Green
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "💬 " + AppText("whatsapp"),
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}

/**
 * Mobile Bottom Navigation
 */
enum class AppTab {
    HOME,
    PLANTS,
    BOOKINGS,
    GALLERY,
    ADMIN
}

@Composable
fun DwarkeshBottomNavigation(
    selectedTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.navigationBarsPadding(),
        containerColor = ForestGreenDark,
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            Triple(AppTab.HOME, AppText("nav_home"), Pair(Icons.Filled.Home, Icons.Outlined.Home)),
            Triple(AppTab.PLANTS, AppText("nav_plants"), Pair(Icons.Filled.LocalFlorist, Icons.Outlined.LocalFlorist)),
            Triple(AppTab.BOOKINGS, AppText("nav_bookings"), Pair(Icons.Filled.ReceiptLong, Icons.Outlined.ReceiptLong)),
            Triple(AppTab.GALLERY, AppText("nav_gallery"), Pair(Icons.Filled.Collections, Icons.Outlined.Collections)),
            Triple(AppTab.ADMIN, AppText("nav_admin"), Pair(Icons.Filled.AdminPanelSettings, Icons.Outlined.AdminPanelSettings))
        )

        items.forEach { (tab, label, icons) ->
            val isSelected = selectedTab == tab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) icons.first else icons.second,
                        contentDescription = label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = ForestGreenDark,
                    selectedTextColor = GoldAccentLight,
                    indicatorColor = GoldAccentLight,
                    unselectedIconColor = Color.White.copy(alpha = 0.7f),
                    unselectedTextColor = Color.White.copy(alpha = 0.7f)
                ),
                modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
            )
        }
    }
}

/**
 * Comprehensive App Footer
 */
@Composable
fun DwarkeshFooter(
    primaryPhone: String = "8200596044",
    secondaryPhone: String = "9426477948",
    managerName: String = "Vansh Ram",
    managerPhone: String = "9328040045",
    onAdminClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color(0xFF061A0F)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DwarkeshLogoView(
                size = 90.dp,
                transparentBackground = true
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "DWARKESH COCONUTS & NURSERY",
                color = GoldAccentLight,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Serif,
                fontSize = 17.sp,
                letterSpacing = 1.1.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Quality Plants • Healthy Growth • Trusted Service",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Contact Numbers Box
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0D2919)),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "📞 Contact Numbers / સંપર્ક નંબરો:",
                        color = GoldAccentLight,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "1. $primaryPhone", color = Color.White, fontSize = 13.sp)
                        IconButton(onClick = { dialPhoneNumber(context, primaryPhone) }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Call, contentDescription = "Call", tint = FreshGreen, modifier = Modifier.size(18.dp))
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "2. $secondaryPhone", color = Color.White, fontSize = 13.sp)
                        IconButton(onClick = { dialPhoneNumber(context, secondaryPhone) }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Call, contentDescription = "Call", tint = FreshGreen, modifier = Modifier.size(18.dp))
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "3. $managerName: $managerPhone", color = GoldAccentLight, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        Row {
                            IconButton(onClick = { dialPhoneNumber(context, managerPhone) }, modifier = Modifier.size(32.dp)) {
                                Icon(Icons.Default.Call, contentDescription = "Call", tint = FreshGreen, modifier = Modifier.size(18.dp))
                            }
                            IconButton(
                                onClick = {
                                    openWhatsAppChat(context, managerPhone, "Hello $managerName bhai, I want to book coconut plants.")
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Text("💬", fontSize = 14.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Call & WhatsApp Action Buttons in Footer
            QuickContactButtons(
                primaryPhone = primaryPhone,
                whatsappPhone = managerPhone
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Admin Panel Shortcut Link
            Text(
                text = "🔐 " + AppText("admin_panel"),
                color = GoldAccent.copy(alpha = 0.7f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable(onClick = onAdminClick)
                    .padding(8.dp)
                    .testTag("footer_admin_link")
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = AppText("copyright"),
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
