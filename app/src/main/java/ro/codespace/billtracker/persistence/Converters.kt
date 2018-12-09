package ro.codespace.billtracker.persistence

import android.arch.persistence.room.TypeConverter
import ro.codespace.billtracker.persistence.entity.Bill
import ro.codespace.billtracker.persistence.entity.enumeration.Currency
import ro.codespace.billtracker.swagger.model.BillDTO

class Converters {

    @TypeConverter
    fun fromCurrency(currency: Currency) = currency.name

    @TypeConverter
    fun toCurrency(currencyName: String) = Currency.valueOf(currencyName)

}

fun BillDTO.toEntity(): Bill {
    return Bill(name, price, Currency.valueOf(currency.name), dayOfMonth).also {
        it.id = id
    }
}


fun Bill.toDTO(): BillDTO {
    return BillDTO().also {
        it.id = id
        it.name = name
        it.dayOfMonth = paymentDay
        it.currency = BillDTO.CurrencyEnum.valueOf(currency.name)
        it.price = price
        it.fixedPrice = 1
    }
}
