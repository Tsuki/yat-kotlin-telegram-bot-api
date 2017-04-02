package com.sukitsuki.telegram

import mu.KLogging
import com.sukitsuki.telegram.handler.UpdateHandler


abstract class TelegramBot internal constructor() : TelegramBotService {
    companion object: KLogging()
    abstract fun listen(maxId: Long, handler: UpdateHandler): Long
}