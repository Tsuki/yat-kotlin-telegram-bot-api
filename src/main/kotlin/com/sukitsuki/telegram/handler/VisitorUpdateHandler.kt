package com.sukitsuki.telegram.handler

import com.sukitsuki.telegram.Response
import com.sukitsuki.telegram.entities.Message
import com.sukitsuki.telegram.entities.Update
import org.joda.time.DateTime

class VisitorUpdateHandler(val visitor: UpdateVisitor) : UpdateHandler {
    override fun onError(response: Response<*>) = visitor.onError(response)
    override fun handleUpdate(update: Update) = visitor.processUpdate(update)

    private fun UpdateVisitor.processUpdate(update: Update) {
        var result = true
        when {
            update.message != null ->
                if ((DateTime().millis / 1000) < update.message.date + 1800) {
                    handleNewMessage(update, update.message)
                }
            update.editedMessage != null -> visitEdited(update, update.editedMessage)
            update.inlineQuery != null -> visitInlineQuery(update, update.inlineQuery)
            update.chosenInlineResult != null -> visitChosenInlineResult(update, update.chosenInlineResult)
            update.callbackQuery != null -> visitCallbackQuery(update, update.callbackQuery)
            else -> result = false
        }

        if (!result) {
            visitUpdate(update)
        }
    }

    private fun UpdateVisitor.handleNewMessage(update: Update, message: Message): Boolean {
        return when {
            message.text != null -> visitText(update, message, message.text)
            message.audio != null -> visitAudio(update, message, message.audio)
            message.photo != null -> visitPhoto(update, message, message.photo, message.caption)
            message.document != null -> visitDocument(update, message, message.document)
            message.sticker != null -> visitSticker(update, message, message.sticker)
            message.video != null -> visitVideo(update, message, message.video, message.caption)
            message.contact != null -> visitContact(update, message, message.contact)
            message.venue != null -> visitVenue(update, message, message.venue)
            message.location != null -> visitLocation(update, message, message.location)
            else -> false
        }
    }
}