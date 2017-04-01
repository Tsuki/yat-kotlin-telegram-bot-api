package org.yanex.telegram

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

class TelegramProperties {
    val token: String
        get() = readProperties().getProperty(PROP_TOKEN) ?: throw RuntimeException("Token is not set in " + FILE_NAME)

    var lastId: Long
        get() = readProperties().getProperty(PROP_LAST_ID)?.toLong() ?: 0
        set(lastId) = writeProperties(readProperties().apply {
            setProperty(PROP_LAST_ID, lastId.toString())
        })

    val handleUnknown: Boolean
        get() = readProperties().getProperty(HANDLE_UNKNOWN)?.toBoolean() ?: false

    private fun writeProperties(props: Properties) {
        FileOutputStream(file).use { output -> props.store(output, null) }
    }

    private fun readProperties() = Properties().apply {
        FileInputStream(file).use { input -> load(input) }
    }

    private companion object {
        val FILE_NAME = "telegram.properties"

        val file: File = when {
            File(FILE_NAME).exists() -> File(FILE_NAME)
            else -> File(::TelegramProperties.javaClass.getResource("/$FILE_NAME").toURI())
        }

        val PROP_TOKEN = "token"
        val HANDLE_UNKNOWN = "handleUnknown"
        val PROP_LAST_ID = "lastId"
    }

}
