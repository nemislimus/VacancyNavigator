package ru.practicum.android.diploma.util


interface NumDeclension {
    fun declension(
        num: Int,
        str: String
    ): String {
        return declension(num.toLong(), str)
    }

    fun declension(
        num: Long,
        str: String
    ): String {
        var result: String
        var count: Long = num % N100
        val expressions = str.split("|") as MutableList<String>
        expressions.forEachIndexed { i, value ->
            expressions[i] = value.trim()
        }

        require(
            expressions.size == LENGTH
        ) {
            return "Укажите все возможные словоформы. Например \"кот кота котов\" -> \"1 кот, 2 кота, 5 котов\""
        }

        if (count in N5..N20) {
            result = expressions[2]
        } else {
            count %= N10
            result = when (count) {
                1L -> expressions[0]
                in 2L..N4 -> expressions[1]
                else -> expressions[2]
            }
        }

        if (!result.contains("%d")) {
            result = "%d $result"
        }

        return result.replace("%d", num.toString())
    }

    fun longFromString(salary: String): Long {
        return salary.replace(Regex("""[^0-9]"""), "").let {
            if (it.isNotBlank()) it.toLong() else 0
        }
    }

    fun threeZeroFormat(salary: String): String {
        return salary.replace(
            Regex("""(\d{1,3}|\G\d{3})(?=(?:\d{3})+(?!\d))"""),
            "$1 "
        ).trim()
    }

    companion object {
        private const val N100: Long = 100
        private const val N5: Long = 5
        private const val N20: Long = 20
        private const val N10: Long = 10
        private const val N4: Long = 4
        private const val LENGTH: Int = 3
    }
}

/*
Пример использования. Добавляем к классу реализацию интерфейса
class SomeClass(....) : SomeParentClass(), NumDeclension
После этого внутри класса можем вызывать
for(i in 0..10){
    println(declension(i, "Найдена %d вакансия | Найдены %d вакансии | Найдено %d вакансий"))
}

Вывод будет такой:
Найдено 0 вакансий
Найдена 1 вакансия
Найдены 2 вакансии
Найдены 3 вакансии
Найдены 4 вакансии
Найдено 5 вакансий
Найдено 6 вакансий
Найдено 7 вакансий
Найдено 8 вакансий
Найдено 9 вакансий
Найдено 10 вакансий
*/
