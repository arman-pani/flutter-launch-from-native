package com.example.myapplication

import kotlinx.serialization.Serializable

@Serializable
data class ProductModel(
    val name: String,
    val price: Double,
    val description: String,
    val textColor: String,
    val textSize: Double,
    val textStyle: String,
    val imageBytes: ByteArray,
)