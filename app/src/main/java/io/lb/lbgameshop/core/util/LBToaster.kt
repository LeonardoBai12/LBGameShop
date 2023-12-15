package io.lb.lbgameshop.core.util

import android.content.Context

class LBToaster(private val context: Context) : Toaster {
    override fun showToast(message: String) {
        context.showToast(message)
    }

    override fun showToast(resId: Int) {
        context.showToast(resId)
    }
}
