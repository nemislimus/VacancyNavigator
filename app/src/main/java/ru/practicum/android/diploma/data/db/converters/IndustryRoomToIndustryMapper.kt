package ru.practicum.android.diploma.data.db.converters

import ru.practicum.android.diploma.data.db.models.IndustryRoom
import ru.practicum.android.diploma.domain.models.Industry

object IndustryRoomToIndustryMapper {
    fun map(industry: IndustryRoom): Industry = Industry(
        id = industry.id,
        name = industry.name,
        parentId = if (industry.parentId > 0) industry.parentId.toString() else null
    )

    fun map(list: List<IndustryRoom>): List<Industry> {
        return list.map { industry ->
            map(industry)
        }
    }
}
