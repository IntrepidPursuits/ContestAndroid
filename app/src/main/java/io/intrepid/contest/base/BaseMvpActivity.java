package io.intrepid.contest.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.intrepid.contest.ContestApplication;
import io.intrepid.contest.screens.splash.SplashActivity;
import io.intrepid.contest.utils.ShakeDetector;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * Base class for activities that will have some business logic instead of just hosting a fragment.
 * If the activity is only going to act as a container for a fragment, use {@link BaseFragmentActivity}
 * instead
 */
public abstract class BaseMvpActivity<T extends BaseContract.Presenter> extends BaseActivity implements BaseContract.View {

    protected T presenter;
    private Flowable<?> shakeFlowable;
    private Disposable shakeSubscription;

    @NonNull
    public abstract T createPresenter(PresenterConfiguration configuration);

    protected abstract int getLayoutResourceId();

    @Override
    /**
     * Override {@link #onViewCreated(Bundle)} to handle any logic that needs to occur right after inflating the view.
     * onViewCreated is called immediately after onCreateView
     */
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PresenterConfiguration configuration = getContestApplication().getPresenterConfiguration();
        presenter = createPresenter(configuration);
        onViewCreated(savedInstanceState);
        presenter.onViewCreated();
        shakeFlowable = ShakeDetector.create(this);
    }

    /**
     * Override this method to do any additional view initialization (ex: setup RecycleView adapter)
     */
    protected void onViewCreated(Bundle savedInstanceState) {

    }

    @Override
    @CallSuper
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        presenter.bindView(this);
    }

    @Override
    @CallSuper
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.bindView(this);
    }

    @Override
    @CallSuper
    public void onStart() {
        super.onStart();
        presenter.bindView(this);
        shakeSubscription = shakeFlowable.subscribe((Consumer<Object>) o -> {
            Timber.e("WOOT WOOT");
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
    public void onStop() {
        super.onStop();
        presenter.unbindView();
        shakeSubscription.dispose();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onViewDestroyed();
    }
}
