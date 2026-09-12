package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val gujaratiName: String,
    val description: String,
    val benefits: String,
    val age: String,
    val price: Double,
    val oldPrice: Double = 0.0,
    val discountPercent: Int = 0,
    val stockQuantity: Int,
    val isAvailable: Boolean = true,
    val minOrderQuantity: Int = 1,
    val mainImage: String,
    val additionalImages: String = "", // Comma-separated or JSON
    val isFeatured: Boolean = false,
    val category: String = "Coconut Plants"
)

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey val bookingId: String, // e.g. DWK-2026-0001
    val customerName: String,
    val mobileNumber: String,
    val plantId: Long,
    val plantName: String,
    val gujaratiPlantName: String = "",
    val quantity: Int,
    val unitPrice: Double,
    val totalAmount: Double,
    val advanceAmount: Double,
    val remainingAmount: Double,
    val deliveryType: String, // "Nursery Pickup" or "Home Delivery"
    val address: String,
    val villageCity: String,
    val district: String,
    val bookingDate: String,
    val notes: String = "",
    val deliveryStatus: String = "Order Confirmed", // "Pending", "Order Confirmed", "Plant Ready", "Out for Delivery", "Delivered", "Cancelled"
    val paymentStatus: String = "Partially Paid", // "Pending", "Partially Paid", "Paid"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "gallery")
data class GalleryItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String = "Nursery", // "Nursery", "Coconut Plants", "Plant Delivery", "Plantation", "Customers", "Farm", "Videos"
    val imageUrl: String,
    val displayOrder: Int = 0,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val customerName: String,
    val villageOrCity: String = "Gujarat",
    val rating: Int, // 1 to 5
    val reviewText: String,
    val photoUrl: String = "",
    val isApproved: Boolean = true,
    val isFeatured: Boolean = false,
    val dateString: String = "2026-09"
)

@Entity(tableName = "offers")
data class OfferEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val gujaratiTitle: String,
    val description: String,
    val discountText: String,
    val imageUrl: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val isActive: Boolean = true
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val message: String,
    val type: String = "general", // "booking", "delivery", "offer", "general"
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

@Entity(tableName = "settings")
data class SettingEntity(
    @PrimaryKey val key: String,
    val value: String
)
