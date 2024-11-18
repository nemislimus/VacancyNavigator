package ru.practicum.android.diploma.util

/*
* утилита для обхода возмущения детект по поводу переноса логических выражений на новую строку
* Пример использования тут https://pl.kotl.in/kaGl9GVbD
* */

interface BooleanForDetekt {
    fun ifOneTrue(vararg list: Boolean): Boolean {
        list.forEach {
            if (it) return true
        }
        return false
    }

    fun ifAllTrue(vararg list: Boolean): Boolean {
        list.forEach {
            if (!it) return false
        }
        return true
    }
}
