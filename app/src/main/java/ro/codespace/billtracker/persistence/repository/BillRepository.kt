package ro.codespace.billtracker.persistence.repository

import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import ro.codespace.billtracker.BillsTrackerApplication
import ro.codespace.billtracker.persistence.entity.Bill
import ro.codespace.billtracker.persistence.entity.enumeration.Currency
import ro.codespace.billtracker.persistence.toDTO
import ro.codespace.billtracker.persistence.toEntity
import ro.codespace.billtracker.swagger.api.BillsApi

object BillRepository {

    private val billDao = BillsTrackerApplication.database.billDao()
    private val billApi = BillsApi()

    fun getBills(): Flowable<List<Bill>> {

        doAsync {
            billApi.bills.forEach {
                billDao.save(it.toEntity())
            }
        }

        return billDao.getBills()
    }

    fun getBill(id: Int) = billDao.getById(id).subscribeOn(Schedulers.io())!!

    fun create(name: String, price: Float, currency: Currency, paymentDay: Int) {
        doAsync {
            val bill = Bill(name, price, currency, paymentDay)
            bill.id = billDao.save(bill).toInt()
            billApi.saveBill(bill.toDTO())
        }
    }

    fun update(bill: Bill) {
        doAsync {
            billDao.update(bill)
                billApi.updateBill(bill.id, bill.toDTO())
        }
    }

    fun delete(bill: Bill) {
        doAsync {
            billApi.deleteBill(bill.id)
            billDao.delete(bill)
        }
    }
}