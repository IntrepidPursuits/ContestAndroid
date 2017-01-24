package io.intrepid.contest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.intrepid.contest.base.BaseFragment;
import timber.log.Timber;

public abstract class BaseSlideFragment extends Fragment {

    public abstract boolean canMoveFurther();
    public abstract int cantMoveFurtherErrorMessage();

    @Override
    public final View onCreateView(LayoutInflater inflater,
                                   @Nullable ViewGroup container,
                                   @Nullable Bundle savedInstanceState) {
        Timber.v("Lifecycle onCreateView: " + this);
        View view = inflater.inflate(getLayoutResourceId(), container, false);
        Unbinder unbinder = ButterKnife.bind(this, view);
        return view;
    }

    protected abstract int getLayoutResourceId();
}
