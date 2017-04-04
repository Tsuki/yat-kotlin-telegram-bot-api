package com.sukitsuki.telegram.entities

import com.google.gson.annotations.SerializedName as Name

/**
 * This object represents one size of a photo or a file / sticker thumbnail.
 * 
 * @property fileId Unique identifier for this file.
 * @property width Photo width.
 * @property height Photo height.
 * @property fileSize File size.
 */
data class PhotoSize(
        @Name("file_id") val fileId: String,
        val width: Int,
        val height: Int,
        @Name("file_size") val fileSize: Int?)