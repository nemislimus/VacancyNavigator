package ru.practicum.android.diploma.domain.models

enum class DataLoadingStatus(val level: Int, val code: Int?, val message: String) {
    APP_FIRST_START(0, null, "Приложение только запустилось"),
    NO_INTERNET(0, -1, "Показываем ошибку нет сети"),
    LOADING(0, 0, "Можно крутить прелоадер загрузки"),
    SERVER_ERROR(0, 1, "Показываем ошибку сервера"),
    COMPLETE(1, 2, "Данные загружены. Можно делать запросы");

    companion object {
        fun find(code: Int?): DataLoadingStatus {
            return DataLoadingStatus.entries.firstOrNull { it.code == code } ?: APP_FIRST_START
        }
    }
}
