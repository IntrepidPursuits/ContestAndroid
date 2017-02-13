package io.intrepid.contest.screens.sendinvitations;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.screens.sendinvitations.selectcontacts.SelectContactsActivity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SendInvitationsActivity extends BaseMvpActivity<SendInvitationsContract.Presenter>
        implements SendInvitationsContract.View {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @BindView(R.id.select_contacts_fab_button)
    ImageView selectContactsButton;
    @BindView(R.id.send_invitations_icon)
    ImageView sendInvitationsIcon;
    @BindView(R.id.send_invitations_intro_text_view)
    TextView sendInvitationsIntroTextView;

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
    protected void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);

        setActionBarTitle(R.string.invite_contestants_bar_title);
        setActionBarDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_send_invitations;
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

    @Override
    public boolean hasContactsPermissions() {
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
        if (show) {
            selectContactsButton.setVisibility(VISIBLE);
        } else {
            selectContactsButton.setVisibility(GONE);
        }
    }

    @Override
    public void showSelectContactsMessage() {
        sendInvitationsIcon.setVisibility(VISIBLE);
        sendInvitationsIntroTextView.setText(getResources().getString(R.string.invite_contestants_intro));
    }

    @Override
    public void showPermissionDeniedMessage() {
        sendInvitationsIcon.setVisibility(GONE);
        sendInvitationsIntroTextView.setText(getResources().getString(R.string.no_contacts_permissions));

        // TODO: Enable click in part of the message that starts ACTION_PRIVACY_SETTINGS intent
        showMessage("Display permission denied message with intent to ACTION_PRIVACY_SETTINGS");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            boolean granted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);
            presenter.onContactsPermissionsResult(granted);
        }
    }

    @OnClick(R.id.select_contacts_fab_button)
    public void onSelectContactsButtonClicked() {
        presenter.onSelectContactsButtonClicked();
    }

    @Override
    public void showSelectContactsScreen() {
        startActivity(SelectContactsActivity.makeIntent(this));
    }
}
