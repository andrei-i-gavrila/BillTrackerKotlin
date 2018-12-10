package ro.codespace.billtracker.persistence.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import ro.codespace.billtracker.persistence.entity.enumeration.Currency

@Entity
data class Bill(
    var name: String,
    var price: Float,
    var currency: Currency,
    var paymentDay: Int
) {
    @PrimaryKey(autoGenerate = true) var id: Int? = null
    var synced = false
    var deleted = false
}