package ru.practicum.android.diploma.domain.models

enum class AreaType(val type: String) {
    COUNTRY("country"), REGION("region"), CITY("city");

    companion object {
        fun find(type: String): AreaType {
            return AreaType.entries.firstOrNull { it.type == type } ?: CITY
        }
    }
}
