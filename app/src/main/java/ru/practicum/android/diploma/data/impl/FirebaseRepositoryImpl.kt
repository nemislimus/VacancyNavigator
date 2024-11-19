package ru.practicum.android.diploma.data.impl

import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import ru.practicum.android.diploma.domain.repository.FirebaseRepository
import kotlin.math.min

class FirebaseRepositoryImpl(private val analytics: FirebaseAnalytics) : FirebaseRepository {

    private val fireBaseEnabled = true
    private val notValidChars = Regex("""[^a-z0-9_]""", RegexOption.IGNORE_CASE)
    private val notValidStartChars = Regex("""^[^a-z]+""", RegexOption.IGNORE_CASE)

    /*
    log.d(tag: String, value: String)
    tag - Название события. Должно содержать от 1 до 40 символов [a-z0-9] или знаков подчеркивания.
    Название должно начинаться с буквенного символа. Короче те же правила, что для названия переменных

    value - может содержать до 100 абсолютно любых символов

    Например:
    log.d("viewScreen", "Экран поиска вакансий")
    log.d("viewScreen", "Экран избранных вакансий")
     */

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
                Log.d(tag, "Ключ не подходит $tag для Firebase")
            }
        }
    }

    /*
    log.d(eventName: String, eventParams: Map<String, String>)
    eventName - Название события. Должно содержать от 1 до 40 символов [a-z0-9] или знаков подчеркивания.
    Название должно начинаться с буквенного символа. Короче те же правила, что для названия переменных

    eventParams - можно передать до 25 параментров в виде ключ - значение
    Например:
    log.d("addVacancyToFav", mapOf(
        "city" to "Moscow",
        "rubles" to "280_000",
        "company" to "Yandex"
    ))
     */

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
                    Log.d(eventName, "$key -> $value не подходит для Firebase")
                }
            }

            // Параметров может быть не больше 25
            if (validEventParams.size() in 1..N25) {
                analytics.logEvent(eventName, validEventParams)
            }
        }
    }

    private fun getKey(key: String): String {
        // Ключ должен быть не длинее 40 символов
        // соделжать только латинские буквы, цифры и подчеркивания
        // начинаться обазательно с буквы
        val clearString = notValidStartChars.replace(notValidChars.replace(key, ""), "")
        return clearString.substring(0..min(N40, clearString.length - 1))
    }

    private fun getValue(value: String): String {
        // Значение должно быть не длинее 100 символов

        return value.substring(0, min(N100, value.length - 1))
    }

    companion object {
        const val N25 = 25
        const val N40 = 40
        const val N100 = 100
    }
}
