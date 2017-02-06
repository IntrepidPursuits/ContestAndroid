package io.intrepid.contest.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.intrepid.contest.ContestApplication;
import timber.log.Timber;

public abstract class BaseFragment<T extends BaseContract.Presenter> extends Fragment implements BaseContract.View {

    protected T presenter;
    private Unbinder unbinder;
    private ActionBar actionBar;

    @Override
    @CallSuper
    public void onAttach(Context context) {
        Timber.v("Lifecycle onAttach: " + this + " to " + context);
        super.onAttach(context);
    }

    @Override
    @CallSuper
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Timber.v("Lifecycle onCreate: " + this);
        super.onCreate(savedInstanceState);
        PresenterConfiguration configuration = getContestApplication().getPresenterConfiguration();
        presenter = createPresenter(configuration);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    /**
     * Override {@link #onViewCreated(Bundle)} to handle any logic that needs to occur right after inflating the view.
     * onViewCreated is called immediately after onCreateView
     */
    @Override
    public final View onCreateView(LayoutInflater inflater,
                                   @Nullable ViewGroup container,
                                   @Nullable Bundle savedInstanceState) {
        Timber.v("Lifecycle onCreateView: " + this);
        View view = inflater.inflate(getLayoutResourceId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public final void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onViewCreated(savedInstanceState);
        presenter.onViewCreated();
    }

    /**
     * Override this method to do any additional view initialization (ex: setup RecyclerView adapter)
     */
    protected void onViewCreated(@Nullable Bundle savedInstanceState) {
    }


    protected abstract int getLayoutResourceId();

    @NonNull
    public abstract T createPresenter(PresenterConfiguration configuration);

    @Override
    @CallSuper
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Timber.v("Lifecycle onActivityResult: " + this);
        super.onActivityResult(requestCode, resultCode, data);
        presenter.bindView(this);
    }

    @Override
    @CallSuper
    public void onStart() {
        Timber.v("Lifecycle onStart: " + this);
        super.onStart();
        presenter.bindView(this);
    }

    @Override
    @CallSuper
    public void onResume() {
        Timber.v("Lifecycle onResume: " + this);
        super.onResume();
    }

    @Override
    @CallSuper
    public void onPause() {
        Timber.v("Lifecycle onPause: " + this);
        super.onPause();
    }

    @Override
    @CallSuper
    public void onStop() {
        Timber.v("Lifecycle onStop: " + this);
        super.onStop();
        presenter.unbindView();
    }

    @Override
    @CallSuper
    public void onDestroyView() {
        Timber.v("Lifecycle onDestroyView: " + this);
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    @CallSuper
    public void onDestroy() {
        Timber.v("Lifecycle onDestroy: " + this);
        super.onDestroy();
        presenter.onViewDestroyed();
    }

    @Override
    public void onDetach() {
        Timber.v("Lifecycle onDetach: " + this + " from " + getContext());
        super.onDetach();
    }

    protected final ContestApplication getContestApplication() {
        return (ContestApplication) getActivity().getApplication();
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

    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public void showMessage(@StringRes int messageResource) {
        showMessage(getResources().getString(messageResource));
    }
}
