package ro.codespace.billtracker.persistence.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ro.codespace.billtracker.persistence.entity.Bill
import ro.codespace.billtracker.persistence.entity.Currency

object BillRepository {

    fun getBills(): Flowable<List<Bill>> {
        val bills = PublishSubject.create<List<Bill>>()
        FirebaseFirestore.getInstance()
            .collection("bills")
            .whereEqualTo("userId", FirebaseAuth.getInstance().currentUser?.uid)
            .addSnapshotListener { snapshot, _ ->
                snapshot?.map {
                    it.toObject(Bill::class.java).apply {
                        id = it.id
                    }
                }?.let {
                    bills.onNext(it)
                }
            }
        return bills.toFlowable(BackpressureStrategy.LATEST)
    }

    fun getBill(billId: String): Observable<Bill> {
        val result = PublishSubject.create<Bill>()

        FirebaseFirestore.getInstance().collection("bills").document(billId)
            .addSnapshotListener { snapshot, _ ->
                snapshot?.toObject(Bill::class.java)?.let {
                    it.id = snapshot.id
                    result.onNext(it)
                }
            }

        return result.subscribeOn(Schedulers.io())
    }

    fun create(name: String, price: Float, currency: Currency, paymentDay: Int) {
        FirebaseFirestore.getInstance().collection("bills")
            .add(Bill(name, price, currency, paymentDay, FirebaseAuth.getInstance().uid!!))
    }

    fun update(bill: Bill) {
        FirebaseFirestore.getInstance().collection("bills").document(bill.id!!).set(bill)
    }

    fun delete(bill: Bill) {
        FirebaseFirestore.getInstance().collection("bills").document(bill.id!!).delete()
    }

}