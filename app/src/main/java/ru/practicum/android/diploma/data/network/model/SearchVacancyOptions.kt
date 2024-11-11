package ru.practicum.android.diploma.data.network.model

class SearchVacancyOptions(
    var text: String,
    var page: Int = 0,
    var areaId: String? = null,
    var industryId: String? = null,
    var salary: Int? = null,
    var onlyWithSalary: Boolean? = null,
) {
    fun toMap(): Map<String, String> {
        val map = mutableMapOf<String, String>()
        map["text"] = text
        map["page"] = page.toString()
        areaId?.let {
            map["area"] = it
        }
        industryId?.let {
            map["industry"] = it
        }
        salary?.let {
            map["salary"] = it.toString()
        }
        areaId?.let {
            map["area"] = it
        }
        onlyWithSalary?.let {
            map["area"] = it.toString()
        }

        return map

    }
}
