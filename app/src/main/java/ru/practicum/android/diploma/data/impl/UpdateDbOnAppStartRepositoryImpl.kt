package ru.practicum.android.diploma.data.impl

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CONFLICT_IGNORE
import android.util.Log
import ru.practicum.android.diploma.data.db.DbHelper
import ru.practicum.android.diploma.data.db.converters.AreaDtoToTempAreaItemMapper
import ru.practicum.android.diploma.data.db.converters.IndustryDtoToTempIndustryMapper
import ru.practicum.android.diploma.data.db.models.AreaRoomTemp
import ru.practicum.android.diploma.data.db.models.IndustryRoomTemp
import ru.practicum.android.diploma.data.network.ApiRequest
import ru.practicum.android.diploma.data.network.ApiResponse
import ru.practicum.android.diploma.data.network.api.NetworkClient
import ru.practicum.android.diploma.data.search.dto.model.AreaDto
import ru.practicum.android.diploma.data.search.dto.model.IndustryDto
import ru.practicum.android.diploma.domain.models.AreaType
import ru.practicum.android.diploma.domain.models.DataLoadingStatus
import ru.practicum.android.diploma.domain.repository.DataLoadingStatusRepository
import ru.practicum.android.diploma.domain.repository.UpdateDbOnAppStartRepository

class UpdateDbOnAppStartRepositoryImpl(
    private val client: NetworkClient,
    private val sql: DbHelper,
    private var roomDb: DataLoadingStatusRepository
) : UpdateDbOnAppStartRepository {

    private val db: SQLiteDatabase by lazy { sql.writableDatabase }
    private var time = System.currentTimeMillis()
    private val countriesIds: MutableMap<String, Boolean> = mutableMapOf()
    private var countryCounter = 1
    private var regionCounter = 1
    private var cityCounter = 1

    override suspend fun update(): Boolean {
        roomDb.setStatus(DataLoadingStatus.LOADING)

        clearTempTables()
        db.execSQL("CREATE TABLE IF NOT EXISTS $AREAS_NO_INDEXES AS SELECT * FROM areas_temp LIMIT 0")
        db.execSQL("CREATE TABLE IF NOT EXISTS $INDUSTRY_NO_INDEXES AS SELECT * FROM industry_temp LIMIT 0")

        try {
            getCountries()

            getAreas()

            getIndustries()

            db.beginTransaction()
            db.execSQL("ALTER TABLE areas RENAME TO _areas")
            db.execSQL("ALTER TABLE areas_temp RENAME TO areas")
            db.execSQL("ALTER TABLE _areas RENAME TO areas_temp")

            db.execSQL("ALTER TABLE industry RENAME TO _industry")
            db.execSQL("ALTER TABLE industry_temp RENAME TO industry")
            db.execSQL("ALTER TABLE _industry RENAME TO industry_temp")
            db.setTransactionSuccessful()
            db.endTransaction()

            logTime("таблицы в BD заменены")

            clearTempTables()
            sql.close()
        } catch (er: IllegalArgumentException) {
            Log.d("WWW", "$er")

            sql.close()

            roomDb.setStatus(DataLoadingStatus.SERVER_ERROR)

            return false
        }

        roomDb.setStatus(DataLoadingStatus.COMPLETE)

        return true
    }

    override suspend fun setNoInternet() {
        roomDb.setStatus(DataLoadingStatus.NO_INTERNET)
    }

    private suspend fun clearTempTables() {
        db.execSQL("DELETE FROM areas_temp")
        db.execSQL("DELETE FROM industry_temp")
        db.execSQL("DROP TABLE IF EXISTS $AREAS_NO_INDEXES")
        db.execSQL("DROP TABLE IF EXISTS $INDUSTRY_NO_INDEXES")
    }

    private suspend fun getCountries() {
        when (val response = client.doRequest(ApiRequest.Country)) {
            is ApiResponse.CountryResponse -> {
                logTime("страны загружены")
                response.result.forEach { country ->
                    countriesIds[country.id] = true
                }
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
                db.execSQL("INSERT OR IGNORE INTO areas_temp SELECT * FROM $AREAS_NO_INDEXES")
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
                db.execSQL("INSERT OR IGNORE INTO industry_temp SELECT * FROM $INDUSTRY_NO_INDEXES")
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

        areas.forEach { area ->
            val type: AreaType
            val hhPosition: Int
            if (countriesIds[area.id] != null) {
                type = AreaType.COUNTRY
                hhPosition = countryCounter++
            } else if (countriesIds[area.parentId] != null) {
                type = AreaType.REGION
                hhPosition = regionCounter++
            } else if (area.parentId != null) {
                type = AreaType.CITY
                hhPosition = cityCounter++
            } else {
                type = AreaType.COUNTRY // это другие регионы
                hhPosition = -countryCounter
            }

            insertArea(
                area = AreaDtoToTempAreaItemMapper.map(area, type = type, nestingLevel = level),
                addAlsoAsCity = type == AreaType.REGION && area.areas.isNullOrEmpty(),
                hhPosition = hhPosition
            )

            insertAreas(area.areas, level + 1)
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

    private fun insertArea(area: AreaRoomTemp, addAlsoAsCity: Boolean = false, hhPosition: Int = 0) {
        val contentValues = ContentValues()
        contentValues.put(ID, area.id)
        contentValues.put(NAME, SPACE + area.name)
        contentValues.put(TYPE, area.type)
        contentValues.put(PARENT_ID, area.parentId)
        contentValues.put(NESTING_LEVEL, area.nestingLevel)
        contentValues.put(HH_POSITION, hhPosition)
        db.insertWithOnConflict(AREAS_NO_INDEXES, null, contentValues, CONFLICT_IGNORE)

        if (addAlsoAsCity) {
            // добавим Москву и другие федеральные города тоже в города
            val parentId = if (area.nestingLevel < 2) {
                area.id
            } else {
                area.parentId
            }
            val nestingLevel = area.nestingLevel + if (area.nestingLevel < 2) 1 else 0
            val contentValues = ContentValues()
            contentValues.put(ID, -1 * area.id)
            contentValues.put(NAME, SPACE + area.name)
            contentValues.put(TYPE, AreaType.CITY.type)
            contentValues.put(PARENT_ID, parentId)
            contentValues.put(NESTING_LEVEL, nestingLevel)
            contentValues.put(HH_POSITION, hhPosition)
            db.insertWithOnConflict(AREAS_NO_INDEXES, null, contentValues, CONFLICT_IGNORE)
        }
    }

    private fun insertIndustry(industry: IndustryRoomTemp) {
        val contentValues = ContentValues()
        contentValues.put(ID, industry.id)
        contentValues.put(NAME, industry.name)
        contentValues.put(PARENT_ID, industry.parentId)
        db.insertWithOnConflict(INDUSTRY_NO_INDEXES, null, contentValues, CONFLICT_IGNORE)
    }

    companion object {
        const val NAME = "name"
        const val TYPE = "type"
        const val ID = "id"
        const val PARENT_ID = "parentId"
        const val NESTING_LEVEL = "nestingLevel"
        const val AREAS_NO_INDEXES = "areas_no_indexes"
        const val INDUSTRY_NO_INDEXES = "industry_no_indexes"
        const val HH_POSITION = "hhPosition"
        const val SPACE = " "
    }
}
