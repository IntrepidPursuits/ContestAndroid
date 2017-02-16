package io.intrepid.contest.screens.sendinvitations.invitationintro;

import android.support.annotation.NonNull;

import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;

public class InvitationIntroPresenter extends BasePresenter<InvitationIntroContract.View>
        implements InvitationIntroContract.Presenter {

    private boolean hasContactPermissions;

    public InvitationIntroPresenter(@NonNull InvitationIntroContract.View view,
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
            view.showPermissionDeniedMessage();
        }
    }
}
