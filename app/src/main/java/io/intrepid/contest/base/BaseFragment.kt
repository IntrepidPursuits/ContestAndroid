package io.intrepid.contest.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import butterknife.ButterKnife
import butterknife.Unbinder
import io.intrepid.contest.ContestApplication
import timber.log.Timber

abstract class BaseFragment<P : BaseContract.Presenter<V>, in V : BaseContract.View> : Fragment(), BaseContract.View {

    protected lateinit var presenter: P
    private var unbinder: Unbinder? = null
    private var actionBar: ActionBar? = null

    protected val contestApplication: ContestApplication
        get() = activity.application as ContestApplication

    @CallSuper
    override fun onAttach(context: Context?) {
        Timber.v("Lifecycle onAttach: " + this + " to " + context)
        super.onAttach(context)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.v("Lifecycle onCreate: " + this)
        super.onCreate(savedInstanceState)
        val configuration = contestApplication.presenterConfiguration
        presenter = createPresenter(configuration)
        actionBar = (activity as AppCompatActivity).supportActionBar
    }

    /**
     * Override [.onViewCreated] to handle any logic that needs to occur right after inflating the view.
     * onViewCreated is called immediately after onCreateView
     */
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Timber.v("Lifecycle onCreateView: " + this)
        val view = inflater.inflate(layoutResourceId, container, false)
        unbinder = ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated(savedInstanceState)
        presenter.onViewCreated()
    }

    /**
     * Override this method to do any additional view initialization (ex: setup RecyclerView adapter)
     */
    protected open fun onViewCreated(savedInstanceState: Bundle?) {
    }

    protected abstract val layoutResourceId: Int

    abstract fun createPresenter(configuration: PresenterConfiguration): P

    @CallSuper
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Timber.v("Lifecycle onActivityResult: " + this)
        super.onActivityResult(requestCode, resultCode, data)
        presenter.bindView(this as V)
    }

    @CallSuper
    override fun onStart() {
        Timber.v("Lifecycle onStart: " + this)
        super.onStart()
        presenter.bindView(this as V)
    }

    @CallSuper
    override fun onResume() {
        Timber.v("Lifecycle onResume: " + this)
        super.onResume()
    }

    @CallSuper
    override fun onPause() {
        Timber.v("Lifecycle onPause: " + this)
        super.onPause()
    }

    @CallSuper
    override fun onStop() {
        Timber.v("Lifecycle onStop: " + this)
        super.onStop()
        presenter.unbindView()
    }

    @CallSuper
    override fun onDestroyView() {
        Timber.v("Lifecycle onDestroyView: " + this)
        super.onDestroyView()
        unbinder!!.unbind()
    }

    @CallSuper
    override fun onDestroy() {
        Timber.v("Lifecycle onDestroy: " + this)
        super.onDestroy()
        presenter.onViewDestroyed()
    }

    override fun onDetach() {
        Timber.v("Lifecycle onDetach: " + this + " from " + context)
        super.onDetach()
    }

    protected fun setActionBarTitle(title: String) {
            actionBar?.title = title
    }

    protected fun setActionBarTitle(@StringRes titleResource: Int) {
        setActionBarTitle(resources.getString(titleResource))
    }

    protected fun setActionBarDisplayHomeAsUpEnabled(enabled: Boolean) {
            actionBar?.setDisplayHomeAsUpEnabled(enabled)
    }

    override fun showMessage(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun showMessage(@StringRes messageResource: Int) {
        showMessage(resources.getString(messageResource))
    }
}
