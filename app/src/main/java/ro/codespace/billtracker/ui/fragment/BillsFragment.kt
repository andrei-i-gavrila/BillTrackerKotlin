package ro.codespace.billtracker.ui.fragment

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Icon
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


class BillsFragment : Fragment() {

    private val disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = inflater.inflate(R.layout.fragment_bills, container, false)!!

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
            val editBillFragment = EditBillFragment()
            editBillFragment.arguments = Bundle().apply {
                putInt("billId", bill.id!!)
            }
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, editBillFragment)
                .addToBackStack(null).commit()
        }.addTo(disposables)
    }

    private fun initCreateBillButton() {
        RxView.clicks(bills_fab).subscribe {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CreateBillFragment())
                .addToBackStack(null).commit()
        }.addTo(disposables)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

}