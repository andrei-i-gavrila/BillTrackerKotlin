package ro.codespace.billtracker.persistence.entity

import android.arch.persistence.room.Entity

@Entity
data class Bill(
    var name: String = "",
    var price: Float = 0f,
    var currency: Currency = Currency.EUR,
    var paymentDay: Int = 1,
    var userId: String = ""
) {
    var id: String? = null
}