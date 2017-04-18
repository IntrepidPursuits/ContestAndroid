package io.intrepid.contest.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.StringRes
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import butterknife.ButterKnife
import io.intrepid.contest.ContestApplication
import io.intrepid.contest.screens.splash.SplashActivity
import io.intrepid.contest.utils.ShakeDetector
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import timber.log.Timber
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

abstract class BaseActivity : AppCompatActivity() {

    private var actionBar: ActionBar? = null
    private var shakeFlowable: Flowable<*>? = null
    private var shakeSubscription: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.v("Lifecycle onCreate: " + this)
        super.onCreate(savedInstanceState)

        setContentView(layoutResourceId)
        ButterKnife.bind(this)

        actionBar = supportActionBar
        shakeFlowable = ShakeDetector.create(this)
    }

    @CallSuper
    override fun onNewIntent(intent: Intent) {
        Timber.v("Lifecycle onNewIntent: " + this)
        super.onNewIntent(intent)
    }

    @CallSuper
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        Timber.v("Lifecycle onActivityResult: " + this)
        super.onActivityResult(requestCode, resultCode, data)
    }

    @CallSuper
    override fun onStart() {
        Timber.v("Lifecycle onStart: " + this)
        super.onStart()
        shakeSubscription = shakeFlowable!!.subscribe({
            (application as ContestApplication).resetState()
            val intent = Intent(this, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
            Runtime.getRuntime().exit(0)
        })
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
        shakeSubscription!!.dispose()
    }

    @CallSuper
    override fun onDestroy() {
        Timber.v("Lifecycle onDestroy: " + this)
        super.onDestroy()
    }

    @CallSuper
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    protected abstract val layoutResourceId: Int

    protected val contestApplication: ContestApplication
        get() = application as ContestApplication

    protected fun setActionBarTitle(title: String) {
        actionBar?.title = title
    }

    protected open fun setActionBarTitle(@StringRes titleResource: Int) {
        setActionBarTitle(resources.getString(titleResource))
    }

    protected open fun setActionBarDisplayHomeAsUpEnabled(enabled: Boolean) {
        actionBar?.setDisplayHomeAsUpEnabled(enabled)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showMessage(@StringRes messageResource: Int) {
        showMessage(resources.getString(messageResource))
    }
}
