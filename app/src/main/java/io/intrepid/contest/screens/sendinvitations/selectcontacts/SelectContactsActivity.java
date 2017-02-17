package io.intrepid.contest.screens.sendinvitations.selectcontacts;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseMvpActivity;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.models.Contact;
import timber.log.Timber;

import static android.provider.ContactsContract.CommonDataKinds;
import static android.provider.ContactsContract.Contacts;
import static android.provider.ContactsContract.Data;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SelectContactsActivity extends BaseMvpActivity<SelectContactsContract.Presenter>
        implements SelectContactsContract.View, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String ACTION_BAR_TITLE = "";
    private static final int CONTACTS_LOADER = 0;

    // Columns to read from the Contacts table
    private static final String[] CONTACT_PROJECTION = {
            Contacts._ID,
            Contacts.LOOKUP_KEY,
            Contacts.DISPLAY_NAME_PRIMARY
    };
    private static final int CONTACTS_PROJECTION_INDEX_CONTACT_ID = 0;
    private static final int CONTACTS_PROJECTION_INDEX_CONTACT_LOOKUP_KEY = 1;
    private static final int CONTACTS_PROJECTION_INDEX_CONTACT_DISPLAY_NAME = 2;

    private static final String CONTACT_SELECTION =
            Contacts.DISPLAY_NAME_PRIMARY + " LIKE ?";
    private static final String CONTACT_SELECTION_ORDER =
            Contacts.DISPLAY_NAME_PRIMARY + " ASC";
    private static final String SEARCH_WILDCARD = "%";

    // Columns to read from the Contacts Data table
    private static final String[] DATA_PROJECTION = {
            Data._ID,
            Data.MIMETYPE,
            Data.DATA1,
            Data.DATA15
    };
    private static final int DATA_PROJECTION_INDEX_MIMETYPE = 1;
    private static final int DATA_PROJECTION_INDEX_DATA1 = 2;
    private static final int DATA_PROJECTION_INDEX_DATA15 = 3;

    private static final String DATA_SELECTION = Data.LOOKUP_KEY + " = ?" +
            " AND (" +
            Data.MIMETYPE + " = '" +
            CommonDataKinds.Email.CONTENT_ITEM_TYPE +
            "' OR " +
            Data.MIMETYPE + " = '" +
            CommonDataKinds.Phone.CONTENT_ITEM_TYPE +
            "' OR " +
            Data.MIMETYPE + " = '" +
            CommonDataKinds.Photo.CONTENT_ITEM_TYPE +
            "')";

    @BindView(R.id.select_contacts_recycler_view)
    RecyclerView selectContactsRecyclerView;
    @BindView(R.id.add_participants_button)
    Button addParticipantsButton;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private String contactSearchString = "";
    private String[] contactSelectionArgs = { contactSearchString };
    private SelectContactsAdapter selectContactsAdapter;

    public static Intent makeIntent(Context context) {
        return new Intent(context, SelectContactsActivity.class);
    }

    @NonNull
    @Override
    public SelectContactsContract.Presenter createPresenter(PresenterConfiguration configuration) {
        return new SelectContactsPresenter(this, configuration);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.select_contacts_fragment;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);

        setActionBarTitle(ACTION_BAR_TITLE);
        setActionBarDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem searchItem = menu.add(R.string.common_search);
        searchItem.setIcon(R.drawable.search_icon);
        searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        SearchView searchView = new SearchView(this);
        searchView.setOnQueryTextListener(presenter);
        searchItem.setActionView(searchView);
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
    public void goBackToPreviousScreen() {
        super.onBackPressed();
    }

    @Override
    public void displayContactList() {
        selectContactsAdapter = new SelectContactsAdapter(presenter);
        selectContactsRecyclerView.setAdapter(selectContactsAdapter);

        getSupportLoaderManager().initLoader(CONTACTS_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String currentSearchParam = SEARCH_WILDCARD + contactSearchString + SEARCH_WILDCARD;
        contactSelectionArgs[0] = currentSearchParam;

        return new CursorLoader(
                this,
                Contacts.CONTENT_URI,
                CONTACT_PROJECTION,
                CONTACT_SELECTION,
                contactSelectionArgs,
                CONTACT_SELECTION_ORDER
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        List<Contact> contacts = new ArrayList<>();

        while (cursor.moveToNext()) {
            String[] lookupKey = { cursor.getString(CONTACTS_PROJECTION_INDEX_CONTACT_LOOKUP_KEY) };
            String displayName = cursor.getString(CONTACTS_PROJECTION_INDEX_CONTACT_DISPLAY_NAME);
            String phone = "";
            String email = "";
            byte[] photo = null;

            Cursor details = getContentResolver().query(
                    Data.CONTENT_URI,
                    DATA_PROJECTION,
                    DATA_SELECTION,
                    lookupKey,
                    null);
            while (details.moveToNext()) {
                String mime = details.getString(DATA_PROJECTION_INDEX_MIMETYPE);
                switch (mime) {
                    case CommonDataKinds.Phone.CONTENT_ITEM_TYPE:
                        phone = details.getString(DATA_PROJECTION_INDEX_DATA1);
                        break;
                    case CommonDataKinds.Email.CONTENT_ITEM_TYPE:
                        email = details.getString(DATA_PROJECTION_INDEX_DATA1);
                        break;
                    case CommonDataKinds.Photo.CONTENT_ITEM_TYPE:
                        photo = details.getBlob(DATA_PROJECTION_INDEX_DATA15);
                        break;
                }
            }
            details.close();

            Timber.d("Contact name: " + displayName + ", phone: " + phone + ", email: " + email);
            Contact contact = new Contact();
            contact.setName(displayName);
            contact.setPhone(phone);
            contact.setEmail(email);
            contact.setPhoto(photo);
            contacts.add(contact);
        }

        presenter.onContactListUpdated(contacts);
    }

    @Override
    public void updateContactList(List<Contact> contacts) {
        selectContactsAdapter.updateContactList(contacts);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        selectContactsAdapter.clear();
    }

    @Override
    public void updateContactSearchFilter(String newFilter) {
        contactSearchString = newFilter;
        getSupportLoaderManager().restartLoader(CONTACTS_LOADER, null, this);
    }

    @Override
    public void onContactSelected() {
        selectContactsAdapter.onContactSelected();
    }

    @Override
    public void showAddContestantButton(int numContestants) {
        String contestants = getResources()
                .getQuantityString(R.plurals.numberOfContestants, numContestants, numContestants);
        addParticipantsButton.setText(getResources().getString(R.string.add_participants_quantified_action,
                                                               contestants));
        addParticipantsButton.setVisibility(VISIBLE);
    }

    @Override
    public void hideAddContestantButton() {
        addParticipantsButton.setVisibility(GONE);
    }

    @Override
    public void showProgressBar(boolean visible) {
        progressBar.setVisibility(visible ? VISIBLE : GONE);
    }
}
