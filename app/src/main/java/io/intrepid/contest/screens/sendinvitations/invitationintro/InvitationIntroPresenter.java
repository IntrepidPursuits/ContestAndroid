package io.intrepid.contest.screens.sendinvitations.invitationintro;

import android.support.annotation.NonNull;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;

class InvitationIntroPresenter extends BasePresenter<InvitationIntroContract.View>
        implements InvitationIntroContract.Presenter {

    private final boolean hasContactPermissions;

    InvitationIntroPresenter(@NonNull InvitationIntroContract.View view,
                             @NonNull PresenterConfiguration configuration,
                             boolean hasContactPermissions) {
        super(view, configuration);

        this.hasContactPermissions = hasContactPermissions;
    }

    @Override
    protected void onViewBound() {
        super.onViewBound();

        if (hasContactPermissions) {
            view.showSelectContactsMessage();
        } else {
            view.showPermissionDeniedMessage(R.string.no_contacts_permissions, R.string.no_contacts_permissions_link);
        }
    }
}
