package com.kumadev.kumastream.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kumadev.kumastream.data.local.dao.CategoryDao
import com.kumadev.kumastream.data.local.dao.EventDao
import com.kumadev.kumastream.data.local.entity.CategoryEntity
import com.kumadev.kumastream.data.local.entity.EventEntity

@Database(
    entities = [EventEntity::class, CategoryEntity::class],
    version = 1,
    exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class KumaStreamDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        const val NAME = "kumastream.db"
    }
}
