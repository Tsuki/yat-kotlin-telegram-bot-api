package com.sukitsuki.telegram

import com.google.gson.Gson
import com.sukitsuki.telegram.handler.UpdateHandler
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServer
import io.vertx.core.http.HttpServerOptions
import mu.KLogging
import okhttp3.OkHttpClient
import retrofit2.Retrofit


abstract class TelegramBot internal constructor() : TelegramBotService {
    companion object : KLogging()

    abstract fun listen(maxId: Long, handler: UpdateHandler): Long

    val vertx: Vertx = Vertx.vertx()
    val options = HttpServerOptions()
    val gson = Gson()
    lateinit var server: HttpServer
    lateinit var properties: TelegramProperties
    lateinit var client: OkHttpClient
}