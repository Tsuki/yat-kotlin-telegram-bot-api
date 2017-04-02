package com.sukitsuki.telegram.entities

import com.google.gson.annotations.SerializedName as Name

/**
 * This object represents a general file (as opposed to photos, voice messages and audio files).
 *
 * @property fileId Unique file identifier.
 * @property thumb Document thumbnail as defined by sender.
 * @property fileName Original filename as defined by sender.
 * @property mimeType MIME type of the file as defined by sender.
 * @property fileSize File size.
 */
data class Document(
        @com.google.gson.annotations.SerializedName("file_id") val fileId: String,
        val thumb: PhotoSize?,
        @com.google.gson.annotations.SerializedName("file_name") val fileName: String?,
        @com.google.gson.annotations.SerializedName("mime_type") val mimeType: String?,
        @com.google.gson.annotations.SerializedName("file_size") val fileSize: Int?)