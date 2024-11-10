package ru.practicum.android.diploma.util

import java.util.Locale

interface NumDeclension {
    fun declension(
        num: Int,
        str: String,
        n100: Int = 100, // эта херня ниже для detekt
        n5: Int = 5, // ее указывать не нужно
        n20: Int = 20,
        n10: Int = 10,
        n4: Int = 4
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
