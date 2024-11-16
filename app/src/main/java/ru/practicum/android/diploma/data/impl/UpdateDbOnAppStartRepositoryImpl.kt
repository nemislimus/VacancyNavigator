package ru.practicum.android.diploma.data.impl

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CONFLICT_IGNORE
import android.util.Log
import ru.practicum.android.diploma.data.db.DbHelper
import ru.practicum.android.diploma.data.db.converters.AreaDtoToTempAreaItemMapper
import ru.practicum.android.diploma.data.db.converters.IndustryDtoToTempIndustryMapper
import ru.practicum.android.diploma.data.db.dao.CreateDbDao
import ru.practicum.android.diploma.data.db.models.AreaRoomTemp
import ru.practicum.android.diploma.data.db.models.IndustryRoomTemp
import ru.practicum.android.diploma.data.network.ApiRequest
import ru.practicum.android.diploma.data.network.ApiResponse
import ru.practicum.android.diploma.data.network.api.NetworkClient
import ru.practicum.android.diploma.data.search.dto.model.AreaDto
import ru.practicum.android.diploma.data.search.dto.model.IndustryDto
import ru.practicum.android.diploma.domain.models.AreaType
import ru.practicum.android.diploma.domain.repository.UpdateDbOnAppStartRepository


class UpdateDbOnAppStartRepositoryImpl(
    private val client: NetworkClient,
    private val sql: DbHelper,
    private var roomDb: CreateDbDao?
) : UpdateDbOnAppStartRepository {

    private val types = mapOf(
        0 to AreaType.COUNTRY, 1 to AreaType.REGION, 2 to AreaType.CITY
    )

    private val db: SQLiteDatabase by lazy { sql.writableDatabase }
    private val areasNoIndexes = "areas_no_indexes"
    private val industryNoIndexes = "industry_no_indexes"
    private var time = System.currentTimeMillis()

    override suspend fun update(): Boolean {
        logTime("roomDb -> " + roomDb?.version())

        roomDb = null

        db.execSQL("CREATE TABLE IF NOT EXISTS $areasNoIndexes AS SELECT * FROM areas_temp LIMIT 0")
        db.execSQL("CREATE TABLE IF NOT EXISTS $industryNoIndexes AS SELECT * FROM industry_temp LIMIT 0")
        db.execSQL("DELETE FROM $areasNoIndexes")
        db.execSQL("DELETE FROM $industryNoIndexes")

        try {
            getCountries()

            getAreas()

            getIndustries()

            db.beginTransaction()
            db.execSQL("ALTER TABLE areas RENAME TO _areas")
            db.execSQL("ALTER TABLE areas_temp RENAME TO areas")
            db.execSQL("ALTER TABLE _areas RENAME TO areas_temp")
            db.execSQL("DELETE FROM areas_temp")

            db.execSQL("ALTER TABLE industry RENAME TO _industry")
            db.execSQL("ALTER TABLE industry_temp RENAME TO industry")
            db.execSQL("ALTER TABLE _industry RENAME TO industry_temp")
            db.setTransactionSuccessful()
            db.endTransaction()

            logTime("таблицы в BD заменены")

            db.execSQL("DROP TABLE $areasNoIndexes")
            db.execSQL("DROP TABLE $industryNoIndexes")
            sql.close()

        } catch (er: IllegalArgumentException) {

            Log.d("WWW", "$er")

            sql.close()

            return false
        }

        return true
    }

    private suspend fun getCountries() {
        when (val response = client.doRequest(ApiRequest.Country)) {
            is ApiResponse.CountryResponse -> {
                logTime("страны загружены")
                db.beginTransaction()
                response.result.forEach { country ->
                    insertArea(AreaDtoToTempAreaItemMapper.map(country))
                }
                db.execSQL("INSERT OR IGNORE INTO areas_temp SELECT * FROM $areasNoIndexes")
                db.execSQL("DELETE FROM $areasNoIndexes")
                db.setTransactionSuccessful()
                db.endTransaction()
            }

            else -> throw IllegalArgumentException("Не удалось получить страны ${response.resultCode}")
        }

        logTime("страны обработаны")
    }

    private suspend fun getAreas() {
        when (val response = client.doRequest(ApiRequest.AreasAll)) {
            is ApiResponse.AreaResponse -> {
                logTime("регионы загружены")
                db.beginTransaction()
                insertAreas(response.areas, 0)
                db.execSQL("INSERT OR IGNORE INTO areas_temp SELECT * FROM $areasNoIndexes")
                db.setTransactionSuccessful()
                db.endTransaction()
            }

            else -> throw IllegalArgumentException("Не удалось получить регионы")
        }

        logTime("регионы обработаны")
    }

    private suspend fun getIndustries() {
        when (val response = client.doRequest(ApiRequest.Industry)) {
            is ApiResponse.IndustryResponse -> {
                logTime("индустрии загружены")
                db.beginTransaction()
                insertIndustries(response.result)
                db.execSQL("INSERT OR IGNORE INTO industry_temp SELECT * FROM $industryNoIndexes")
                db.setTransactionSuccessful()
                db.endTransaction()
            }

            else -> throw IllegalArgumentException("Не удалось получить индустрии")
        }

        logTime("индустрии обработаны")
    }

    private fun logTime(message: String) {
        val newTime = System.currentTimeMillis()
        Log.d("WWW", "$message: " + (newTime - time).toString())
        time = newTime
    }

    private fun insertAreas(areas: List<AreaDto>?, level: Int = 0): Boolean {
        if (areas.isNullOrEmpty()) {
            return false
        }

        types[level]?.let { type ->
            areas.forEach { area ->
                insertArea(AreaDtoToTempAreaItemMapper.map(area, type = type))

                insertAreas(area.areas, level + 1)
            }
        }
        return true
    }

    private fun insertIndustries(list: List<IndustryDto>) {
        list.forEach { item ->
            insertIndustry(IndustryDtoToTempIndustryMapper.map(item))

            item.industries?.let {
                insertIndustries(it)
            }
        }
    }

    private fun insertArea(area: AreaRoomTemp) {
        val contentValues = ContentValues()
        contentValues.put("id", area.id)
        contentValues.put("name", area.name)
        contentValues.put("type", area.type)
        contentValues.put("parentId", area.parentId)
        db.insertWithOnConflict(areasNoIndexes, null, contentValues, CONFLICT_IGNORE)
    }

    private fun insertIndustry(industry: IndustryRoomTemp) {
        val contentValues = ContentValues()
        contentValues.put("id", industry.id)
        contentValues.put("name", industry.name)
        contentValues.put("parentId", industry.parentId)
        db.insertWithOnConflict(industryNoIndexes, null, contentValues, CONFLICT_IGNORE)
    }
}
