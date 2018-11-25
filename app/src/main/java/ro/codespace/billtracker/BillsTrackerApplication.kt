package ro.codespace.billtracker

import android.app.Application
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import ro.codespace.billtracker.persistence.Database

class BillsTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        database = Room
            .databaseBuilder(applicationContext, Database::class.java, "BillTracker.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    companion object {

        lateinit var database: Database
    }
}