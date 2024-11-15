package ru.practicum.android.diploma.data.impl

import android.util.Log
import kotlinx.coroutines.delay
import ru.practicum.android.diploma.data.db.DbHelper
import ru.practicum.android.diploma.data.db.converters.AreaDtoToTempAreaItemMapper
import ru.practicum.android.diploma.data.db.converters.IndustryDtoToTempIndustryMapper
import ru.practicum.android.diploma.data.db.dao.OnStartUpdateDao
import ru.practicum.android.diploma.data.network.ApiRequest
import ru.practicum.android.diploma.data.network.ApiResponse
import ru.practicum.android.diploma.data.network.api.NetworkClient
import ru.practicum.android.diploma.data.search.dto.model.AreaDto
import ru.practicum.android.diploma.data.search.dto.model.IndustryDto
import ru.practicum.android.diploma.domain.models.AreaType


class UpdateDbOnAppStartImpl(
    private val client: NetworkClient,
    private val room: OnStartUpdateDao,
    private val sql: DbHelper
) : ru.practicum.android.diploma.domain.repository.UpdateDbOnAppStart {

    private val types = mapOf(
        0 to AreaType.COUNTRY, 1 to AreaType.REGION, 2 to AreaType.CITY
    )

    override suspend fun update(): Boolean {
        room.clearTempTable()

        try {
            when (val response = client.doRequest(ApiRequest.Country)) {
                is ApiResponse.CountryResponse -> {
                    response.result.forEach { country ->
                        room.insertArea(AreaDtoToTempAreaItemMapper.map(country))
                    }
                }

                else -> throw IllegalArgumentException("Не удалось получить страны ${response.resultCode}")
            }

            when (val response = client.doRequest(ApiRequest.AreasAll)) {
                is ApiResponse.AreaResponse -> {
                    insertAreas(response.areas, 0)
                }

                else -> throw IllegalArgumentException("Не удалось получить регионы")
            }

            val db = sql.writableDatabase

            db.beginTransaction()
            db.execSQL("DROP TABLE areas")
            db.execSQL("ALTER TABLE areas_temp RENAME TO areas")
            db.execSQL("CREATE TABLE areas_temp AS SELECT * FROM areas LIMIT 0")
            db.setTransactionSuccessful()
            db.endTransaction()

            when (val response = client.doRequest(ApiRequest.Industry)) {
                is ApiResponse.IndustryResponse -> {
                    insertIndustry(response.result)
                }

                else -> throw IllegalArgumentException("Не удалось получить индустрии")
            }

            db.beginTransaction()
            db.execSQL("DROP TABLE industry")
            db.execSQL("ALTER TABLE industry_temp RENAME TO industry")
            db.execSQL("CREATE TABLE industry_temp AS SELECT * FROM industry LIMIT 0")
            db.setTransactionSuccessful()
            db.endTransaction()
        } catch (er: IllegalArgumentException) {
            Log.d("WWW", "$er")

            return false
        }

        return true
    }

    private suspend fun insertAreas(areas: List<AreaDto>?, level: Int = 0): Boolean {
        if (areas.isNullOrEmpty()) {
            return false
        }

        types[level]?.let { type ->
            areas.forEach { area ->
                room.insertArea(AreaDtoToTempAreaItemMapper.map(area, type = type))

                insertAreas(area.areas, level + 1)
            }
        }
        return true
    }

    private suspend fun insertIndustry(list: List<IndustryDto>) {
        list.forEach { item ->
            room.insertIndustry(IndustryDtoToTempIndustryMapper.map(item))

            item.industries?.let {
                insertIndustry(it)
            }
        }
    }
}
