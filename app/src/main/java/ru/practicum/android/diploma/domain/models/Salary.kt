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

        fun getStringSalaryValue(salary: Salary?): String {
            return if (salary == null) {
                NO_SALARY
            } else {
                when (getSalaryType(salary)) {
                    FULL_SALARY -> "от ${salary.from} до ${salary.to} ${salary.currency}"
                    JUST_FROM -> "от ${salary.from} ${salary.currency}"
                    else -> "до ${salary.to} ${salary.currency}"
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
    }
}
