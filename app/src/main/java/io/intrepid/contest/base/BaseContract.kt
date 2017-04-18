package io.intrepid.contest.base

import android.support.annotation.StringRes

class BaseContract {

    interface View {
        fun showMessage(message: String)

        fun showMessage(@StringRes messageResource: Int)
    }

    interface Presenter<in T : View> {

        fun bindView(view: T)

        fun unbindView()

        fun onViewCreated()

        fun onViewDestroyed()
    }
}
