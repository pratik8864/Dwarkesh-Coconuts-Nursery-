package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY id ASC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE isFeatured = 1 ORDER BY id ASC")
    fun getFeaturedProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    suspend fun getProductById(id: Long): ProductEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductEntity>)

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Query("UPDATE products SET price = :price, oldPrice = :oldPrice, discountPercent = :discount WHERE id = :id")
    suspend fun updatePrice(id: Long, price: Double, oldPrice: Double, discount: Int)

    @Query("UPDATE products SET stockQuantity = :stock, isAvailable = :isAvailable WHERE id = :id")
    suspend fun updateStock(id: Long, stock: Int, isAvailable: Boolean)

    @Delete
    suspend fun deleteProduct(product: ProductEntity)

    @Query("DELETE FROM products WHERE id = :id")
    suspend fun deleteProductById(id: Long)
}

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings ORDER BY timestamp DESC")
    fun getAllBookings(): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE bookingId = :bookingId LIMIT 1")
    suspend fun getBookingById(bookingId: String): BookingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity)

    @Update
    suspend fun updateBooking(booking: BookingEntity)

    @Query("UPDATE bookings SET deliveryStatus = :status WHERE bookingId = :bookingId")
    suspend fun updateDeliveryStatus(bookingId: String, status: String)

    @Query("UPDATE bookings SET paymentStatus = :paymentStatus, advanceAmount = :advance, remainingAmount = :remaining WHERE bookingId = :bookingId")
    suspend fun updatePayment(bookingId: String, paymentStatus: String, advance: Double, remaining: Double)

    @Delete
    suspend fun deleteBooking(booking: BookingEntity)

    @Query("DELETE FROM bookings WHERE bookingId = :bookingId")
    suspend fun deleteBookingById(bookingId: String)
}

@Dao
interface GalleryDao {
    @Query("SELECT * FROM gallery WHERE isActive = 1 ORDER BY displayOrder ASC, id DESC")
    fun getActiveGalleryItems(): Flow<List<GalleryItemEntity>>

    @Query("SELECT * FROM gallery ORDER BY displayOrder ASC, id DESC")
    fun getAllGalleryItems(): Flow<List<GalleryItemEntity>>

    @Query("SELECT * FROM gallery WHERE id = :id LIMIT 1")
    suspend fun getItemById(id: Long): GalleryItemEntity?

    @Query("SELECT * FROM gallery WHERE category = :category AND isActive = 1 ORDER BY displayOrder ASC, id DESC")
    fun getGalleryByCategory(category: String): Flow<List<GalleryItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: GalleryItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<GalleryItemEntity>)

    @Update
    suspend fun updateItem(item: GalleryItemEntity)

    @Query("UPDATE gallery SET isActive = :isActive, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateActiveStatus(id: Long, isActive: Boolean, updatedAt: Long)

    @Query("UPDATE gallery SET displayOrder = :displayOrder, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateDisplayOrder(id: Long, displayOrder: Int, updatedAt: Long)

    @Delete
    suspend fun deleteItem(item: GalleryItemEntity)

    @Query("DELETE FROM gallery WHERE id = :id")
    suspend fun deleteItemById(id: Long)
}

@Dao
interface ReviewDao {
    @Query("SELECT * FROM reviews WHERE isApproved = 1 ORDER BY id DESC")
    fun getApprovedReviews(): Flow<List<ReviewEntity>>

    @Query("SELECT * FROM reviews ORDER BY id DESC")
    fun getAllReviews(): Flow<List<ReviewEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(reviews: List<ReviewEntity>)

    @Update
    suspend fun updateReview(review: ReviewEntity)

    @Query("UPDATE reviews SET isApproved = :approved WHERE id = :id")
    suspend fun setApproved(id: Long, approved: Boolean)

    @Query("UPDATE reviews SET isFeatured = :featured WHERE id = :id")
    suspend fun setFeatured(id: Long, featured: Boolean)

    @Delete
    suspend fun deleteReview(review: ReviewEntity)

    @Query("DELETE FROM reviews WHERE id = :id")
    suspend fun deleteReviewById(id: Long)
}

@Dao
interface OfferDao {
    @Query("SELECT * FROM offers WHERE isActive = 1 ORDER BY id DESC")
    fun getActiveOffers(): Flow<List<OfferEntity>>

    @Query("SELECT * FROM offers ORDER BY id DESC")
    fun getAllOffers(): Flow<List<OfferEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOffer(offer: OfferEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(offers: List<OfferEntity>)

    @Update
    suspend fun updateOffer(offer: OfferEntity)

    @Delete
    suspend fun deleteOffer(offer: OfferEntity)

    @Query("DELETE FROM offers WHERE id = :id")
    suspend fun deleteOfferById(id: Long)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity): Long

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Long)

    @Query("DELETE FROM notifications WHERE id = :id")
    suspend fun deleteNotification(id: Long)

    @Query("DELETE FROM notifications")
    suspend fun clearAll()
}

@Dao
interface SettingDao {
    @Query("SELECT * FROM settings")
    fun getAllSettings(): Flow<List<SettingEntity>>

    @Query("SELECT value FROM settings WHERE `key` = :key LIMIT 1")
    suspend fun getSettingValue(key: String): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setSetting(setting: SettingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setSettings(settings: List<SettingEntity>)
}
