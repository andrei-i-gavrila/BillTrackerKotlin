package ro.codespace.billtracker.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_add_bill.*
import ro.codespace.billtracker.R
import ro.codespace.billtracker.persistence.entity.enumeration.Currency
import ro.codespace.billtracker.persistence.repository.BillRepository

class EditBillFragment : BaseBillFragment() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.edit_bill_fragment_title)
        bill_currency_input.adapter = ArrayAdapter<Currency>(activity!!.applicationContext, R.layout.support_simple_spinner_dropdown_item, Currency.values())
        delete_bill_button.visibility = View.VISIBLE

        BillRepository.getBill(arguments!!.getInt("billId")).observeOn(AndroidSchedulers.mainThread()).subscribe { bill ->
            bill_name_input.setText(bill.name)
            bill_payment_day_input.setText(bill.paymentDay.toString())
            bill_price_input.setText(bill.price.toString())
            bill_currency_input.setSelection(bill.currency.ordinal)

            RxView.clicks(save_bill_button).subscribe {
                if (validateBillInput()) {
                    bill.name = bill_name_input.text.toString()
                    bill.price = bill_price_input.text.toString().toFloat()
                    bill.currency = Currency.valueOf(bill_currency_input.selectedItem.toString())
                    bill.paymentDay = bill_payment_day_input.text.toString().toInt()
                    BillRepository.update(bill)
                }
                fragmentManager!!.popBackStack()
            }.addTo(disposables)

            RxView.clicks(delete_bill_button).subscribe {
                BillRepository.delete(bill)
                fragmentManager!!.popBackStack()
            }
        }.addTo(disposables)



    }

}