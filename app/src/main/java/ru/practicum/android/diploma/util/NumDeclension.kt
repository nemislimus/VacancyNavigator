package ru.practicum.android.diploma.util


fun declension(
    num: Int,
    str: String
): String {
    var result: String
    var count: Int = num % N100
    val expressions = str.split("|") as MutableList<String>
    expressions.forEachIndexed { i, value ->
        expressions[i] = value.trim()
    }

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

    if (!result.contains("%d")) {
        result = "%d $result"
    }

    return result.replace("%d", num.toString())
}

private const val N100: Int = 100
private const val N5: Int = 5
private const val N20: Int = 20
private const val N10: Int = 10
private const val N4: Int = 4
private const val N3: Int = 3

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
