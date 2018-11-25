package ro.codespace.billtracker.persistence.dao

import android.arch.persistence.room.*
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import ro.codespace.billtracker.persistence.entity.Bill

@Dao
interface BillDao {

    @Query("SELECT * from Bill")
    fun getBills(): Flowable<List<Bill>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(bill: Bill)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(bill: Bill)

    @Delete()
    fun delete(bill: Bill)

    @Query("select * from Bill where id = :id")
    fun getById(id: Int): Maybe<Bill>

}