package com.sukitsuki.telegram

import com.google.gson.Gson
import com.sukitsuki.telegram.handler.UpdateHandler
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServerOptions
import mu.KLogging


abstract class TelegramBot internal constructor() : TelegramBotService {
    companion object : KLogging()

    abstract fun listen(maxId: Long, handler: UpdateHandler): Long

    val vertx: Vertx = Vertx.vertx()
    val options = HttpServerOptions()
    val gson = Gson()
}