package com.sukitsuki.telegram

import com.google.common.base.MoreObjects
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

class TelegramProperties(FILE_NAME: String = "telegram.properties") {
    val token: String
        get() = readProperties().getProperty(PROP_TOKEN) ?: throw RuntimeException("Token is not set in " + file.name)

    val hookUrl: String
        get() = readProperties().getProperty(HOOK_URL) ?: throw RuntimeException("HookUrl is not set in " + file.name)

    val webHook: Boolean
        get() = readProperties().getProperty(WEB_HOOK)?.toBoolean() ?: false

    val handleUnknown: Boolean
        get() = readProperties().getProperty(HANDLE_UNKNOWN)?.toBoolean() ?: true

    var lastId: Long
        get() = readProperties().getProperty(PROP_LAST_ID)?.toLong() ?: 0
        set(lastId) = writeProperties(readProperties().apply {
            setProperty(PROP_LAST_ID, lastId.toString())
        })
    
    val admin: List<Long>
        get() = readProperties().getProperty(ADMIN)?.split(",")?.map(String::toLong) ?: emptyList()

    private fun writeProperties(props: Properties) {
        FileOutputStream(file).use { output -> props.store(output, null) }
    }

    private fun readProperties() = Properties().apply {
        FileInputStream(file).use { input -> load(input) }
    }
    fun readProperties(filename:String) = Properties().apply {
        val file: File = when {
            File(filename).exists() -> File(filename)
            else -> File(::TelegramProperties.javaClass.getResource("/$filename").toURI())
        }
        FileInputStream(file).use { input -> load(input) }
    }

    override fun toString(): String {
        return MoreObjects.toStringHelper(this)
                .add("hookUrl", hookUrl)
                .add("webHook", webHook)
                .add("handleUnknown", handleUnknown)
                .add("lastId", lastId)
                .add("token", token)
                .toString()
    }

    val file: File = when {
        File(FILE_NAME).exists() -> File(FILE_NAME)
        else -> File(::TelegramProperties.javaClass.getResource("/$FILE_NAME").toURI())
    }

    companion object {
        val PROP_TOKEN = "token"
        val HANDLE_UNKNOWN = "handleUnknown"
        val PROP_LAST_ID = "lastId"
        val HOOK_URL = "hookUrl"
        val WEB_HOOK = "webHook"
        val ADMIN = "admin"
    }

}
