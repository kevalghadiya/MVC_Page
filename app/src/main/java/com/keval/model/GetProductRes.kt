package com.keval.model

data class GetProductRes(
    val limit: Int,
    val products: List<Product>,
    val skip: Int,
    val total: Int
) {
    data class Product(
        val id: Int,
        val price: Int,
        val title: String
    )
}