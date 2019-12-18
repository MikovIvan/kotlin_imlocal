package ru.imlocal.models

data class ShopRating(
    val userId: Int,
    val shopId: Int,
    val rating: Int
) {
}