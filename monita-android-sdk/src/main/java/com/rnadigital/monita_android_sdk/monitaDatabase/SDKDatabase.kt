package com.rnadigital.monita_android_sdk.monitaDatabase
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [RequestPayloadEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class SDKDatabase : RoomDatabase() {
    abstract fun requestPayloadDao(): RequestPayloadDao

    companion object {
        @Volatile
        private var INSTANCE: SDKDatabase? = null

        fun getDatabase(context: Context): SDKDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SDKDatabase::class.java,
                    "monita_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
