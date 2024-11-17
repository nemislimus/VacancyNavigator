package ru.practicum.android.diploma.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale

fun formatterSalary(salary: Int?): String? {
    if (salary == null) {
        return null
    } else {
        val formatter: DecimalFormat = NumberFormat.getInstance(Locale.US) as DecimalFormat
        val symbols: DecimalFormatSymbols = formatter.getDecimalFormatSymbols()
        symbols.setGroupingSeparator(' ')
        formatter.setDecimalFormatSymbols(symbols)
        return (formatter.format(salary))
    }
}
