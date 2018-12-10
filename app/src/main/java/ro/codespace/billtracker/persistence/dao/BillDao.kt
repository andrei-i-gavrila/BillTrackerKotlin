package ro.codespace.billtracker.persistence.dao

import android.arch.persistence.room.*
import io.reactivex.Flowable
import io.reactivex.Maybe
import ro.codespace.billtracker.persistence.entity.Bill

@Dao
interface BillDao {

    @Query("SELECT * from Bill where deleted = 0")
    fun getBills(): Flowable<List<Bill>>

    @Query("SELECT * from Bill where deleted = 1")
    fun getBillsToDelete(): List<Bill>

    @Query("SELECT * from Bill where synced = 0")
    fun getBillsToSync(): List<Bill>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(bill: Bill): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(bill: Bill)

    @Delete()
    fun delete(bill: Bill)

    @Query("select * from Bill where id = :id")
    fun getById(id: Int): Maybe<Bill>

}