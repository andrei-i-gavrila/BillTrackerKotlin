package ro.codespace.billtracker.ui.viewmodel

import android.arch.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import io.reactivex.subjects.PublishSubject
import ro.codespace.billtracker.persistence.entity.Bill

class BillsViewModel : ViewModel() {
    var selectedBill: Bill? = null
}
