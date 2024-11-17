package ru.practicum.android.diploma.domain.models

data class Salary(
    val from: Int? = null,
    val to: Int? = null,
    val currency: String // RUR, USD + тут надо будет реализовать конвертер в символ
) {
    companion object {
        private const val NO_SALARY = "Зарплата не указана"
        private const val JUST_FROM = 0
        private const val JUST_TO = 1
        private const val FULL_SALARY = 2

        private const val RUB_SYMBOL = "₽"
        private const val DOLLAR_SYMBOL = "$"
        private const val EURO_SYMBOL = "€"
        private const val BELORUSSIAN_RUB_SYMBOL = "Br"
        private const val KAZAKHSTAN_TENGE_SYMBOL = "₸"
        private const val UKRAINE_GRIVNA_SYMBOL = "₴"
        private const val AZERBAIJAN_MANAT_SYMBOL = "₼"
        private const val GEORGIA_LARI_SYMBOL = "₾"

        fun getStringSalaryValue(salary: Salary?): String {
            return if (salary == null) {
                NO_SALARY
            } else {
                when (getSalaryType(salary)) {
                    FULL_SALARY -> "от ${salary.from} до ${salary.to} ${getCurrencySymbol(salary.currency)}"
                    JUST_FROM -> "от ${salary.from} ${getCurrencySymbol(salary.currency)}"
                    else -> "${salary.to} ${getCurrencySymbol(salary.currency)}"
                }
            }
        }

        private fun getSalaryType(salary: Salary): Int {
            return if (salary.from != null && salary.to != null) {
                FULL_SALARY
            } else {
                if (salary.to == null) JUST_FROM else JUST_TO
            }
        }

        private fun getCurrencySymbol(code: String): String {
            return when (code) {
                "RUB", "RUR" -> RUB_SYMBOL
                "BYR" -> BELORUSSIAN_RUB_SYMBOL
                "USD" -> DOLLAR_SYMBOL
                "EUR" -> EURO_SYMBOL
                "KZT" -> KAZAKHSTAN_TENGE_SYMBOL
                "UAH" -> UKRAINE_GRIVNA_SYMBOL
                "AZN" -> AZERBAIJAN_MANAT_SYMBOL
                "GEL" -> GEORGIA_LARI_SYMBOL
                else -> code
            }
        }
    }
}
