package com.sukitsuki.telegram.entities

import com.google.gson.annotations.SerializedName as Name

/**
 * This object represents a video file.
 *
 * @property fileId Unique identifier for this file.
 * @property width Video width as defined by sender.
 * @property height Video height as defined by sender.
 * @property duration Duration of the video in seconds as defined by sender.
 * @property thumb Video thumbnail.
 * @property mimeType Mime type of a file as defined by sender.
 * @property fileSize File size.
 */
data class Video(
        @Name("file_id") val fileId: String,
        val width: Int,
        val height: Int,
        val duration: Int,
        val thumb: PhotoSize?,
        @Name("mime_type") val mimeType: String?,
        @Name("file_size") val fileSize: Int?)