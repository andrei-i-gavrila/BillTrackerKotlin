package ro.codespace.billtracker.utils

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

fun FragmentManager.replace(@IdRes id: Int, fragment: Fragment, addToBackStack: Boolean = false) {
    this.beginTransaction().replace(id, fragment).also { if (addToBackStack) it.addToBackStack(null) }.commit()
}