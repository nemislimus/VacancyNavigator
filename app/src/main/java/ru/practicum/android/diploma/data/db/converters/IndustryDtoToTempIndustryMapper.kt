package ru.practicum.android.diploma.data.db.converters

import ru.practicum.android.diploma.data.db.models.IndustryRoomTemp
import ru.practicum.android.diploma.data.search.dto.model.IndustryDto

object IndustryDtoToTempIndustryMapper {
    fun map(item: IndustryDto): IndustryRoomTemp {
        var parentId = 0
        val matchResult = "(?<id>\\d+)\\.\\d+".toRegex().find(item.id)

        matchResult?.let {
            it.groups["id"]?.let { id ->
                parentId = id.value.toInt()
            }
        }

        return IndustryRoomTemp(
            id = item.id,
            name = item.name,
            parentId = parentId
        )
    }
}
