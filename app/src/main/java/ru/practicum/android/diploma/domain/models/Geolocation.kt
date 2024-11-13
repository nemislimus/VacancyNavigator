package ru.practicum.android.diploma.domain.models

data class Geolocation(
    val lat: String, // 54.997006
    val lng: String // 73.346356
)

/*
информация лежит в поле address (может быть null)
{
	"city": "Омск",
	"street": "Кемеровская улица",
	"building": "2А",
	"lat": 54.997006,
	"lng": 73.346356,
	"description": null,
	"raw": "Омск, Кемеровская улица, 2А",
	"metro": null,
	"metro_stations": []
}
*/
