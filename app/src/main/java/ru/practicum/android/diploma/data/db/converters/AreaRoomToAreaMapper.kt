package ru.practicum.android.diploma.data.db.converters

import ru.practicum.android.diploma.data.db.models.AreaRoom
import ru.practicum.android.diploma.domain.models.Area
import ru.practicum.android.diploma.domain.models.AreaType
import kotlin.math.abs

object AreaRoomToAreaMapper {
    fun map(area: AreaRoom): Area = Area(
        id = abs(area.id).toString(),
        name = area.name.trim(),
        type = AreaType.find(area.type),
        parentId = if (area.parentId > 0) area.parentId.toString() else null
    )

    fun map(list: List<AreaRoom>): List<Area> = list.map { area ->
        map(area)
    }
}
