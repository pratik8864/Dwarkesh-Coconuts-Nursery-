package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ProductEntity::class,
        BookingEntity::class,
        GalleryItemEntity::class,
        ReviewEntity::class,
        OfferEntity::class,
        NotificationEntity::class,
        SettingEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun bookingDao(): BookingDao
    abstract fun galleryDao(): GalleryDao
    abstract fun reviewDao(): ReviewDao
    abstract fun offerDao(): OfferDao
    abstract fun notificationDao(): NotificationDao
    abstract fun settingDao(): SettingDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "dwarkesh_nursery.db"
                )
                .fallbackToDestructiveMigration()
                .addCallback(DatabaseCallback())
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                INSTANCE?.let { database ->
                    seedInitialData(database)
                }
            }
        }

        private suspend fun seedInitialData(database: AppDatabase) {
            // Seed Settings with official admin contact numbers
            val defaultSettings = listOf(
                SettingEntity("phone_1", "8200596044"),
                SettingEntity("phone_2", "9426477948"),
                SettingEntity("phone_3_name", "Vansh Ram"),
                SettingEntity("phone_3", "9328040045"),
                SettingEntity("admin_pin", "1234"),
                SettingEntity("nursery_name", "DWARKESH COCONUTS & NURSERY"),
                SettingEntity("nursery_tagline", "Quality Plants • Healthy Growth • Trusted Service"),
                SettingEntity("nursery_address", "Dwarkesh Coconuts & Nursery, Coastal Highway, Mahuva - Somnath Road, Gujarat"),
                SettingEntity("about_intro", "Dwarkesh Coconuts & Nursery is a premier agricultural nursery in Gujarat specializing in high-yield, disease-resistant coconut cultivars, hybrid varieties, and dwarf varieties for commercial farmers and gardeners."),
                SettingEntity("about_quality", "Every sapling is nurtured from certified seed coconuts with 100% genuine pedigree, robust root development, and verified high productivity."),
                SettingEntity("about_support", "We provide end-to-end guidance from soil preparation and plantation spacing to organic fertilization and irrigation management."),
                SettingEntity("about_delivery", "Safe and secure transport available across Gujarat and neighboring regions directly to your farm gate."),
                SettingEntity("branding_app_name", "DWARKESH COCONUTS & NURSERY"),
                SettingEntity("branding_tagline", "Quality Plants • Healthy Growth • Trusted Service"),
                SettingEntity("branding_logo_url", ""),
                SettingEntity("branding_icon_url", ""),
                SettingEntity("branding_favicon_url", ""),
                SettingEntity("branding_updated_at", System.currentTimeMillis().toString())
            )
            database.settingDao().setSettings(defaultSettings)

            // Seed Initial Products
            val defaultProducts = listOf(
                ProductEntity(
                    id = 1,
                    name = "Green Dwarf Coconut Plant",
                    gujaratiName = "લીલો વામન નાળિયેરનો રોપો",
                    description = "Fast-yielding dwarf coconut variety that starts fruiting within 3 to 4 years. High water content, sweet taste, and easy harvesting from low height.",
                    benefits = "Early fruiting (3-4 yrs), High water yield per nut (450-550ml), Compact tree height, Ideal for tender coconut sales.",
                    age = "12 Months",
                    price = 150.0,
                    oldPrice = 180.0,
                    discountPercent = 17,
                    stockQuantity = 1200,
                    isAvailable = true,
                    minOrderQuantity = 5,
                    mainImage = "plant_green_dwarf",
                    additionalImages = "plant_green_dwarf_1,plant_green_dwarf_2",
                    isFeatured = true,
                    category = "Dwarf Varieties"
                ),
                ProductEntity(
                    id = 2,
                    name = "Chowghat Orange Dwarf (COD)",
                    gujaratiName = "નારંગી વામન નાળિયેરનો રોપો",
                    description = "Highly attractive golden-orange tender coconuts with extremely sweet water. Very popular for ornamental planting as well as premium commercial tender water markets.",
                    benefits = "Super sweet tender water (Brix 6.5+), Distinctive bright orange appearance, Bears 200-250 nuts/year under good care.",
                    age = "12-14 Months",
                    price = 220.0,
                    oldPrice = 250.0,
                    discountPercent = 12,
                    stockQuantity = 850,
                    isAvailable = true,
                    minOrderQuantity = 5,
                    mainImage = "plant_orange_dwarf",
                    additionalImages = "plant_orange_dwarf_1",
                    isFeatured = true,
                    category = "Special Hybrids"
                ),
                ProductEntity(
                    id = 3,
                    name = "Hybrid T×D (Tall × Dwarf) Coconut",
                    gujaratiName = "સંકર ટી×ડી નાળિયેરનો રોપો",
                    description = "High yielding cross between Tall and Dwarf coconut varieties combining early fruiting with heavy nut yield and copra content.",
                    benefits = "Heavy bearer (250+ nuts/tree/yr), Copra content 190-210g per nut, High wind and stress resistance.",
                    age = "14 Months",
                    price = 250.0,
                    oldPrice = 290.0,
                    discountPercent = 14,
                    stockQuantity = 1500,
                    isAvailable = true,
                    minOrderQuantity = 10,
                    mainImage = "plant_txd_hybrid",
                    additionalImages = "plant_txd_hybrid_1",
                    isFeatured = true,
                    category = "Special Hybrids"
                ),
                ProductEntity(
                    id = 4,
                    name = "West Coast Tall (WCT) Coconut",
                    gujaratiName = "વેસ્ટ કોસ્ટ ટોલ નાળિયેરનો રોપો",
                    description = "Traditional long-lifespan coconut palm capable of high oil yield and durable wood. Well adapted to coastal and tropical sandy soils.",
                    benefits = "Lifespan 80-100 years, High copra oil percentage, Strong resistance to seasonal drought and salinity.",
                    age = "12 Months",
                    price = 130.0,
                    oldPrice = 150.0,
                    discountPercent = 13,
                    stockQuantity = 2200,
                    isAvailable = true,
                    minOrderQuantity = 10,
                    mainImage = "plant_wct_tall",
                    additionalImages = "plant_wct_tall_1",
                    isFeatured = false,
                    category = "Coconut Plants"
                ),
                ProductEntity(
                    id = 5,
                    name = "Malaysian Yellow Dwarf (MYD)",
                    gujaratiName = "મલેશિયન યલો વામન રોપો",
                    description = "Premium exotic dwarf coconut palm bearing bright yellow nuts with bountiful sweet coconut water and tender pulp.",
                    benefits = "First harvest in 3.5 years, Excellent drought tolerance, Beautiful yellow fronds and clusters.",
                    age = "14 Months",
                    price = 280.0,
                    oldPrice = 320.0,
                    discountPercent = 12,
                    stockQuantity = 600,
                    isAvailable = true,
                    minOrderQuantity = 5,
                    mainImage = "plant_myd_yellow",
                    additionalImages = "plant_myd_yellow_1",
                    isFeatured = true,
                    category = "Dwarf Varieties"
                ),
                ProductEntity(
                    id = 6,
                    name = "Gangabondam Green Dwarf",
                    gujaratiName = "ગંગાબોન્દમ ગ્રીન નાળિયેરનો રોપો",
                    description = "Traditional south Indian coastal variety famous for large water volume (over 600ml) per nut and robust disease resilience.",
                    benefits = "Extra-large tender nut size, High sweetness index, Strong root collar.",
                    age = "10 Months",
                    price = 240.0,
                    oldPrice = 270.0,
                    discountPercent = 11,
                    stockQuantity = 700,
                    isAvailable = true,
                    minOrderQuantity = 5,
                    mainImage = "plant_gangabondam",
                    additionalImages = "plant_gangabondam_1",
                    isFeatured = false,
                    category = "Coconut Plants"
                )
            )
            database.productDao().insertAll(defaultProducts)

            // Seed Initial Offers
            val defaultOffers = listOf(
                OfferEntity(
                    id = 1,
                    title = "Monsoon Plantation Festival Offer",
                    gujaratiTitle = "ચોમાસુ વાવેતર મહોત્સવ ઓફર",
                    description = "Special direct-from-nursery discount of ₹25 per plant on orders of 50 or more saplings. Includes free bio-fertilizer starter pack.",
                    discountText = "Flat ₹25 OFF / Plant on 50+ Qty",
                    imageUrl = "offer_monsoon",
                    startDate = "2026-06-01",
                    endDate = "2026-10-31",
                    isActive = true
                ),
                OfferEntity(
                    id = 2,
                    title = "Free Farm Gate Delivery Scheme",
                    gujaratiTitle = "ખેડૂત સીધી ફ્રી ડિલિવરી યોજના",
                    description = "Free transportation and doorstep farm unloading across Saurashtra & Gujarat on advance orders of 100+ hybrid plants.",
                    discountText = "100% Free Delivery on 100+ Plants",
                    imageUrl = "offer_delivery",
                    startDate = "2026-08-01",
                    endDate = "2026-12-31",
                    isActive = true
                )
            )
            database.offerDao().insertAll(defaultOffers)

            // Seed Initial Gallery
            val defaultGallery = listOf(
                GalleryItemEntity(id = 1, title = "Certified Coconut Sapling Nursery Beds", category = "Nursery", imageUrl = "gallery_nursery_1", displayOrder = 1),
                GalleryItemEntity(id = 2, title = "Heavy Bearing Mother Palm Trees", category = "Coconut Plants", imageUrl = "gallery_mother_palm", displayOrder = 2),
                GalleryItemEntity(id = 3, title = "Farmer Plantation Layout Support", category = "Plantation", imageUrl = "gallery_plantation_1", displayOrder = 3),
                GalleryItemEntity(id = 4, title = "Safe Truck Delivery to Farm", category = "Plant Delivery", imageUrl = "gallery_delivery_truck", displayOrder = 4),
                GalleryItemEntity(id = 5, title = "Happy Farmer with 3-Year Dwarf Harvest", category = "Customers", imageUrl = "gallery_customer_happy", displayOrder = 5),
                GalleryItemEntity(id = 6, title = "Nursery Root Development Inspection", category = "Farm", imageUrl = "gallery_farm_inspection", displayOrder = 6),
                GalleryItemEntity(id = 7, title = "Guidance on Pit Digging & Organic Manure", category = "Videos", imageUrl = "gallery_video_guide", displayOrder = 7)
            )
            database.galleryDao().insertAll(defaultGallery)

            // Seed Initial Approved Reviews
            val defaultReviews = listOf(
                ReviewEntity(
                    id = 1,
                    customerName = "Ramesh Patel",
                    villageOrCity = "Junagadh",
                    rating = 5,
                    reviewText = "Ordered 120 Green Dwarf plants from Dwarkesh Nursery last season. 100% survival rate! Vansh Ram bhai gave very practical tips on organic fertilizer. Highly satisfied!",
                    isApproved = true,
                    isFeatured = true,
                    dateString = "August 2026"
                ),
                ReviewEntity(
                    id = 2,
                    customerName = "Bhavesh Ahir",
                    villageOrCity = "Mahuva",
                    rating = 5,
                    reviewText = "Dwarkesh Coconuts offers genuine pedigree plants. Look at the root system on their saplings, absolutely top class. Best nursery in Gujarat.",
                    isApproved = true,
                    isFeatured = true,
                    dateString = "July 2026"
                ),
                ReviewEntity(
                    id = 3,
                    customerName = "Govindbhai Solanki",
                    villageOrCity = "Veraval / Somnath",
                    rating = 5,
                    reviewText = "Delivered right to my farm gate without a single leaf damaged. The Chowghat Orange plants are already showing vibrant growth.",
                    isApproved = true,
                    isFeatured = false,
                    dateString = "June 2026"
                )
            )
            database.reviewDao().insertAll(defaultReviews)

            // Seed Initial Sample Booking so customer & admin have an instant receipt to view
            val defaultBooking = BookingEntity(
                bookingId = "DWK-2026-0001",
                customerName = "Jayeshbhai Patel",
                mobileNumber = "9825123456",
                plantId = 1,
                plantName = "Green Dwarf Coconut Plant",
                gujaratiPlantName = "લીલો વામન નાળિયેરનો રોપો",
                quantity = 50,
                unitPrice = 150.0,
                totalAmount = 7500.0,
                advanceAmount = 2500.0,
                remainingAmount = 5000.0,
                deliveryType = "Home Delivery",
                address = "Near Hanuman Temple, Farm Plot #12",
                villageCity = "Talala",
                district = "Gir Somnath",
                bookingDate = "2026-09-10",
                notes = "Please deliver during morning hours. Call before loading.",
                deliveryStatus = "Order Confirmed",
                paymentStatus = "Partially Paid",
                timestamp = System.currentTimeMillis() - 86400000L
            )
            database.bookingDao().insertBooking(defaultBooking)

            // Seed Initial Welcome Notification
            val welcomeNotification = NotificationEntity(
                id = 1,
                title = "Welcome to Dwarkesh Coconuts & Nursery",
                message = "Explore our high-yielding coconut saplings and book directly with advance farm delivery support.",
                type = "general",
                timestamp = System.currentTimeMillis(),
                isRead = false
            )
            database.notificationDao().insertNotification(welcomeNotification)
        }
    }
}
