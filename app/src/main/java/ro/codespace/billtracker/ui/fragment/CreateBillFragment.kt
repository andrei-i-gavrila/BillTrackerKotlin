package ro.codespace.billtracker.ui.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_add_bill.*
import ro.codespace.billtracker.R
import ro.codespace.billtracker.persistence.entity.Currency
import ro.codespace.billtracker.persistence.repository.BillRepository

class CreateBillFragment : BaseBillFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.add_bill_title)
        bill_currency_input.adapter = ArrayAdapter<Currency>(activity!!.applicationContext, R.layout.support_simple_spinner_dropdown_item, Currency.values())
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.appbar_menu_create, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        R.id.action_save -> {
            if (validateBillInput()) {
                BillRepository.create(
                    bill_name_input.text.toString(),
                    bill_price_input.text.toString().toFloat(),
                    Currency.valueOf(bill_currency_input.selectedItem.toString()),
                    bill_payment_day_input.text.toString().toInt()
                )
                activity?.onBackPressed()
            }
            true
        }
        R.id.action_close -> {
            activity?.onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}