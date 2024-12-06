package ru.practicum.android.diploma.data.db.converters

import ru.practicum.android.diploma.data.db.models.DataLoadingStatusRoom
import ru.practicum.android.diploma.domain.models.DataLoadingStatus

object DataLoadingStatusToDataLoadingStatusRoomMapper {
    private const val APP_START_STATUS_CODE = -7

    fun map(status: DataLoadingStatusRoom?): DataLoadingStatus {
        return if (status == null || status.code == APP_START_STATUS_CODE) {
            DataLoadingStatus.APP_FIRST_START
        } else {
            DataLoadingStatus.find(
                code = status.code
            )
        }
    }

    fun map(status: DataLoadingStatus) = DataLoadingStatusRoom(
        level = status.level,
        code = status.code ?: APP_START_STATUS_CODE,
        message = status.message
    )
}
