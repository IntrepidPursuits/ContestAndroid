package io.intrepid.contest.screens.sendinvitations;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Contact;
import io.intrepid.contest.models.ParticipationType;
import io.intrepid.contest.screens.conteststatus.ContestStatusActivity;
import io.intrepid.contest.screens.sendinvitations.invitationintro.InvitationIntroFragment;
import io.intrepid.contest.screens.sendinvitations.selectcontacts.SelectContactsFragment;
import io.intrepid.contest.screens.splash.SplashActivity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SendInvitationsActivity extends BaseMvpActivity<SendInvitationsContract.Presenter>
        implements SendInvitationsContract.View, SendInvitationsActivityContract {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @BindView(R.id.select_contacts_fab_button)
    ImageView selectContactsButton;

    private Menu menu;

    public static Intent makeIntent(Context context) {
        return new Intent(context, SendInvitationsActivity.class);
    }

    @NonNull
    @Override
    public SendInvitationsContract.Presenter createPresenter(PresenterConfiguration configuration) {
        return new SendInvitationsPresenter(this, configuration);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_send_invitations;
    }

    @Override
    public ParticipationType getParticipationType() {
        return presenter.getInvitationParticipantType();
    }

    @Override
    public void onBackPressed() {
        presenter.onBackButtonClicked();
    }

    @Override
    public void setActionBarDisplayHomeAsUpEnabled(boolean enabled) {
        super.setActionBarDisplayHomeAsUpEnabled(enabled);
    }

    @Override
    public void setActionBarTitle(@StringRes int titleResource) {
        super.setActionBarTitle(titleResource);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_send_invitations, menu);
        this.menu = menu;
        presenter.onCreateOptionsMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        presenter.onOptionsItemSelected(item.getItemId());
        return true;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean checkContactsPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        return checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void requestContactsPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] { Manifest.permission.READ_CONTACTS },
                               PERMISSIONS_REQUEST_READ_CONTACTS);
        }
    }

    @Override
    public void showSendInvitationsMenuItem(boolean visible) {
        menu.findItem(R.id.send_invitations_menu_action).setVisible(visible);
    }

    @Override
    public void showSendInvitationsSkipMenuItem(boolean visible) {
        menu.findItem(R.id.send_invitations_skip_menu_action).setVisible(visible);
    }

    @Override
    public void showSelectContactsButton(boolean show) {
        selectContactsButton.setVisibility(show ? VISIBLE : GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            presenter.onContactsPermissionsResult();
        }
    }

    @OnClick(R.id.select_contacts_fab_button)
    public void onSelectContactsButtonClicked() {
        presenter.onSelectContactsButtonClicked();
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitAllowingStateLoss();
    }

    @Override
    public void showInvitationIntroFragment() {
        InvitationIntroFragment fragment = (InvitationIntroFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_invitation_intro);

        if (fragment == null) {
            fragment = new InvitationIntroFragment();
        }

        replaceFragment(fragment);
    }

    @Override
    public void showSelectContactsFragment() {
        SelectContactsFragment fragment = (SelectContactsFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_select_contacts);

        if (fragment == null) {
            fragment = new SelectContactsFragment();
        }

        replaceFragment(fragment);
    }

    @Override
    public void showContestStatusScreen() {
        startActivity(ContestStatusActivity.makeIntent(this));
    }

    @Override
    public void cancelSelection() {
        startActivity(SplashActivity.makeIntent(this));
        finish();
    }

    @Override
    public boolean hasContactPermissions() {
        return presenter.hasContactPermissions();
    }

    @Override
    public boolean isContactSelectionEnabled() {
        return presenter.isContactSelectionEnabled();
    }

    @Override
    public List<Contact> getContactList() {
        return presenter.getContactList();
    }

    @Override
    public void onAddContestantsButtonClicked(List<Contact> selectedContactList) {
        presenter.onAddContestantsButtonClicked(selectedContactList);
    }
}
