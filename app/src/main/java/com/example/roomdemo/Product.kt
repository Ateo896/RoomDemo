package com.example.roomdemo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "productName")
    var productName: String = "",

    @ColumnInfo(name = "quantity")
    var quantity: Int = 0
) {
    // Конструктор для удобства
    constructor(productName: String, quantity: Int) : this(
        id = 0,
        productName = productName,
        quantity = quantity
    )
}