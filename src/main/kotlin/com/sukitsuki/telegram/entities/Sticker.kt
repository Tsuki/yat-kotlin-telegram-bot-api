package com.sukitsuki.telegram.entities

import com.google.gson.annotations.SerializedName as Name

/**
 * This object represents a sticker.
 *
 * @property fileId Unique identifier for this file.
 * @property width Sticker width.
 * @property height Sticker height.
 * @property thumb Sticker thumbnail in .webp or .jpg format.
 * @property emoji Emoji associated with the sticker.
 * @property fileSize File size.
 */
data class Sticker(
        @Name("file_id") val fileId: String,
        val width: Int,
        val height: Int,
        val thumb: PhotoSize?,
        val emoji: String?,
        @Name("file_size") val fileSize: Int?)