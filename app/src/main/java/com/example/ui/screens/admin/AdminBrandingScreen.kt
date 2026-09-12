package com.example.ui.screens.admin

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.storage.ImageStorageManager
import com.example.ui.components.DwarkeshHeaderBrand
import com.example.ui.components.DwarkeshLogoView
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldAccentDark
import com.example.ui.theme.GoldAccentLight
import com.example.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Admin App Branding Management Screen
 * Manages Dynamic App Logo, App Icon, App Name & Tagline.
 * Provides live previews, file upload from device, reset actions,
 * and transparent platform information regarding Android APK compile-time icons vs runtime dynamic branding.
 */
@Composable
fun AdminBrandingManagementContent(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val brandingConfig by viewModel.brandingConfig.collectAsState()
    val feedback by viewModel.adminFeedback.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var selectedSubTab by remember { mutableIntStateOf(0) }
    val subTabs = listOf("App Logo", "App Icon", "Business Info")

    // State for Logo Editing
    var tempLogoUrl by remember(brandingConfig.logoUrl) { mutableStateOf(brandingConfig.logoUrl) }
    var isUploadingLogo by remember { mutableStateOf(false) }

    // State for Icon Editing
    var tempIconUrl by remember(brandingConfig.appIconUrl) { mutableStateOf(brandingConfig.appIconUrl) }
    var isUploadingIcon by remember { mutableStateOf(false) }

    // State for Business Info Editing
    var appName by remember(brandingConfig.appName) { mutableStateOf(brandingConfig.appName) }
    var tagline by remember(brandingConfig.tagline) { mutableStateOf(brandingConfig.tagline) }

    // Device File Pickers
    val logoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            isUploadingLogo = true
            coroutineScope.launch {
                val result = withContext(Dispatchers.IO) {
                    ImageStorageManager.saveImageFromUri(context, uri, subDir = "branding")
                }
                isUploadingLogo = false
                result.fold(
                    onSuccess = { savedPath ->
                        tempLogoUrl = savedPath
                        Toast.makeText(context, "Logo file selected! Click 'Save' to apply.", Toast.LENGTH_SHORT).show()
                    },
                    onFailure = { error ->
                        Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_LONG).show()
                    }
                )
            }
        }
    }

    val iconPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            isUploadingIcon = true
            coroutineScope.launch {
                val result = withContext(Dispatchers.IO) {
                    ImageStorageManager.saveImageFromUri(context, uri, subDir = "branding")
                }
                isUploadingIcon = false
                result.fold(
                    onSuccess = { savedPath ->
                        tempIconUrl = savedPath
                        Toast.makeText(context, "App icon selected! Click 'Save' to apply.", Toast.LENGTH_SHORT).show()
                    },
                    onFailure = { error ->
                        Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_LONG).show()
                    }
                )
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
            .padding(16.dp)
            .testTag("admin_branding_management"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = ForestGreenDark.copy(alpha = 0.1f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Palette, contentDescription = null, tint = ForestGreen)
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "App Branding Management",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = ForestGreenDark
                            )
                            Text(
                                text = "Customize Logo, App Icon & Identity dynamically",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    // Operation Feedback Banner
                    if (feedback != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (feedback!!.isError) Color(0xFFFFEBEE) else Color(0xFFE8F5E9),
                            border = BorderStroke(1.dp, if (feedback!!.isError) Color(0xFFEF9A9A) else Color(0xFFA5D6A7)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = (if (feedback!!.isError) "⚠️ " else "✅ ") + feedback!!.message,
                                    color = if (feedback!!.isError) Color(0xFFC62828) else Color(0xFF2E7D32),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    onClick = { viewModel.clearAdminFeedback() },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = Color.Gray, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Sub Tabs
                    TabRow(
                        selectedTabIndex = selectedSubTab,
                        containerColor = Color(0xFFF5F5F5),
                        contentColor = ForestGreenDark,
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedSubTab]),
                                color = ForestGreen
                            )
                        },
                        modifier = Modifier.clip(RoundedCornerShape(10.dp))
                    ) {
                        subTabs.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedSubTab == index,
                                onClick = { selectedSubTab = index },
                                text = {
                                    Text(
                                        text = title,
                                        fontWeight = if (selectedSubTab == index) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 13.sp
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }

        // Tab Content 0: App Logo Management
        if (selectedSubTab == 0) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Main App Logo",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = ForestGreenDark
                        )
                        Text(
                            text = "Displayed on Home headers, Splash Screen, navigation bars, and receipts.",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Live Preview Box (with Dark & Light contrasting backgrounds)
                        Text(text = "Live Preview (Header Simulation)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
                        Spacer(modifier = Modifier.height(6.dp))

                        // Simulation on Forest Green Header
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = ForestGreenDark,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                DwarkeshHeaderBrand(
                                    customLogoUrl = tempLogoUrl,
                                    businessTitle = appName,
                                    logoSize = 48.dp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Simulation on White Card
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF8F9FA),
                            border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                DwarkeshLogoView(
                                    size = 80.dp,
                                    customLogoUrl = tempLogoUrl,
                                    showGlow = true
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (tempLogoUrl.isEmpty()) "Status: Using Default Bundled Official Emblem" else "Status: Custom Uploaded Logo Active",
                            fontSize = 11.sp,
                            color = if (tempLogoUrl.isEmpty()) Color.Gray else ForestGreen,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // File Picker Button
                        Button(
                            onClick = { logoPickerLauncher.launch("image/*") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = ForestGreenDark),
                            shape = RoundedCornerShape(10.dp),
                            enabled = !isUploadingLogo
                        ) {
                            if (isUploadingLogo) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp))
                            } else {
                                Icon(Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Upload New Logo From Device", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Or Custom URL Input
                        OutlinedTextField(
                            value = tempLogoUrl,
                            onValueChange = { tempLogoUrl = it },
                            label = { Text("Or Enter Logo Image URL / Path") },
                            placeholder = { Text("https://... or file://...") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ForestGreen),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Action Buttons: Save & Reset
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    viewModel.resetAppLogo { success, msg ->
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                        if (success) tempLogoUrl = ""
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Reset Default", fontSize = 12.sp)
                            }

                            Button(
                                onClick = {
                                    viewModel.updateAppLogo(tempLogoUrl) { success, msg ->
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                                modifier = Modifier.weight(1.2f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Save & Apply Logo", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        // Tab Content 1: App Icon Management
        if (selectedSubTab == 1) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "App Icon Management",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = ForestGreenDark
                        )
                        Text(
                            text = "Manage in-app launcher avatar, notification icons, and PWA identity.",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Android Native Architecture Note Card
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFFFF8E1),
                            border = BorderStroke(1.dp, Color(0xFFFFE082)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = GoldAccentDark, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Android Architecture Note",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = GoldAccentDark
                                    )
                                    Text(
                                        text = "In native Android, the device home screen launcher icon is embedded in the APK at build time. Icons updated here immediately take effect across all dynamic in-app surfaces (headers, splash, orders, receipts, dialogs) and the master image is preserved for subsequent builds.",
                                        fontSize = 11.sp,
                                        color = Color(0xFF5D4037)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Mask Previews
                        Text(text = "Icon Shape Previews", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Circular Preview
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(CircleShape)
                                        .background(ForestGreenDark)
                                        .border(2.dp, GoldAccentLight, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (tempIconUrl.isNotEmpty()) {
                                        AsyncImage(
                                            model = tempIconUrl,
                                            contentDescription = "App Icon",
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    } else {
                                        DwarkeshLogoView(size = 54.dp, transparentBackground = true)
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Circle Mask", fontSize = 11.sp, color = Color.Gray)
                            }

                            // Squircle Preview
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(ForestGreenDark)
                                        .border(2.dp, GoldAccentLight, RoundedCornerShape(16.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (tempIconUrl.isNotEmpty()) {
                                        AsyncImage(
                                            model = tempIconUrl,
                                            contentDescription = "App Icon",
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    } else {
                                        DwarkeshLogoView(size = 54.dp, transparentBackground = true)
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Squircle Mask", fontSize = 11.sp, color = Color.Gray)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Upload Icon Button
                        Button(
                            onClick = { iconPickerLauncher.launch("image/*") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = ForestGreenDark),
                            shape = RoundedCornerShape(10.dp),
                            enabled = !isUploadingIcon
                        ) {
                            if (isUploadingIcon) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp))
                            } else {
                                Icon(Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Upload App Icon From Device", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // URL input
                        OutlinedTextField(
                            value = tempIconUrl,
                            onValueChange = { tempIconUrl = it },
                            label = { Text("Or App Icon URL / Path") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ForestGreen),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Actions
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    viewModel.resetAppIcon { success, msg ->
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                        if (success) tempIconUrl = ""
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Reset Default", fontSize = 12.sp)
                            }

                            Button(
                                onClick = {
                                    viewModel.updateAppIcon(tempIconUrl) { success, msg ->
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                                modifier = Modifier.weight(1.2f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Save & Apply Icon", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        // Tab Content 2: Business Info & Title
        if (selectedSubTab == 2) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Business Name & Tagline",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = ForestGreenDark
                        )
                        Text(
                            text = "Updates the displayed nursery brand name across top bars and cards.",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = appName,
                            onValueChange = { appName = it },
                            label = { Text("Nursery / App Title") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ForestGreen),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = tagline,
                            onValueChange = { tagline = it },
                            label = { Text("Brand Tagline") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ForestGreen),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                viewModel.updateBrandingDetails(appName, tagline) { success, msg ->
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Save Business Details", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}
