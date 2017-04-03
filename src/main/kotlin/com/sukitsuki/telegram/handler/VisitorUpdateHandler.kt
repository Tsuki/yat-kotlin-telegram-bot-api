package com.sukitsuki.telegram.handler

import com.sukitsuki.telegram.Response
import com.sukitsuki.telegram.entities.Message
import com.sukitsuki.telegram.entities.Update
import org.joda.time.DateTime

class VisitorUpdateHandler(val visitor: UpdateVisitor) : UpdateHandler {
    override fun onError(response: Response<*>) = visitor.onError(response)
    override fun handleUpdate(update: Update) = visitor.processUpdate(update)

    private fun UpdateVisitor.processUpdate(update: Update) {
        val result = when {
            update.message != null -> {
                if ((DateTime().millis / 1000) > update.message.date + 6000) {
                    handleNewMessage(update, update.message)
                };true
            }
            update.editedMessage != null -> visitEdited(update, update.editedMessage)
            update.inlineQuery != null -> visitInlineQuery(update, update.inlineQuery)
            update.chosenInlineResult != null -> visitChosenInlineResult(update, update.chosenInlineResult)
            update.callbackQuery != null -> visitCallbackQuery(update, update.callbackQuery)
            else -> false
        }

        if (!result) {
            visitUpdate(update)
        }
    }

    private fun UpdateVisitor.handleNewMessage(update: Update, message: Message): Boolean {
        return if (message.text != null) {
            visitText(update, message, message.text)
        } else if (message.audio != null) {
            visitAudio(update, message, message.audio)
        } else if (message.photo != null) {
            visitPhoto(update, message, message.photo, message.caption)
        } else if (message.document != null) {
            visitDocument(update, message, message.document)
        } else if (message.sticker != null) {
            visitSticker(update, message, message.sticker)
        } else if (message.video != null) {
            visitVideo(update, message, message.video, message.caption)
        } else if (message.contact != null) {
            visitContact(update, message, message.contact)
        } else if (message.venue != null) {
            visitVenue(update, message, message.venue)
        } else if (message.location != null) {
            visitLocation(update, message, message.location)
        } else {
            false
        }
    }
}