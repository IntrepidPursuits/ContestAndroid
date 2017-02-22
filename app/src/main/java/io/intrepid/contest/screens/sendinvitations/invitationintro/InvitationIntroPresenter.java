package io.intrepid.contest.screens.sendinvitations.invitationintro;

import android.support.annotation.NonNull;

import io.intrepid.contest.R;
import io.intrepid.contest.base.BasePresenter;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.ParticipationType;

class InvitationIntroPresenter extends BasePresenter<InvitationIntroContract.View>
        implements InvitationIntroContract.Presenter {

    private final ParticipationType participationType;
    private final boolean hasContactPermissions;

    public InvitationIntroPresenter(@NonNull InvitationIntroContract.View view,
                                    @NonNull PresenterConfiguration configuration,
                                    boolean hasContactPermissions,
                                    ParticipationType participationType) {
        super(view, configuration);

        this.hasContactPermissions = hasContactPermissions;
        this.participationType = participationType;
    }

    @Override
    protected void onViewBound() {
        super.onViewBound();

        if (hasContactPermissions) {
            view.showSelectContactsMessage(participationType.equals(ParticipationType.CONTESTANT) ?
                                                   R.string.invite_contestants_intro :
                                                   R.string.invite_judges_intro);
        } else {
            view.showPermissionDeniedMessage(R.string.no_contacts_permissions, R.string.no_contacts_permissions_link);
        }
    }
}
