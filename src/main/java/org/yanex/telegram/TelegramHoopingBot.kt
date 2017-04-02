package org.yanex.telegram

import com.google.gson.Gson
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServerOptions
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import okhttp3.logging.HttpLoggingInterceptor.Level.BASIC
import org.yanex.telegram.entities.Update
import org.yanex.telegram.handler.StopProcessingException
import org.yanex.telegram.handler.UpdateHandler
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class TelegramHoopingBot internal constructor(
        serviceProvider: TelegramBotService) : TelegramBot(), TelegramBotService by serviceProvider {
    val vertx = Vertx.vertx()

    companion object {
        @JvmStatic
        fun create(properties: TelegramProperties, timeout: Int = 30, logLevel: Level = BASIC): TelegramBot {
            val bot: TelegramHoopingBot
//            val vertx = Vertx.vertx()

            val logging = HttpLoggingInterceptor().apply {
                level = logLevel
            }

            val httpClient = OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(timeout + 10L, TimeUnit.SECONDS)
                    .readTimeout(timeout + 10L, TimeUnit.SECONDS)
                    .writeTimeout(timeout + 10L, TimeUnit.SECONDS)
                    .build()

            val adapter = Retrofit.Builder()
                    .baseUrl("https://api.telegram.org/bot${properties.token}/")
                    .addConverterFactory(GsonConverterFactory.create(Gson()))
                    .client(httpClient)
                    .build()

            bot = TelegramHoopingBot(adapter.create(TelegramBotService::class.java))
            val response = bot.setWebhook(properties.hookUrl).execute()
            if (!response.isSuccessful) {
                logger.error("Set web hook ${properties.hookUrl} error message: ${response.errorBody().string()}")
                throw StopProcessingException()
            }
            return bot
        }
    }

    override fun listen(maxId: Long, handler: UpdateHandler): Long {
        val gson = Gson()
        val options = HttpServerOptions()
        options.logActivity = true
        vertx.createHttpServer(options)
                .requestHandler {
                    val response = it.response()
                    response.statusCode = 200
                    response.end()
                    it.bodyHandler { body ->
                        val update = gson.fromJson(body.toString(), Update::class.java)
                        try {
                            handler.handleUpdate(update)
                        } catch(e: StopProcessingException) {
                            System.exit(0)
                        }
                    }
                }.listen(8443)
        return 0
    }
}