package com.example.ui.screens.admin

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GalleryItemEntity
import com.example.data.storage.ImageStorageManager
import com.example.ui.components.PlantVisualCard
import com.example.ui.theme.CreamBackground
import com.example.ui.theme.ForestGreen
import com.example.ui.theme.ForestGreenDark
import com.example.ui.theme.FreshGreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldAccentDark
import com.example.ui.theme.GoldAccentLight
import com.example.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Admin Panel - Gallery Management Component
 * Features: Upload new images, preview before saving, replace, delete with confirmation,
 * reorder, enable/disable status, grid/list view modes, and real-time validation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminGalleryManagementContent(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val allItems by viewModel.allGalleryItems.collectAsState()
    val feedback by viewModel.adminFeedback.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var isGridView by remember { mutableStateOf(false) }
    var selectedCategoryFilter by remember { mutableStateOf("All") }
    var isAddingItem by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<GalleryItemEntity?>(null) }
    var deletingItem by remember { mutableStateOf<GalleryItemEntity?>(null) }
    var previewingItem by remember { mutableStateOf<GalleryItemEntity?>(null) }

    val categories = listOf("All", "Nursery", "Coconut Plants", "Plant Delivery", "Plantation", "Customers", "Farm", "Videos")
    val filteredItems = if (selectedCategoryFilter == "All") {
        allItems.sortedBy { it.displayOrder }
    } else {
        allItems.filter { it.category.equals(selectedCategoryFilter, ignoreCase = true) }.sortedBy { it.displayOrder }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CreamBackground)
            .padding(16.dp)
            .testTag("admin_gallery_management")
    ) {
        // Top Action Bar & Stats
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Gallery Management",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = ForestGreenDark
                        )
                        Text(
                            text = "${allItems.size} Total • ${allItems.count { it.isActive }} Active",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { isGridView = !isGridView },
                            modifier = Modifier.testTag("toggle_gallery_view")
                        ) {
                            Icon(
                                if (isGridView) Icons.Default.ViewList else Icons.Default.GridView,
                                contentDescription = "Toggle View",
                                tint = ForestGreen
                            )
                        }

                        Button(
                            onClick = { isAddingItem = true },
                            colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("admin_add_gallery_btn")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Photo", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }

                // Feedback Banner
                if (feedback != null) {
                    Spacer(modifier = Modifier.height(10.dp))
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
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Category Filter Chips
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { cat ->
                val isSelected = selectedCategoryFilter == cat
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) ForestGreen else Color.White,
                    border = BorderStroke(1.dp, if (isSelected) ForestGreen else Color.LightGray),
                    modifier = Modifier.clickable { selectedCategoryFilter = cat }
                ) {
                    Text(
                        text = cat,
                        color = if (isSelected) Color.White else ForestGreenDark,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Gallery Items List / Grid
        if (filteredItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📸", fontSize = 40.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No gallery images found in '$selectedCategoryFilter'.", color = Color.Gray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(onClick = { isAddingItem = true }) {
                        Text("Upload First Image")
                    }
                }
            }
        } else if (isGridView) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredItems, key = { it.id }) { item ->
                    GalleryGridCard(
                        item = item,
                        onPreview = { previewingItem = item },
                        onEdit = { editingItem = item },
                        onDelete = { deletingItem = item },
                        onToggleActive = { viewModel.toggleGalleryItemActive(item.id, it) },
                        onMoveUp = { viewModel.reorderGalleryItem(item.id, (item.displayOrder - 1).coerceAtLeast(1)) },
                        onMoveDown = { viewModel.reorderGalleryItem(item.id, item.displayOrder + 1) }
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredItems, key = { it.id }) { item ->
                    GalleryListCard(
                        item = item,
                        onPreview = { previewingItem = item },
                        onEdit = { editingItem = item },
                        onDelete = { deletingItem = item },
                        onToggleActive = { viewModel.toggleGalleryItemActive(item.id, it) },
                        onMoveUp = { viewModel.reorderGalleryItem(item.id, (item.displayOrder - 1).coerceAtLeast(1)) },
                        onMoveDown = { viewModel.reorderGalleryItem(item.id, item.displayOrder + 1) }
                    )
                }
            }
        }
    }

    // Add Image Dialog
    if (isAddingItem) {
        AddOrEditGalleryDialog(
            titleText = "Upload / Add Gallery Image",
            confirmButtonText = "Save to Gallery",
            existingItem = null,
            onDismiss = { isAddingItem = false },
            onConfirm = { title, category, imageUrl, order, isActive ->
                viewModel.addGalleryItem(
                    title = title,
                    category = category,
                    imageUrl = imageUrl,
                    displayOrder = order,
                    isActive = isActive,
                    onResult = { success, msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        if (success) isAddingItem = false
                    }
                )
            }
        )
    }

    // Edit Image Dialog
    if (editingItem != null) {
        val itemToEdit = editingItem!!
        AddOrEditGalleryDialog(
            titleText = "Edit / Replace Gallery Image",
            confirmButtonText = "Update Image",
            existingItem = itemToEdit,
            onDismiss = { editingItem = null },
            onConfirm = { title, category, imageUrl, order, isActive ->
                val oldImageToDelete = if (imageUrl != itemToEdit.imageUrl) itemToEdit.imageUrl else null
                viewModel.updateGalleryItem(
                    item = itemToEdit.copy(
                        title = title,
                        category = category,
                        imageUrl = imageUrl,
                        displayOrder = order,
                        isActive = isActive
                    ),
                    oldImageUrlToDelete = oldImageToDelete,
                    onResult = { success, msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        if (success) editingItem = null
                    }
                )
            }
        )
    }

    // Delete Confirmation Dialog
    if (deletingItem != null) {
        val itemToDelete = deletingItem!!
        AlertDialog(
            onDismissRequest = { deletingItem = null },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp),
            title = {
                Text(
                    text = "Delete Gallery Image?",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFC62828),
                    fontSize = 17.sp
                )
            },
            text = {
                Column {
                    Text(
                        text = "Are you sure you want to permanently delete \"${itemToDelete.title}\"?",
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        PlantVisualCard(imageKey = itemToDelete.imageUrl, modifier = Modifier.fillMaxSize())
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteGalleryItem(itemToDelete) { success, msg ->
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            deletingItem = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("Delete Permanently", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { deletingItem = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Preview Dialog
    if (previewingItem != null) {
        val itemToPreview = previewingItem!!
        AlertDialog(
            onDismissRequest = { previewingItem = null },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp),
            title = {
                Text(
                    text = itemToPreview.title,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenDark,
                    fontSize = 16.sp
                )
            },
            text = {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        PlantVisualCard(imageKey = itemToPreview.imageUrl, modifier = Modifier.fillMaxSize())
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Category: ${itemToPreview.category}", fontSize = 12.sp, color = Color.Gray)
                        Text(text = "Order: #${itemToPreview.displayOrder}", fontSize = 12.sp, color = ForestGreen, fontWeight = FontWeight.Bold)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { previewingItem = null }) {
                    Text("Close", color = ForestGreenDark, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
fun GalleryListCard(
    item: GalleryItemEntity,
    onPreview: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleActive: (Boolean) -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onPreview() }
            ) {
                PlantVisualCard(imageKey = item.imageUrl, modifier = Modifier.fillMaxSize())
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = ForestGreenDark.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = item.category,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenDark,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = GoldAccentLight.copy(alpha = 0.25f)
                    ) {
                        Text(
                            text = "#${item.displayOrder}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldAccentDark,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = if (item.isActive) ForestGreenDark else Color.Gray,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        checked = item.isActive,
                        onCheckedChange = onToggleActive,
                        modifier = Modifier.size(36.dp),
                        colors = SwitchDefaults.colors(checkedThumbColor = ForestGreen, checkedTrackColor = FreshGreen)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (item.isActive) "Active" else "Disabled",
                        fontSize = 11.sp,
                        color = if (item.isActive) ForestGreen else Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Ordering & Action buttons
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row {
                    IconButton(onClick = onMoveUp, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.ArrowUpward, contentDescription = "Move Up", tint = ForestGreen, modifier = Modifier.size(16.dp))
                    }
                    IconButton(onClick = onMoveDown, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.ArrowDownward, contentDescription = "Move Down", tint = ForestGreen, modifier = Modifier.size(16.dp))
                    }
                }
                Row {
                    IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = ForestGreen, modifier = Modifier.size(16.dp))
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFC62828), modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun GalleryGridCard(
    item: GalleryItemEntity,
    onPreview: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleActive: (Boolean) -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
                    .clickable { onPreview() }
            ) {
                PlantVisualCard(imageKey = item.imageUrl, modifier = Modifier.fillMaxSize())
                Surface(
                    shape = RoundedCornerShape(bottomEnd = 8.dp),
                    color = ForestGreenDark.copy(alpha = 0.85f),
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Text(
                        text = item.category,
                        color = GoldAccentLight,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Surface(
                    shape = RoundedCornerShape(bottomStart = 8.dp),
                    color = Color.Black.copy(alpha = 0.6f),
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Text(
                        text = "#${item.displayOrder}",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = if (item.isActive) ForestGreenDark else Color.Gray,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Switch(
                        checked = item.isActive,
                        onCheckedChange = onToggleActive,
                        modifier = Modifier.size(32.dp),
                        colors = SwitchDefaults.colors(checkedThumbColor = ForestGreen, checkedTrackColor = FreshGreen)
                    )

                    Row {
                        IconButton(onClick = onEdit, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = ForestGreen, modifier = Modifier.size(16.dp))
                        }
                        IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFC62828), modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

/**
 * Clean modal dialog supporting:
 * 1. Native Device Image Picker (saves to persistent files storage via ImageStorageManager)
 * 2. Remote URL / Preset Image input
 * 3. Instant Live Preview before committing
 * 4. Title, Category, Order, and Active state inputs
 */
@Composable
fun AddOrEditGalleryDialog(
    titleText: String,
    confirmButtonText: String,
    existingItem: GalleryItemEntity?,
    onDismiss: () -> Unit,
    onConfirm: (title: String, category: String, imageUrl: String, order: Int, isActive: Boolean) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var title by remember { mutableStateOf(existingItem?.title ?: "") }
    var category by remember { mutableStateOf(existingItem?.category ?: "Nursery") }
    var imageUrl by remember { mutableStateOf(existingItem?.imageUrl ?: "") }
    var orderText by remember { mutableStateOf((existingItem?.displayOrder ?: 1).toString()) }
    var isActive by remember { mutableStateOf(existingItem?.isActive ?: true) }
    var isProcessingFile by remember { mutableStateOf(false) }
    var validationError by remember { mutableStateOf<String?>(null) }

    val categories = listOf("Nursery", "Coconut Plants", "Plant Delivery", "Plantation", "Customers", "Farm", "Videos")

    // Image Picker Launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            isProcessingFile = true
            validationError = null
            coroutineScope.launch {
                val result = withContext(Dispatchers.IO) {
                    ImageStorageManager.saveImageFromUri(context, uri, subDir = "gallery")
                }
                isProcessingFile = false
                result.fold(
                    onSuccess = { savedPath ->
                        imageUrl = savedPath
                        if (title.isEmpty()) {
                            title = "Dwarkesh Nursery Photo"
                        }
                    },
                    onFailure = { error ->
                        validationError = error.message ?: "Failed to read image file."
                    }
                )
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp),
        title = {
            Text(
                text = titleText,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = ForestGreenDark
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Live Preview Card
                item {
                    Text(text = "Preview Before Saving", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF5F5F5)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isProcessingFile) {
                            CircularProgressIndicator(color = ForestGreen)
                        } else if (imageUrl.isNotEmpty()) {
                            PlantVisualCard(imageKey = imageUrl, modifier = Modifier.fillMaxSize())
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Image, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("No image selected yet", color = Color.Gray, fontSize = 12.sp)
                            }
                        }
                    }
                }

                // File Upload Button
                item {
                    Button(
                        onClick = { imagePickerLauncher.launch("image/*") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreenDark),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Pick Image from Device Storage", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Or Direct Image URL input
                item {
                    OutlinedTextField(
                        value = imageUrl,
                        onValueChange = {
                            imageUrl = it
                            validationError = null
                        },
                        label = { Text("Image URL or Internal Path") },
                        placeholder = { Text("https://... or file://...") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ForestGreen,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                // Title Input
                item {
                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            title = it
                            validationError = null
                        },
                        label = { Text("Photo Title / Description *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ForestGreen,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                // Category Selection
                item {
                    Text(text = "Category", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(categories) { cat ->
                            val isSelected = category == cat
                            FilterChip(
                                selected = isSelected,
                                onClick = { category = cat },
                                label = { Text(cat, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = ForestGreen,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                // Order & Active Toggle
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedTextField(
                            value = orderText,
                            onValueChange = { if (it.all { ch -> ch.isDigit() }) orderText = it },
                            label = { Text("Sort Order") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.width(110.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ForestGreen),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Active in Gallery:", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.width(6.dp))
                            Switch(
                                checked = isActive,
                                onCheckedChange = { isActive = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = ForestGreen, checkedTrackColor = FreshGreen)
                            )
                        }
                    }
                }

                // Validation Error Message
                if (validationError != null) {
                    item {
                        Text(
                            text = "⚠️ $validationError",
                            color = Color(0xFFC62828),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isBlank()) {
                        validationError = "Please enter a title."
                        return@Button
                    }
                    if (imageUrl.isBlank()) {
                        validationError = "Please select or enter an image."
                        return@Button
                    }
                    val order = orderText.toIntOrNull() ?: 1
                    onConfirm(title, category, imageUrl, order, isActive)
                },
                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                enabled = !isProcessingFile
            ) {
                Text(confirmButtonText, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
