package ru.imlocal.models

import java.util.*

private fun String.shortStreetType(): String {
    return when {
        toLowerCase(Locale.getDefault()).contains("улица") -> replace("улица", "ул.")
        toLowerCase(Locale.getDefault()).contains("проспект") -> replace("проспект", "пр.")
        toLowerCase(Locale.getDefault()).contains("бульвар") -> replace("бульвар", "бул.")
        toLowerCase(Locale.getDefault()).contains("переулок") -> replace("переулок", "пер.")
        toLowerCase(Locale.getDefault()).contains("набережная") -> replace("набережная", "наб.")
        toLowerCase(Locale.getDefault()).contains("аллея") -> replace("аллея", "ал.")
        toLowerCase(Locale.getDefault()).contains("площадь") -> replace("площадь", "пл.")
        else -> this
    }
}

data class Address(
    val id: Int,
    val city: String,
    val street: String,
    val houseNumber: String,
    val building: String,
    val housing: String,
    val latitude: Double,
    val longitude: Double
) {
    override fun toString(): String {
        return "г. $city, ${street.shortStreetType()}, $houseNumber"
    }
}