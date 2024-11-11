package ru.practicum.android.diploma.util

interface NumDeclension {
    fun declension(
        num: Int,
        str: String
    ): String {
        val result: String
        var count: Int = num % N100
        val expressions = str.split(" ")

        require(
            expressions.size == N3
        ) {
            return "Укажите все возможные словоформы. Например \"кот кота котов\" -> \"1 кот, 2 кота, 5 котов\""
        }

        if (count in N5..N20) {
            result = expressions[2]
        } else {
            count %= N10
            result = when (count) {
                1 -> expressions[0]
                in 2..N4 -> expressions[1]
                else -> expressions[2]
            }
        }

        return "$num $result"
    }

    companion object {
        const val N100: Int = 100
        const val N5: Int = 5
        const val N20: Int = 20
        const val N10: Int = 10
        const val N4: Int = 4
        const val N3: Int = 3
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
