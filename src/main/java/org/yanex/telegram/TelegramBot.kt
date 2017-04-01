package org.yanex.telegram

import org.yanex.telegram.handler.UpdateHandler


abstract class TelegramBot internal constructor() : TelegramBotService {
    abstract fun listen(maxId: Long, handler: UpdateHandler): Long
}