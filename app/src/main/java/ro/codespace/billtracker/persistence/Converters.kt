package ro.codespace.billtracker.persistence

import android.arch.persistence.room.TypeConverter
import ro.codespace.billtracker.persistence.entity.enumeration.Currency

class Converters {

    @TypeConverter
    fun fromCurrency(currency: Currency) = currency.name

    @TypeConverter
    fun toCurrency(currencyName: String) = Currency.valueOf(currencyName)

}