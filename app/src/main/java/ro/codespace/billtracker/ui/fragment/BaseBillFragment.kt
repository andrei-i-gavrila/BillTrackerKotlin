package ro.codespace.billtracker.ui.fragment

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_add_bill.*
import ro.codespace.billtracker.R
import ro.codespace.billtracker.ui.viewmodel.BillsViewModel

abstract class BaseBillFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_add_bill, container, false)
    }

    fun validateBillInput(): Boolean {
        val name = bill_name_input.text.toString()
        val nameValid = name != ""
        if (!nameValid) bill_name_input.error = getString(R.string.bill_name_exists)

        val priceValid = bill_price_input.text.toString().toFloatOrNull() !== null
        if (!priceValid) bill_price_input.error = getString(R.string.invalid_float)

        val paymentDay = bill_payment_day_input.text.toString().toIntOrNull()
        val paymentDayValid = paymentDay !== null && paymentDay >= 0 && paymentDay <= 30
        if (!paymentDayValid) bill_payment_day_input.error = getString(R.string.invalid_payment_day)

        return nameValid && priceValid && paymentDayValid
    }

}



