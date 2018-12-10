package ro.codespace.billtracker.persistence.repository

import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import ro.codespace.billtracker.BillsTrackerApplication
import ro.codespace.billtracker.persistence.entity.Bill
import ro.codespace.billtracker.persistence.entity.enumeration.Currency

object BillRepository {

    private val billDao = BillsTrackerApplication.database.billDao()

    fun getBills(): Flowable<List<Bill>> {
        return billDao.getBills()
    }

    fun getBill(id: Int) = billDao.getById(id).subscribeOn(Schedulers.io())!!

    fun create(name: String, price: Float, currency: Currency, paymentDay: Int) {
        doAsync {
            billDao.save(Bill(name, price, currency, paymentDay))
        }
    }

    fun update(bill: Bill, sync: Boolean = true) {
        doAsync {
            billDao.update(bill.apply {
                synced = !sync
            })
        }
    }

    fun delete(bill: Bill) {
        update(bill.apply { deleted = true }, false)
    }

    fun insertOrUpdate(bill: Bill) {
        doAsync {
            billDao.save(bill)
        }
    }
}