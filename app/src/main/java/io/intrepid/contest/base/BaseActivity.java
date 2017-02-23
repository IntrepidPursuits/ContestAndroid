package io.intrepid.contest.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.ButterKnife;
import io.intrepid.contest.BuildConfig;
import io.intrepid.contest.ContestApplication;
import io.intrepid.contest.screens.splash.SplashActivity;
import io.intrepid.contest.utils.ShakeDetector;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

abstract class BaseActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private Flowable<?> shakeFlowable;
    private Disposable shakeSubscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Timber.v("Lifecycle onCreate: " + this);
        super.onCreate(savedInstanceState);

        setContentView(getLayoutResourceId());
        ButterKnife.bind(this);

        actionBar = getSupportActionBar();
        shakeFlowable = ShakeDetector.create(this);
    }

    @Override
    @CallSuper
    protected void onNewIntent(Intent intent) {
        Timber.v("Lifecycle onNewIntent: " + this);
        super.onNewIntent(intent);
    }

    @Override
    @CallSuper
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Timber.v("Lifecycle onActivityResult: " + this);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    @CallSuper
    protected void onStart() {
        Timber.v("Lifecycle onStart: " + this);
        super.onStart();
        shakeSubscription = shakeFlowable.subscribe((Consumer<Object>) o -> {
            ((ContestApplication) getApplication()).resetState();
            Intent intent = new Intent(this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            Runtime.getRuntime().exit(0);
        });
    }

    @Override
    @CallSuper
    protected void onResume() {
        Timber.v("Lifecycle onResume: " + this);
        super.onResume();
    }

    @Override
    @CallSuper
    protected void onPause() {
        Timber.v("Lifecycle onPause: " + this);
        super.onPause();
    }

    @Override
    @CallSuper
    protected void onStop() {
        Timber.v("Lifecycle onStop: " + this);
        super.onStop();
        shakeSubscription.dispose();
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        Timber.v("Lifecycle onDestroy: " + this);
        super.onDestroy();
    }

    @Override
    @CallSuper
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    protected abstract int getLayoutResourceId();

    protected final ContestApplication getContestApplication() {
        return (ContestApplication) getApplication();
    }

    protected void setActionBarTitle(String title) {
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    protected void setActionBarTitle(@StringRes int titleResource) {
        setActionBarTitle(getResources().getString(titleResource));
    }

    protected void setActionBarDisplayHomeAsUpEnabled(boolean enabled) {
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(enabled);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showMessage(String message) {
        if (BuildConfig.DEV_BUILD) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    public void showMessage(@StringRes int messageResource) {
        showMessage(getResources().getString(messageResource));
    }
}
