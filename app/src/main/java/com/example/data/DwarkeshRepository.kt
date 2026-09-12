package com.example.data

import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DwarkeshRepository(private val database: AppDatabase) {

    // Products
    val allProducts: Flow<List<ProductEntity>> = database.productDao().getAllProducts()
    val featuredProducts: Flow<List<ProductEntity>> = database.productDao().getFeaturedProducts()

    suspend fun getProductById(id: Long): ProductEntity? = database.productDao().getProductById(id)

    suspend fun insertOrUpdateProduct(product: ProductEntity): Long = database.productDao().insertProduct(product)

    suspend fun updateProductPrice(id: Long, price: Double, oldPrice: Double, discount: Int) {
        database.productDao().updatePrice(id, price, oldPrice, discount)
    }

    suspend fun updateProductStock(id: Long, stock: Int, isAvailable: Boolean) {
        database.productDao().updateStock(id, stock, isAvailable)
    }

    suspend fun deleteProduct(product: ProductEntity) = database.productDao().deleteProduct(product)

    suspend fun deleteProductById(id: Long) = database.productDao().deleteProductById(id)

    // Bookings
    val allBookings: Flow<List<BookingEntity>> = database.bookingDao().getAllBookings()

    suspend fun getBookingById(bookingId: String): BookingEntity? = database.bookingDao().getBookingById(bookingId)

    suspend fun createBooking(
        customerName: String,
        mobileNumber: String,
        plant: ProductEntity,
        quantity: Int,
        advanceAmount: Double,
        deliveryType: String,
        address: String,
        villageCity: String,
        district: String,
        notes: String
    ): BookingEntity {
        val count = System.currentTimeMillis() % 10000
        val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())
        val bookingId = String.format(Locale.getDefault(), "DWK-%s-%04d", year, count)
        val today = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())

        val totalAmount = plant.price * quantity
        val remaining = (totalAmount - advanceAmount).coerceAtLeast(0.0)
        val paymentStatus = when {
            advanceAmount >= totalAmount -> "Paid"
            advanceAmount > 0 -> "Partially Paid"
            else -> "Pending"
        }

        val booking = BookingEntity(
            bookingId = bookingId,
            customerName = customerName,
            mobileNumber = mobileNumber,
            plantId = plant.id,
            plantName = plant.name,
            gujaratiPlantName = plant.gujaratiName,
            quantity = quantity,
            unitPrice = plant.price,
            totalAmount = totalAmount,
            advanceAmount = advanceAmount,
            remainingAmount = remaining,
            deliveryType = deliveryType,
            address = address,
            villageCity = villageCity,
            district = district,
            bookingDate = today,
            notes = notes,
            deliveryStatus = "Order Confirmed",
            paymentStatus = paymentStatus,
            timestamp = System.currentTimeMillis()
        )

        database.bookingDao().insertBooking(booking)

        // Decrement stock in product
        val newStock = (plant.stockQuantity - quantity).coerceAtLeast(0)
        database.productDao().updateStock(plant.id, newStock, newStock > 0)

        // Generate automated notification
        val notification = NotificationEntity(
            title = "New Booking: $bookingId",
            message = "Booking confirmed for $quantity × ${plant.name} by $customerName ($mobileNumber).",
            type = "booking",
            timestamp = System.currentTimeMillis()
        )
        database.notificationDao().insertNotification(notification)

        return booking
    }

    suspend fun updateBookingDeliveryStatus(bookingId: String, status: String) {
        database.bookingDao().updateDeliveryStatus(bookingId, status)
    }

    suspend fun updateBookingPayment(bookingId: String, status: String, advance: Double, remaining: Double) {
        database.bookingDao().updatePayment(bookingId, status, advance, remaining)
    }

    suspend fun deleteBooking(booking: BookingEntity) = database.bookingDao().deleteBooking(booking)

    // Gallery
    val activeGalleryItems: Flow<List<GalleryItemEntity>> = database.galleryDao().getActiveGalleryItems()
    val allGalleryItems: Flow<List<GalleryItemEntity>> = database.galleryDao().getAllGalleryItems()

    suspend fun getGalleryItemById(id: Long): GalleryItemEntity? = database.galleryDao().getItemById(id)

    suspend fun insertGalleryItem(item: GalleryItemEntity): Long = database.galleryDao().insertItem(item)

    suspend fun updateGalleryItem(item: GalleryItemEntity) = database.galleryDao().updateItem(item)

    suspend fun updateGalleryItemActive(id: Long, isActive: Boolean) {
        database.galleryDao().updateActiveStatus(id, isActive, System.currentTimeMillis())
    }

    suspend fun updateGalleryItemOrder(id: Long, order: Int) {
        database.galleryDao().updateDisplayOrder(id, order, System.currentTimeMillis())
    }

    suspend fun deleteGalleryItem(item: GalleryItemEntity) = database.galleryDao().deleteItem(item)

    suspend fun deleteGalleryItemById(id: Long) = database.galleryDao().deleteItemById(id)

    // Reviews
    val approvedReviews: Flow<List<ReviewEntity>> = database.reviewDao().getApprovedReviews()
    val allReviews: Flow<List<ReviewEntity>> = database.reviewDao().getAllReviews()

    suspend fun submitReview(customerName: String, village: String, rating: Int, review: String, photoUrl: String = "") {
        val today = SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(Date())
        val reviewEntity = ReviewEntity(
            customerName = customerName,
            villageOrCity = village,
            rating = rating,
            reviewText = review,
            photoUrl = photoUrl,
            isApproved = true, // Approved by default or configurable
            isFeatured = rating >= 5,
            dateString = today
        )
        database.reviewDao().insertReview(reviewEntity)
    }

    suspend fun setReviewApproved(id: Long, approved: Boolean) = database.reviewDao().setApproved(id, approved)

    suspend fun setReviewFeatured(id: Long, featured: Boolean) = database.reviewDao().setFeatured(id, featured)

    suspend fun deleteReview(review: ReviewEntity) = database.reviewDao().deleteReview(review)

    // Offers
    val activeOffers: Flow<List<OfferEntity>> = database.offerDao().getActiveOffers()
    val allOffers: Flow<List<OfferEntity>> = database.offerDao().getAllOffers()

    suspend fun insertOrUpdateOffer(offer: OfferEntity): Long = database.offerDao().insertOffer(offer)

    suspend fun deleteOffer(offer: OfferEntity) = database.offerDao().deleteOffer(offer)

    // Notifications
    val allNotifications: Flow<List<NotificationEntity>> = database.notificationDao().getAllNotifications()

    suspend fun createNotification(title: String, message: String, type: String = "general") {
        database.notificationDao().insertNotification(
            NotificationEntity(title = title, message = message, type = type)
        )
    }

    suspend fun markNotificationAsRead(id: Long) = database.notificationDao().markAsRead(id)

    suspend fun clearAllNotifications() = database.notificationDao().clearAll()

    // Settings
    val allSettings: Flow<List<SettingEntity>> = database.settingDao().getAllSettings()

    suspend fun updateSetting(key: String, value: String) {
        database.settingDao().setSetting(SettingEntity(key, value))
    }

    suspend fun getSettingValue(key: String): String? = database.settingDao().getSettingValue(key)
}
