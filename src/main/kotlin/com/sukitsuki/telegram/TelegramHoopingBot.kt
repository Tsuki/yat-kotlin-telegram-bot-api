package com.sukitsuki.telegram

import com.google.gson.Gson
import com.sukitsuki.telegram.entities.Update
import com.sukitsuki.telegram.handler.StopProcessingException
import com.sukitsuki.telegram.handler.UpdateHandler
import io.vertx.core.http.HttpServer
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import okhttp3.logging.HttpLoggingInterceptor.Level.BASIC
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.lang.System.exit
import java.util.concurrent.TimeUnit


class TelegramHoopingBot internal constructor(
        serviceProvider: TelegramBotService) :
        TelegramBot(), TelegramBotService by serviceProvider {
    companion object {
        @JvmStatic
        fun create(properties: TelegramProperties, logLevel: Level = BASIC): TelegramBot {
            val logging = HttpLoggingInterceptor().apply {
                level = logLevel
            }
            val cacheSize = 10 * 1024 * 1024L // 10 MiB
            val cache = Cache(File("Cache"), cacheSize)

            val httpClient = OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .cache(cache)
                    .build()
            val adapter = Retrofit.Builder()
                    .baseUrl("https://api.telegram.org/bot${properties.token}/")
                    .addConverterFactory(GsonConverterFactory.create(Gson()))
                    .client(httpClient)
                    .build()

            val bot = TelegramHoopingBot(adapter.create(TelegramBotService::class.java))
            bot.properties = properties
            bot.client = httpClient
            return bot
        }
    }

    override fun listen(maxId: Long, handler: UpdateHandler): Long {
//        options.logActivity = true
        server = vertx.createHttpServer(options)
                .requestHandler {
                    val response = it.response()
                    response.statusCode = 200
                    response.end()
                    it.bodyHandler { body ->
                        val update = gson.fromJson(body.toString(), Update::class.java)
                        try {
                            handler.handleUpdate(update)
                        } catch(e: StopProcessingException) {
                            exit(0)
                        }
                    }
                }.listen(8443)
        val response = this.setWebhook(properties.hookUrl).execute()
        if (!response.isSuccessful) {
            logger.error("Set web hook ${properties.hookUrl} error message: ${response.errorBody().string()}")
            exit(0)
        }
        return 0
    }
}