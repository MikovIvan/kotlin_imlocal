package ru.imlocal.models

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
}