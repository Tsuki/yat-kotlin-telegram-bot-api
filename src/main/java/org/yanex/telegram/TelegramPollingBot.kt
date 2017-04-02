package org.yanex.telegram

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.yanex.telegram.handler.StopProcessingException
import org.yanex.telegram.handler.UpdateHandler
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class TelegramPollingBot internal constructor(
        serviceProvider: TelegramBotService,
        val timeout: Int = 30) : TelegramBot(), TelegramBotService by serviceProvider {
    companion object {
        @JvmStatic
        fun create(properties: TelegramProperties, timeout: Int = 30, logLevel: Level = Level.BASIC): TelegramBot {
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
            val bot = TelegramPollingBot(adapter.create(TelegramBotService::class.java), timeout)
            val response = bot.deleteWebhook().execute()
            if (!response.isSuccessful) {
                logger.error("Delete web hook error message: ${response.errorBody().string()}")
                throw StopProcessingException()
            }
            return bot
        }
    }

    override fun listen(maxId: Long, handler: UpdateHandler): Long {
        var currentMaxId: Long = maxId
        outer@ while (true) {
            val call = if (currentMaxId > 0)
                getUpdates(offset = currentMaxId, timeout = timeout)
            else
                getUpdates(timeout = timeout)

            val response = call.execute()

            //TODO can we do anything with this?
            if (!response.isSuccessful) continue
            val responseBody = response.body() ?: continue

            val updates = responseBody.result ?: if (handler.onError(responseBody)) continue else break
            for (update in updates) {
                try {
                    handler.handleUpdate(update)
                } catch (e: StopProcessingException) {
                    currentMaxId = update.updateId + 1
                    break@outer
                }
                currentMaxId = update.updateId + 1
            }
        }
        return currentMaxId
    }
}