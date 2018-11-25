package ro.codespace.billtracker.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_add_bill.*
import ro.codespace.billtracker.R
import ro.codespace.billtracker.persistence.entity.enumeration.Currency
import ro.codespace.billtracker.persistence.repository.BillRepository

class CreateBillFragment : BaseBillFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.add_bill_title)
        bill_currency_input.adapter = ArrayAdapter<Currency>(activity!!.applicationContext, R.layout.support_simple_spinner_dropdown_item, Currency.values())


        RxView.clicks(save_bill_button).subscribe {
            if (validateBillInput()) {
                BillRepository.create(
                    bill_name_input.text.toString(),
                    bill_price_input.text.toString().toFloat(),
                    Currency.valueOf(bill_currency_input.selectedItem.toString()),
                    bill_payment_day_input.text.toString().toInt()
                )
                fragmentManager!!.popBackStack()
            }
        }.addTo(disposables)
    }

}