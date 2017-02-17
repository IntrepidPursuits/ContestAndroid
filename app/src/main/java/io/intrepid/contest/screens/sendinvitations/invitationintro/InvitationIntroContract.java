package io.intrepid.contest.screens.sendinvitations.invitationintro;

import android.support.annotation.StringRes;

import io.intrepid.contest.base.BaseContract;

class InvitationIntroContract {
    interface View extends BaseContract.View {
        void showSelectContactsMessage();

        void showPermissionDeniedMessage(@StringRes int wrappingStringResource, @StringRes int clickableResource);
    }

    interface Presenter extends BaseContract.Presenter<View> {
    }
}
