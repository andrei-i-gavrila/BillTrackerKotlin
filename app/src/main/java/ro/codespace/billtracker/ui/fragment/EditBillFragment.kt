package ro.codespace.billtracker.ui.fragment

import android.arch.lifecycle.ViewModelProviders
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
import ro.codespace.billtracker.ui.viewmodel.BillsViewModel

class EditBillFragment : BaseBillFragment() {

    private val viewModel: BillsViewModel by lazy { ViewModelProviders.of(activity!!)[BillsViewModel::class.java] }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.edit_bill_fragment_title)
        bill_currency_input.adapter = ArrayAdapter<Currency>(activity!!.applicationContext, R.layout.support_simple_spinner_dropdown_item, Currency.values())


        viewModel.selectedBill?.let { bill ->
            bill_name_input.setText(bill.name)
            bill_payment_day_input.setText(bill.paymentDay.toString())
            bill_price_input.setText(bill.price.toString())
            bill_currency_input.setSelection(bill.currency.ordinal)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.appbar_menu_edit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        R.id.action_save -> {
            if (validateBillInput()) {
                viewModel.selectedBill?.apply {
                    name = bill_name_input.text.toString()
                    price = bill_price_input.text.toString().toFloat()
                    currency = Currency.valueOf(bill_currency_input.selectedItem.toString())
                    paymentDay = bill_payment_day_input.text.toString().toInt()
                }
                BillRepository.update(viewModel.selectedBill!!)
                activity?.onBackPressed()
            }
            true
        }
        R.id.action_delete -> {
            BillRepository.delete(viewModel.selectedBill!!)
            activity?.onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)

    }
}