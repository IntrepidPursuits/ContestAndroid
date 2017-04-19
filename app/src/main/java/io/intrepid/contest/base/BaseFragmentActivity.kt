package io.intrepid.contest.base

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment

import io.intrepid.contest.R

/**
 * Base class for activities whose sole purpose to to host a fragment. Child classes simply need
 * to override [.createFragment] and create the appropriate Fragment. If the activity
 * contains any additional logic, use [BaseMvpActivity] instead
 */
abstract class BaseFragmentActivity : BaseActivity() {
    override val layoutResourceId: Int
        get() = R.layout.activity_fragment_container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val intent = intent
            val fragment = createFragment(intent)
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()
        }
    }

    protected abstract fun createFragment(intent: Intent): Fragment
}