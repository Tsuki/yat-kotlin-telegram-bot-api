package com.sukitsuki.telegram

import com.google.gson.Gson
import com.sukitsuki.telegram.entities.Update
import com.sukitsuki.telegram.handler.StopProcessingException
import com.sukitsuki.telegram.handler.UpdateHandler
import okhttp3.*
import okhttp3.Response
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

            val httpClient = OkHttpClient.Builder()
                    .addNetworkInterceptor(NetInterceptor())
                    .addInterceptor(logging)
                    .cache(Cache(File("Cache"), cacheSize))
                    .build()

            val adapter = Retrofit.Builder()
                    .baseUrl("https://api.telegram.org/bot${properties.token}/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build()

            val bot = TelegramHoopingBot(adapter.create(TelegramBotService::class.java))
            bot.properties = properties
            bot.client = httpClient
            return bot
        }
    }

    class NetInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response? {
            val cacheControl = CacheControl.Builder()
                    .maxAge(1, TimeUnit.HOURS)
                    .maxStale(1, TimeUnit.DAYS)
                    .build()

            return chain.proceed(chain.request()).newBuilder()
                    .header("Cache-Control", cacheControl.toString())
                    .removeHeader("Pragma").build()
        }
    }

    override fun listen(maxId: Long, handler: UpdateHandler): Long {
        options.logActivity = true
        server = vertx.createHttpServer(options)
        .requestHandler {
            val response = it.response()
            it.bodyHandler { body ->
                if (body.bytes.isEmpty()){
                    return@bodyHandler
                }
                val update = gson.fromJson(body.toString(), Update::class.java)
                try {
                    handler.handleUpdate(update)
                } catch(e: StopProcessingException) {
                    response.statusCode = 200
                    response.end("{\"ok\": true}")
                    exit(0)
                }
                response.statusCode = 200
                response.end("{\"ok\": true}")
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
