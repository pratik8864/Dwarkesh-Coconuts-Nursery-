package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.BookingEntity
import com.example.data.BrandingConfig
import com.example.data.DwarkeshRepository
import com.example.data.GalleryItemEntity
import com.example.data.NotificationEntity
import com.example.data.OfferEntity
import com.example.data.ProductEntity
import com.example.data.ReviewEntity
import com.example.data.storage.ImageStorageManager
import com.example.ui.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DwarkeshRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = DwarkeshRepository(db)
    }

    // Language State
    private val _currentLanguage = MutableStateFlow(AppLanguage.GUJARATI)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    fun toggleLanguage() {
        _currentLanguage.value = if (_currentLanguage.value == AppLanguage.GUJARATI) {
            AppLanguage.ENGLISH
        } else {
            AppLanguage.GUJARATI
        }
    }

    // Admin Authentication State
    private val _isAdminLoggedIn = MutableStateFlow(false)
    val isAdminLoggedIn: StateFlow<Boolean> = _isAdminLoggedIn.asStateFlow()

    fun adminLogin(pin: String): Boolean {
        val correctPin = settingsMap.value["admin_pin"] ?: "1234"
        val success = (pin == correctPin)
        if (success) {
            _isAdminLoggedIn.value = true
        }
        return success
    }

    fun adminLogout() {
        _isAdminLoggedIn.value = false
    }

    // Products Flow
    val allProducts: StateFlow<List<ProductEntity>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val featuredProducts: StateFlow<List<ProductEntity>> = repository.featuredProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Bookings Flow
    val allBookings: StateFlow<List<BookingEntity>> = repository.allBookings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Gallery Flow
    val activeGalleryItems: StateFlow<List<GalleryItemEntity>> = repository.activeGalleryItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allGalleryItems: StateFlow<List<GalleryItemEntity>> = repository.allGalleryItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Direct alias for backward compatibility
    val galleryItems: StateFlow<List<GalleryItemEntity>> = activeGalleryItems

    // Central Branding Configuration Flow
    val brandingConfig: StateFlow<BrandingConfig> = repository.allSettings
        .map { list ->
            val map = list.associate { it.key to it.value }
            BrandingConfig(
                appName = map["branding_app_name"] ?: map["nursery_name"] ?: "DWARKESH COCONUTS & NURSERY",
                tagline = map["branding_tagline"] ?: map["nursery_tagline"] ?: "Quality Plants • Healthy Growth • Trusted Service",
                logoUrl = map["branding_logo_url"] ?: "",
                appIconUrl = map["branding_icon_url"] ?: "",
                faviconUrl = map["branding_favicon_url"] ?: "",
                updatedAt = map["branding_updated_at"]?.toLongOrNull() ?: System.currentTimeMillis()
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BrandingConfig())

    // Admin Operation Feedback State
    data class AdminFeedback(
        val message: String,
        val isError: Boolean = false,
        val timestamp: Long = System.currentTimeMillis()
    )

    private val _adminFeedback = MutableStateFlow<AdminFeedback?>(null)
    val adminFeedback: StateFlow<AdminFeedback?> = _adminFeedback.asStateFlow()

    fun clearAdminFeedback() {
        _adminFeedback.value = null
    }

    // Reviews Flow
    val approvedReviews: StateFlow<List<ReviewEntity>> = repository.approvedReviews
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allReviews: StateFlow<List<ReviewEntity>> = repository.allReviews
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Offers Flow
    val activeOffers: StateFlow<List<OfferEntity>> = repository.activeOffers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allOffers: StateFlow<List<OfferEntity>> = repository.allOffers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Notifications Flow
    val notifications: StateFlow<List<NotificationEntity>> = repository.allNotifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Settings Flow (Mapped to Key-Value dictionary)
    val settingsMap: StateFlow<Map<String, String>> = repository.allSettings
        .map { list -> list.associate { it.key to it.value } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    // Plant Catalog Filter States
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    // Booking Creation
    fun createBooking(
        customerName: String,
        mobileNumber: String,
        plant: ProductEntity,
        quantity: Int,
        advanceAmount: Double,
        deliveryType: String,
        address: String,
        villageCity: String,
        district: String,
        notes: String,
        onSuccess: (BookingEntity) -> Unit
    ) {
        viewModelScope.launch {
            val booking = repository.createBooking(
                customerName = customerName,
                mobileNumber = mobileNumber,
                plant = plant,
                quantity = quantity,
                advanceAmount = advanceAmount,
                deliveryType = deliveryType,
                address = address,
                villageCity = villageCity,
                district = district,
                notes = notes
            )
            onSuccess(booking)
        }
    }

    // Admin Product Actions
    fun saveProduct(product: ProductEntity, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            repository.insertOrUpdateProduct(product)
            onDone()
        }
    }

    fun deleteProduct(product: ProductEntity) {
        viewModelScope.launch {
            repository.deleteProduct(product)
        }
    }

    fun updatePrice(id: Long, price: Double, oldPrice: Double, discount: Int) {
        viewModelScope.launch {
            repository.updateProductPrice(id, price, oldPrice, discount)
        }
    }

    fun updateStock(id: Long, stock: Int, isAvailable: Boolean) {
        viewModelScope.launch {
            repository.updateProductStock(id, stock, isAvailable)
        }
    }

    // Admin Booking Actions
    fun updateBookingStatus(bookingId: String, status: String) {
        viewModelScope.launch {
            repository.updateBookingDeliveryStatus(bookingId, status)
        }
    }

    fun updateBookingPayment(bookingId: String, status: String, advance: Double, remaining: Double) {
        viewModelScope.launch {
            repository.updateBookingPayment(bookingId, status, advance, remaining)
        }
    }

    fun deleteBooking(booking: BookingEntity) {
        viewModelScope.launch {
            repository.deleteBooking(booking)
        }
    }

    // Authorization Helper
    private fun requireAdminAuth(): Boolean {
        if (!_isAdminLoggedIn.value) {
            _adminFeedback.value = AdminFeedback("Unauthorized: Please unlock with Admin PIN first.", isError = true)
            return false
        }
        return true
    }

    // Gallery Actions
    fun addGalleryItem(
        title: String,
        category: String,
        imageUrl: String,
        displayOrder: Int = 0,
        isActive: Boolean = true,
        onResult: (Boolean, String) -> Unit = { _, _ -> }
    ) {
        if (!requireAdminAuth()) {
            onResult(false, "Unauthorized: Admin PIN required.")
            return
        }
        val validation = ImageStorageManager.validateImageInput(imageUrl)
        if (!validation.first) {
            val err = validation.second ?: "Invalid image source."
            _adminFeedback.value = AdminFeedback(err, isError = true)
            onResult(false, err)
            return
        }
        viewModelScope.launch {
            try {
                val now = System.currentTimeMillis()
                val id = repository.insertGalleryItem(
                    GalleryItemEntity(
                        title = title.trim(),
                        category = category.trim().ifEmpty { "Nursery" },
                        imageUrl = imageUrl.trim(),
                        displayOrder = displayOrder,
                        isActive = isActive,
                        createdAt = now,
                        updatedAt = now
                    )
                )
                val msg = "Gallery image added successfully (ID: $id)!"
                _adminFeedback.value = AdminFeedback(msg, isError = false)
                onResult(true, msg)
            } catch (e: Exception) {
                val err = "Failed to add image: ${e.message}"
                _adminFeedback.value = AdminFeedback(err, isError = true)
                onResult(false, err)
            }
        }
    }

    fun updateGalleryItem(
        item: GalleryItemEntity,
        oldImageUrlToDelete: String? = null,
        onResult: (Boolean, String) -> Unit = { _, _ -> }
    ) {
        if (!requireAdminAuth()) {
            onResult(false, "Unauthorized: Admin PIN required.")
            return
        }
        viewModelScope.launch {
            try {
                repository.updateGalleryItem(item.copy(updatedAt = System.currentTimeMillis()))
                if (!oldImageUrlToDelete.isNullOrEmpty() && oldImageUrlToDelete != item.imageUrl) {
                    ImageStorageManager.deleteFileIfExists(oldImageUrlToDelete)
                }
                val msg = "Gallery image updated successfully!"
                _adminFeedback.value = AdminFeedback(msg, isError = false)
                onResult(true, msg)
            } catch (e: Exception) {
                val err = "Failed to update image: ${e.message}"
                _adminFeedback.value = AdminFeedback(err, isError = true)
                onResult(false, err)
            }
        }
    }

    fun toggleGalleryItemActive(id: Long, isActive: Boolean) {
        if (!requireAdminAuth()) return
        viewModelScope.launch {
            repository.updateGalleryItemActive(id, isActive)
            _adminFeedback.value = AdminFeedback("Image ${if (isActive) "enabled" else "disabled"}.", isError = false)
        }
    }

    fun reorderGalleryItem(id: Long, newOrder: Int) {
        if (!requireAdminAuth()) return
        viewModelScope.launch {
            repository.updateGalleryItemOrder(id, newOrder)
            _adminFeedback.value = AdminFeedback("Display order set to $newOrder.", isError = false)
        }
    }

    fun deleteGalleryItem(item: GalleryItemEntity, onResult: (Boolean, String) -> Unit = { _, _ -> }) {
        if (!requireAdminAuth()) {
            onResult(false, "Unauthorized: Admin PIN required.")
            return
        }
        viewModelScope.launch {
            try {
                repository.deleteGalleryItem(item)
                ImageStorageManager.deleteFileIfExists(item.imageUrl)
                val msg = "Gallery image removed."
                _adminFeedback.value = AdminFeedback(msg, isError = false)
                onResult(true, msg)
            } catch (e: Exception) {
                val err = "Failed to delete: ${e.message}"
                _adminFeedback.value = AdminFeedback(err, isError = true)
                onResult(false, err)
            }
        }
    }

    // App Branding Actions
    fun updateAppLogo(newLogoUrl: String, onResult: (Boolean, String) -> Unit = { _, _ -> }) {
        if (!requireAdminAuth()) {
            onResult(false, "Unauthorized: Admin PIN required.")
            return
        }
        val validation = ImageStorageManager.validateImageInput(newLogoUrl)
        if (!validation.first) {
            val err = validation.second ?: "Invalid logo format."
            _adminFeedback.value = AdminFeedback(err, isError = true)
            onResult(false, err)
            return
        }
        viewModelScope.launch {
            try {
                val oldLogo = brandingConfig.value.logoUrl
                repository.updateSetting("branding_logo_url", newLogoUrl.trim())
                repository.updateSetting("branding_updated_at", System.currentTimeMillis().toString())
                if (oldLogo.isNotEmpty() && oldLogo != newLogoUrl) {
                    ImageStorageManager.deleteFileIfExists(oldLogo)
                }
                val msg = "App logo updated successfully across all screens!"
                _adminFeedback.value = AdminFeedback(msg, isError = false)
                onResult(true, msg)
            } catch (e: Exception) {
                val err = "Failed to update logo: ${e.message}"
                _adminFeedback.value = AdminFeedback(err, isError = true)
                onResult(false, err)
            }
        }
    }

    fun resetAppLogo(onResult: (Boolean, String) -> Unit = { _, _ -> }) {
        if (!requireAdminAuth()) {
            onResult(false, "Unauthorized: Admin PIN required.")
            return
        }
        viewModelScope.launch {
            try {
                val oldLogo = brandingConfig.value.logoUrl
                repository.updateSetting("branding_logo_url", "")
                repository.updateSetting("branding_updated_at", System.currentTimeMillis().toString())
                if (oldLogo.isNotEmpty()) {
                    ImageStorageManager.deleteFileIfExists(oldLogo)
                }
                val msg = "App logo restored to official Dwarkesh emblem."
                _adminFeedback.value = AdminFeedback(msg, isError = false)
                onResult(true, msg)
            } catch (e: Exception) {
                val err = "Failed to reset logo: ${e.message}"
                _adminFeedback.value = AdminFeedback(err, isError = true)
                onResult(false, err)
            }
        }
    }

    fun updateAppIcon(newIconUrl: String, onResult: (Boolean, String) -> Unit = { _, _ -> }) {
        if (!requireAdminAuth()) {
            onResult(false, "Unauthorized: Admin PIN required.")
            return
        }
        val validation = ImageStorageManager.validateImageInput(newIconUrl)
        if (!validation.first) {
            val err = validation.second ?: "Invalid app icon format."
            _adminFeedback.value = AdminFeedback(err, isError = true)
            onResult(false, err)
            return
        }
        viewModelScope.launch {
            try {
                val oldIcon = brandingConfig.value.appIconUrl
                repository.updateSetting("branding_icon_url", newIconUrl.trim())
                repository.updateSetting("branding_updated_at", System.currentTimeMillis().toString())
                if (oldIcon.isNotEmpty() && oldIcon != newIconUrl) {
                    ImageStorageManager.deleteFileIfExists(oldIcon)
                }
                val msg = "App icon saved! Dynamic in-app & PWA branding updated."
                _adminFeedback.value = AdminFeedback(msg, isError = false)
                onResult(true, msg)
            } catch (e: Exception) {
                val err = "Failed to update icon: ${e.message}"
                _adminFeedback.value = AdminFeedback(err, isError = true)
                onResult(false, err)
            }
        }
    }

    fun resetAppIcon(onResult: (Boolean, String) -> Unit = { _, _ -> }) {
        if (!requireAdminAuth()) {
            onResult(false, "Unauthorized: Admin PIN required.")
            return
        }
        viewModelScope.launch {
            try {
                val oldIcon = brandingConfig.value.appIconUrl
                repository.updateSetting("branding_icon_url", "")
                repository.updateSetting("branding_updated_at", System.currentTimeMillis().toString())
                if (oldIcon.isNotEmpty()) {
                    ImageStorageManager.deleteFileIfExists(oldIcon)
                }
                val msg = "App icon reset to default."
                _adminFeedback.value = AdminFeedback(msg, isError = false)
                onResult(true, msg)
            } catch (e: Exception) {
                val err = "Failed to reset icon: ${e.message}"
                _adminFeedback.value = AdminFeedback(err, isError = true)
                onResult(false, err)
            }
        }
    }

    fun updateBrandingDetails(name: String, tagline: String, onResult: (Boolean, String) -> Unit = { _, _ -> }) {
        if (!requireAdminAuth()) {
            onResult(false, "Unauthorized: Admin PIN required.")
            return
        }
        viewModelScope.launch {
            try {
                repository.updateSetting("branding_app_name", name.trim())
                repository.updateSetting("nursery_name", name.trim())
                repository.updateSetting("branding_tagline", tagline.trim())
                repository.updateSetting("nursery_tagline", tagline.trim())
                repository.updateSetting("branding_updated_at", System.currentTimeMillis().toString())
                val msg = "Branding details updated successfully!"
                _adminFeedback.value = AdminFeedback(msg, isError = false)
                onResult(true, msg)
            } catch (e: Exception) {
                val err = "Failed to update details: ${e.message}"
                _adminFeedback.value = AdminFeedback(err, isError = true)
                onResult(false, err)
            }
        }
    }

    // Review Actions
    fun submitReview(name: String, village: String, rating: Int, comment: String) {
        viewModelScope.launch {
            repository.submitReview(name, village, rating, comment)
        }
    }

    fun setReviewApproval(id: Long, approved: Boolean) {
        viewModelScope.launch {
            repository.setReviewApproved(id, approved)
        }
    }

    fun deleteReview(review: ReviewEntity) {
        viewModelScope.launch {
            repository.deleteReview(review)
        }
    }

    // Offer Actions
    fun saveOffer(offer: OfferEntity, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            repository.insertOrUpdateOffer(offer)
            onDone()
        }
    }

    fun deleteOffer(offer: OfferEntity) {
        viewModelScope.launch {
            repository.deleteOffer(offer)
        }
    }

    // Settings
    fun updateSetting(key: String, value: String) {
        viewModelScope.launch {
            repository.updateSetting(key, value)
        }
    }

    // Notification Actions
    fun markNotificationRead(id: Long) {
        viewModelScope.launch {
            repository.markNotificationAsRead(id)
        }
    }

    fun clearAllNotifications() {
        viewModelScope.launch {
            repository.clearAllNotifications()
        }
    }
}
