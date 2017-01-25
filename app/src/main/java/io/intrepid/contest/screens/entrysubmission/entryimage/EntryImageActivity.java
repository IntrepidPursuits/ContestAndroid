package io.intrepid.contest.screens.entrysubmission.entryimage;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;

import static io.intrepid.contest.screens.entrysubmission.entryimage.EntryImageContract.Presenter;
import static io.intrepid.contest.screens.entrysubmission.entryimage.EntryImageContract.View;

public class EntryImageActivity extends BaseMvpActivity<Presenter> implements View {

    private static final String EXTRA_ENTRY_NAME = "_extra_entry_name_";

    public static Intent makeIntent(Context context, String entryName) {
        return new Intent(context, EntryImageActivity.class).putExtra(EXTRA_ENTRY_NAME, entryName);
    }

    @NonNull
    @Override
    public Presenter createPresenter(PresenterConfiguration configuration) {
        return new EntryImagePresenter(this, configuration);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_entry_image;
    }
}
