package ro.codespace.billtracker.synchronizer

import android.util.Log
import org.jetbrains.anko.doAsync
import ro.codespace.billtracker.BillsTrackerApplication
import ro.codespace.billtracker.persistence.repository.BillRepository
import ro.codespace.billtracker.persistence.toDTO
import ro.codespace.billtracker.persistence.toEntity
import ro.codespace.billtracker.swagger.api.BillsApi

object SyncService {

    private const val TAG = "SyncService"
    private val api = BillsApi()
    private val repository = BillRepository
    private val dao = BillsTrackerApplication.database.billDao()


    fun run() {
        doAsync {
            Log.i(TAG, "Syncing")
            sendDeletes()
            sendUpdates()
            receiveUpdates()
        }
    }

    private fun receiveUpdates() {
        Log.i(TAG, "receiving updates")
        api.bills.forEach { bill ->
            Log.i(TAG, "received bill" + bill.toString())
            repository.insertOrUpdate(bill.toEntity())
        }
    }

    private fun sendUpdates() {
        Log.i(TAG, "sending updates")

        dao.getBillsToSync().forEach { bill ->
            api.saveBill(bill.toDTO(), { _ ->
                Log.i(TAG, "updated bill" + bill.toString())
                repository.update(bill, false)
            }, { error ->
                Log.e(TAG, "Failed to send update", error)
            })
        }
    }

    private fun sendDeletes() {
        Log.i(TAG, "deleting from remote")

        dao.getBillsToDelete().forEach { bill ->
            api.deleteBill(bill.id, { _ ->
                Log.i(TAG, "deleted bill with id " + bill.id)

                repository.delete(bill)
            }, { error ->
                Log.e(TAG, "Failed to delete", error)
            })
        }
    }
}