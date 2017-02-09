package io.intrepid.contest.screens.sendinvitations.selectcontacts;

import io.intrepid.contest.base.BaseContract;

class SelectContactsContract {
    interface View extends BaseContract.View {
        boolean hasContactsPermissions();

        void displayContactList();

        void goBackToPreviousScreen();
    }

    interface Presenter extends BaseContract.Presenter<View> {

    }
}
