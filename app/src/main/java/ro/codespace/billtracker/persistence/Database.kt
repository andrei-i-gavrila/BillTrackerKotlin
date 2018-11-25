package ro.codespace.billtracker.persistence

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import ro.codespace.billtracker.persistence.dao.BillDao
import ro.codespace.billtracker.persistence.entity.Bill

@Database(entities = [Bill::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract fun billDao(): BillDao
}