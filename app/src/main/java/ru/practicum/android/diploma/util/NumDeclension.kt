package ru.practicum.android.diploma.util

import java.util.Locale

interface NumDeclension {
    fun declension(
        num: Int,
        str: String
    ): String {
        val result: String
        var count: Int = num % n100
        val expressions = str.split(" ")

        if (Locale.getDefault().language.lowercase() == "ru") {
            if (count in n5..n20) {
                result = expressions[2]
            } else {
                count %= n10
                result = when (count) {
                    1 -> expressions[0]
                    in 2..n4 -> expressions[1]
                    else -> expressions[2]
                }
            }
        } else {
            result = if (num == 1) {
                expressions[0]
            } else {
                expressions[1]
            }
        }

        return "$num $result"
    }

    companion object {
        const val n100: Int = 100
        const val n5: Int = 5
        const val n20: Int = 20
        const val n10: Int = 10
        const val n4: Int = 4
    }
}

/*
Пример использования. Добавляем к классу реализацию интерфейса
class SomeClass(....) : SomeParentClass(), NumDeclension
После этого внутри класса можем вызывать
for(i in 0..10){
    Log.d("WWW", declension(i, "пирожок пирожка пирожков"))
}
Вывод будет такой:
0 пирожков
1 пирожок
2 пирожка
3 пирожка
4 пирожка
5 пирожков
6 пирожков
7 пирожков
8 пирожков
9 пирожков
10 пирожков
*/
