package com.app.func.utils

import android.content.Context
import com.app.func.R

object Validation {
    enum class ValidateError {
        NO_ERROR, APP_E030, APP_E026, APP_E021, APP_E008, APP_E031, APP_E008_ROOM_NAME
    }

    fun getErrorMessage(error: ValidateError, context: Context): String? {
        val stringId = when (error) {
            ValidateError.APP_E021 -> R.string.name_contain_special_character
            ValidateError.APP_E008 -> R.string.name_is_blank_error
            ValidateError.APP_E026 -> R.string.input_phone_number
            ValidateError.APP_E030 -> R.string.fill_number_phone_please
            ValidateError.APP_E031 -> R.string.error_validation_room_name
            ValidateError.APP_E008_ROOM_NAME -> R.string.error_validation_room_name_empty
            else -> -1
        }
        return if (stringId != -1) {
            context.getString(stringId)
        } else null
    }
}