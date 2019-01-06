package ro.codespace.billtracker.ui.fragment

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_bills.*
import kotlinx.android.synthetic.main.item_list_bill.view.*
import ro.codespace.billtracker.R
import ro.codespace.billtracker.persistence.entity.Bill
import ro.codespace.billtracker.persistence.repository.BillRepository
import ro.codespace.billtracker.ui.ReactiveAdapter
import ro.codespace.billtracker.ui.viewmodel.BillsViewModel
import ro.codespace.billtracker.utils.replace


class BillsFragment : Fragment() {

    private val disposables = CompositeDisposable()

    private val viewModel: BillsViewModel by lazy { ViewModelProviders.of(activity!!)[BillsViewModel::class.java] }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bills, container, false)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.bills_title)

        initBillList()
        initCreateBillButton()
    }

    private fun initBillList() {
        bills_list.adapter = ReactiveAdapter(R.layout.item_list_bill, BillRepository.getBills()) { itemView, bill ->
            setupBillItemView(itemView, bill)
            setupBillClickListener(itemView, bill)
        }.apply { addTo(disposables) }

        bills_list.layoutManager = LinearLayoutManager(activity)
        bills_list.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
    }

    private fun setupBillItemView(itemView: View, bill: Bill) {
        itemView.bill_name.text = bill.name
        itemView.bill_price.text = getString(R.string.price, bill.price, bill.currency.name)
    }

    private fun setupBillClickListener(itemView: View, bill: Bill) {
        RxView.clicks(itemView).subscribe {
            viewModel.selectedBill = bill
            activity!!.supportFragmentManager.replace(R.id.content_frame, EditBillFragment(), addToBackStack = true)
        }.addTo(disposables)
    }

    private fun initCreateBillButton() {
        RxView.clicks(bills_fab).subscribe {
            activity!!.supportFragmentManager.replace(R.id.content_frame, CreateBillFragment(), addToBackStack = true)
        }.addTo(disposables)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

}