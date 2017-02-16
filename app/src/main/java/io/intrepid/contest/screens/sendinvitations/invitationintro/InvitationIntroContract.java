package io.intrepid.contest.screens.sendinvitations.invitationintro;

import io.intrepid.contest.base.BaseContract;

class InvitationIntroContract {
    interface View extends BaseContract.View {
        void showSelectContactsMessage();

        void showPermissionDeniedMessage();
    }

    interface Presenter extends BaseContract.Presenter<View> {
    }
}
