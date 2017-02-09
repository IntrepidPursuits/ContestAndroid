package io.intrepid.contest.screens.sendinvitations.selectcontacts;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;

public class SelectContactsPresenter extends BasePresenter<SelectContactsContract.View>
        implements SelectContactsContract.Presenter {

    public SelectContactsPresenter(@NonNull SelectContactsContract.View view,
                                   @NonNull PresenterConfiguration configuration) {
        super(view, configuration);
    }

    @Override
    protected void onViewBound() {
        super.onViewBound();

        if (view.hasContactsPermissions()) {
            view.displayContactList();
        } else {
            view.goBackToPreviousScreen();
        }
    }
}
