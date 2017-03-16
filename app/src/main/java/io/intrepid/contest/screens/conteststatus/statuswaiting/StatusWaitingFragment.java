package io.intrepid.contest.screens.conteststatus.statuswaiting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import butterknife.BindView;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseFragment;
import io.intrepid.contest.base.PresenterConfiguration;

public class StatusWaitingFragment extends BaseFragment<StatusWaitingContract.Presenter>
        implements StatusWaitingContract.View {

    @NonNull
    @Override
    public StatusWaitingPresenter createPresenter(PresenterConfiguration configuration) {
        return new StatusWaitingPresenter(this, configuration);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_status_waiting;
    }

    @Override
    protected void onViewCreated(@Nullable Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);

        setActionBarTitle(R.string.contest_status_bar_title);
        setActionBarDisplayHomeAsUpEnabled(true);
    }
}
