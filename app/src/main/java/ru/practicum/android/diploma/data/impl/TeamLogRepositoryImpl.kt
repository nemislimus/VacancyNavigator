package ru.practicum.android.diploma.data.impl

import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import ru.practicum.android.diploma.domain.repository.TeamLogRepository
import kotlin.math.min

class TeamLogRepositoryImpl(private val analytics: FirebaseAnalytics) : TeamLogRepository {

    private val fireBaseEnabled = true
    private val notValidChars = Regex("""[^a-z0-9_]""", RegexOption.IGNORE_CASE)
    private val notValidStartChars = Regex("""^[^a-z]+""", RegexOption.IGNORE_CASE)

    override fun d(tag: String, value: String) {

        Log.d(tag, value)

        if (fireBaseEnabled) {

            val validKey = getKey(tag)

            val validValue = getValue(value)

            if (validKey.isNotBlank()) {

                if (validValue.isNotBlank()) {

                    analytics.logEvent(validKey) {
                        param(getValue(validValue + "_value"), validValue)
                    }

                } else {

                    analytics.logEvent(validKey, null)
                }
            } else {

                throw IllegalArgumentException("Ключ не подходит $tag для Firebase")
            }
        }
    }

    override fun d(eventName: String, eventParams: Map<String, String>) {

        Log.d(eventName, eventParams.toString())

        if (fireBaseEnabled) {

            val validEventParams = Bundle()

            eventParams.forEach { (key, value) ->

                val validKey = getKey(key)

                val validValue = getValue(value)

                if (validKey.isNotBlank() && validValue.isNotBlank()) {
                    validEventParams.putString(validKey, validValue)

                } else {
                    throw IllegalArgumentException("$eventName: $key -> $value не подходит для Firebase")
                }
            }

            //Параметров может быть не больше 25
            if (validEventParams.size() in 1..25) {

                analytics.logEvent(eventName, validEventParams)
            }
        }
    }

    private fun getKey(key: String): String {
        //Ключ должен быть не длинее 40 символов
        //соделжать только латинские буквы, цифры и подчеркивания
        //начинаться обазательно с буквы
        val clearString = notValidStartChars.replace(notValidChars.replace(key, ""), "")
        return clearString.substring(0..min(40, clearString.length - 1))
    }

    private fun getValue(value: String): String {
        //Значение должно быть не длинее 100 символов

        return value.substring(0, min(100, value.length - 1))
    }
}
