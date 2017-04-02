package org.yanex.telegram

import mu.KLogging
import org.yanex.telegram.handler.UpdateHandler


abstract class TelegramBot internal constructor() : TelegramBotService {
    companion object: KLogging()
    abstract fun listen(maxId: Long, handler: UpdateHandler): Long
}