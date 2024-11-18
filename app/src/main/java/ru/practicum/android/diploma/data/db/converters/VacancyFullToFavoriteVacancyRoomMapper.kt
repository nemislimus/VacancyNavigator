package ru.practicum.android.diploma.data.db.converters

import ru.practicum.android.diploma.data.db.models.FavoriteVacancyRoom
import ru.practicum.android.diploma.domain.models.Geolocation
import ru.practicum.android.diploma.domain.models.Salary
import ru.practicum.android.diploma.domain.models.VacancyFull
import ru.practicum.android.diploma.domain.models.VacancyShort

object VacancyFullToFavoriteVacancyRoomMapper {
    private const val DELIMITER = "#XXX-TEAM-DELIMITER#"
    private const val MILLISECONDS_IN_SECOND = 1000

    data class DetektSalary(
        val from: Int = -1,
        val to: Int = -1,
        val currency: String = ""
    )

    data class DetektGeolocation(
        val lat: String = "",
        val lng: String = ""
    )

    fun map(vacancy: VacancyFull): FavoriteVacancyRoom {
        val detektSalary = help(vacancy.salary)
        val geo = help(vacancy.geolocation)

        with(vacancy) {
            return FavoriteVacancyRoom(
                id = id.toInt(),
                name = name,
                employer = employer,
                areaName = areaName,
                iconUrl = iconUrl ?: "",
                from = detektSalary.from,
                to = detektSalary.to,
                currency = detektSalary.currency,
                experience = experience,
                employment = employment,
                schedule = schedule,
                description = description,
                keySkills = keySkills.joinToString(DELIMITER),
                address = address ?: "",
                lat = geo.lat,
                lng = geo.lng,
                urlHh = urlHh,
                lastMod = timestamp()
            )
        }
    }

    fun map(list: List<FavoriteVacancyRoom>): List<VacancyShort> = list.map(::toShort)

    fun toFull(vacancy: FavoriteVacancyRoom?): VacancyFull? {
        vacancy?.let {
            with(it) {
                return VacancyFull(
                    id = id.toString(),
                    name = name,
                    employer = employer,
                    areaName = areaName,
                    iconUrl = iconUrl,
                    salary = salary(vacancy),
                    experience = experience,
                    employment = employment,
                    schedule = schedule,
                    description = description,
                    keySkills = keySkills.split(DELIMITER),
                    address = address,
                    geolocation = geolocation(vacancy),
                    urlHh = urlHh
                )
            }
        }

        return null
    }

    fun toShort(vacancy: FavoriteVacancyRoom): VacancyShort {
        with(vacancy) {
            return VacancyShort(
                id = id.toString(),
                name = name,
                employer = employer,
                areaName = areaName,
                iconUrl = iconUrl,
                salary = salary(vacancy),
                geolocation = geolocation(vacancy)
            )
        }
    }

    private fun timestamp(): Int {
        return (System.currentTimeMillis() / MILLISECONDS_IN_SECOND).toInt()
    }

    private fun help(salary: Salary?): DetektSalary {
        var ds = DetektSalary()
        salary?.let { sal ->
            sal.to?.let {
                ds = ds.copy(to = it)
            }
            sal.from?.let {
                ds = ds.copy(from = it)
            }
            ds = ds.copy(currency = sal.currency)
        }
        return ds
    }

    private fun help(geolocation: Geolocation?): DetektGeolocation {
        geolocation?.let {
            return DetektGeolocation(
                lat = it.lat,
                lng = it.lng
            )
        }
        return DetektGeolocation()
    }

    private fun salary(vacancy: FavoriteVacancyRoom): Salary? {
        with(vacancy) {
            if (from == -1 && to == -1) {
                return null
            }

            return Salary(
                from = if (from == -1) null else from,
                to = if (to == -1) null else to,
                currency = currency
            )
        }
    }

    private fun geolocation(vacancy: FavoriteVacancyRoom): Geolocation? {
        with(vacancy) {
            return if (lat.isNotBlank() && lng.isNotBlank()) {
                Geolocation(
                    lat = lat,
                    lng = lng
                )
            } else {
                null
            }
        }
    }
}
