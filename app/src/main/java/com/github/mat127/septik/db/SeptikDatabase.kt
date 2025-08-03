package com.github.mat127.septik.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.mat127.septik.db.converter.InstantConverter
import com.github.mat127.septik.db.dao.EmptyTimestampsDao
import com.github.mat127.septik.db.dao.StateDao
import com.github.mat127.septik.db.entity.EmptyTimestampEntity
import com.github.mat127.septik.db.entity.StateEntity

@Database(entities = [StateEntity::class, EmptyTimestampEntity::class],
    version=1,
    exportSchema = false
)
@TypeConverters(InstantConverter::class)
abstract class SeptikDatabase: RoomDatabase() {
    abstract fun stateDao(): StateDao
    abstract fun emptyDao(): EmptyTimestampsDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: SeptikDatabase? = null

        fun getDatabase(context: Context): SeptikDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    SeptikDatabase::class.java,
                    "septik_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}