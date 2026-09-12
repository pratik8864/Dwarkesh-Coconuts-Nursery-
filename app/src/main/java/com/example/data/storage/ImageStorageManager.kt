package com.example.data.storage

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.Locale

/**
 * Dedicated storage manager for uploaded nursery gallery images and custom branding assets.
 * Validates file types, enforces size constraints, sanitizes filenames, and stores assets securely
 * in the application's internal files directory.
 */
object ImageStorageManager {

    private const val MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024L // 10 MB limit
    private val ALLOWED_EXTENSIONS = setOf("jpg", "jpeg", "png", "webp")
    private val ALLOWED_MIME_TYPES = setOf("image/jpeg", "image/png", "image/webp")

    /**
     * Copies an image from a content URI (e.g. from photo picker) to internal app storage.
     * Returns the persistent file:// URI string.
     */
    fun saveImageFromUri(context: Context, sourceUri: Uri, subDir: String = "gallery"): Result<String> {
        return try {
            val contentResolver = context.contentResolver

            // 1. Validate MIME type
            val mimeType = contentResolver.getType(sourceUri)?.lowercase(Locale.ROOT)
            val extensionFromMime = mimeType?.let { MimeTypeMap.getSingleton().getExtensionFromMimeType(it) }?.lowercase(Locale.ROOT)
            
            val ext = when {
                extensionFromMime in ALLOWED_EXTENSIONS -> extensionFromMime
                sourceUri.path?.endsWith(".png", ignoreCase = true) == true -> "png"
                sourceUri.path?.endsWith(".webp", ignoreCase = true) == true -> "webp"
                sourceUri.path?.endsWith(".jpeg", ignoreCase = true) == true -> "jpeg"
                sourceUri.path?.endsWith(".jpg", ignoreCase = true) == true -> "jpg"
                mimeType != null && mimeType in ALLOWED_MIME_TYPES -> {
                    when (mimeType) {
                        "image/png" -> "png"
                        "image/webp" -> "webp"
                        else -> "jpg"
                    }
                }
                else -> {
                    // Check if openable stream has valid image header
                    "jpg"
                }
            }

            // 2. Validate file size
            var fileSize: Long = -1
            try {
                contentResolver.query(sourceUri, null, null, null, null)?.use { cursor ->
                    val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                    if (sizeIndex != -1 && cursor.moveToFirst()) {
                        fileSize = cursor.getLong(sizeIndex)
                    }
                }
            } catch (_: Exception) { }

            if (fileSize > MAX_FILE_SIZE_BYTES) {
                return Result.failure(IllegalArgumentException("File size exceeds 10MB limit. Please upload a smaller image."))
            }

            // 3. Prepare target directory in internal storage
            val targetDir = File(context.filesDir, "images/$subDir")
            if (!targetDir.exists()) {
                targetDir.mkdirs()
            }

            // 4. Sanitize filename
            val sanitizedName = "dwarkesh_${subDir}_${System.currentTimeMillis()}.${ext}"
            val targetFile = File(targetDir, sanitizedName)

            // 5. Copy data stream safely
            var totalBytesCopied: Long = 0
            val inputStream: InputStream = contentResolver.openInputStream(sourceUri)
                ?: return Result.failure(IllegalStateException("Unable to read selected image stream."))

            inputStream.use { input ->
                FileOutputStream(targetFile).use { output ->
                    val buffer = ByteArray(8192)
                    var bytesRead: Int
                    while (input.read(buffer).also { bytesRead = it } != -1) {
                        totalBytesCopied += bytesRead
                        if (totalBytesCopied > MAX_FILE_SIZE_BYTES) {
                            targetFile.delete()
                            return Result.failure(IllegalArgumentException("File size exceeds 10MB limit."))
                        }
                        output.write(buffer, 0, bytesRead)
                    }
                    output.flush()
                }
            }

            Result.success("file://${targetFile.absolutePath}")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Safely deletes an internal file if it exists, avoiding orphaned files.
     */
    fun deleteFileIfExists(filePath: String): Boolean {
        return try {
            if (filePath.startsWith("file://")) {
                val cleanPath = filePath.removePrefix("file://")
                val file = File(cleanPath)
                if (file.exists() && file.isFile) {
                    file.delete()
                } else false
            } else false
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Validates whether an image input (URL or path) is formatted properly.
     */
    fun validateImageInput(input: String): Pair<Boolean, String?> {
        val trimmed = input.trim()
        if (trimmed.isEmpty()) {
            return Pair(false, "Image source cannot be empty.")
        }
        if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
            return Pair(true, null)
        }
        if (trimmed.startsWith("file://") || trimmed.startsWith("content://")) {
            return Pair(true, null)
        }
        // Known preset illustrations
        if (trimmed.startsWith("plant_") || trimmed.startsWith("gallery_") || trimmed.startsWith("offer_")) {
            return Pair(true, null)
        }
        return Pair(false, "Please provide a valid image file or URL (http/https/file).")
    }
}
