package com.demoapp.myapplication.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.NumberFormat

@Entity
data class Item (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val itemName: String = "",
    @ColumnInfo(name = "price")
    val itemPrice: Double = 0.0,
    @ColumnInfo(name = "quantity")
    val quantityInStock: Int = 0,
)

fun Item.getFormattedPrice() : String = NumberFormat.getCurrencyInstance().format(itemPrice)