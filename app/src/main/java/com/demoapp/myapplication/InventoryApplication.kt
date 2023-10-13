package com.demoapp.myapplication

import android.app.Application
import com.demoapp.myapplication.data.ItemRoomDatabase

class InventoryApplication : Application() {
    val database: ItemRoomDatabase by lazy { ItemRoomDatabase.getDatabase(this) }
}