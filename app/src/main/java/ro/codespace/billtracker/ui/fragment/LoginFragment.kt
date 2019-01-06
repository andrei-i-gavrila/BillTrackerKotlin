package ro.codespace.billtracker.ui.fragment

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_login.*
import ro.codespace.billtracker.R
import ro.codespace.billtracker.ui.viewmodel.BillsViewModel
import ro.codespace.billtracker.utils.replace

class LoginFragment : Fragment() {
    private val compositeDisposable = CompositeDisposable()

    private val viewModel: BillsViewModel by lazy { ViewModelProviders.of(activity!!)[BillsViewModel::class.java] }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = getString(R.string.not_logged_in_title)


        RxView.clicks(log_in_button)
            .subscribe(startLoginActivity)
            .addTo(compositeDisposable)

        FirebaseAuth.getInstance().addAuthStateListener {
            it.currentUser?.also { user ->
                Toast.makeText(activity!!.applicationContext, "Welcome ${user.displayName!!}!", Toast.LENGTH_LONG).show()
                activity!!.supportFragmentManager.replace(R.id.content_frame, BillsFragment())
            }
        }
    }

    private val startLoginActivity = { _: Any ->
        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(listOf(AuthUI.IdpConfig.GoogleBuilder().build()))
                .build(), RC_SIGN_IN
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    companion object {
        const val RC_SIGN_IN = 11111
    }
}
