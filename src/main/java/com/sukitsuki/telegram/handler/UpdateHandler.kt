package com.sukitsuki.telegram.handler

import com.sukitsuki.telegram.Response
import com.sukitsuki.telegram.entities.Update

interface UpdateHandler {
    
    fun handleUpdate(update: Update)

    /**
     * Handle an error response.
     *
     * @return False if should stop querying for updates, true to continue processing.
     */
    fun onError(response: Response<*>): Boolean
}