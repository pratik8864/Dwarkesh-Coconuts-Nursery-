package com.example.data

import androidx.compose.runtime.compositionLocalOf

/**
 * Centralized Application Branding Configuration.
 * All frontend components that display branding read from this centralized configuration.
 * Admin updates to logo, app icon, or nursery name reflect dynamically throughout the app.
 */
data class BrandingConfig(
    val appName: String = "DWARKESH COCONUTS & NURSERY",
    val tagline: String = "Quality Plants • Healthy Growth • Trusted Service",
    val logoUrl: String = "", // Empty means use bundled official logo asset
    val appIconUrl: String = "", // Runtime app icon (headers, dialogs, PWA favicon)
    val faviconUrl: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)

val LocalBrandingConfig = compositionLocalOf { BrandingConfig() }
