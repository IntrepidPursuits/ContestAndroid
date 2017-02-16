package io.intrepid.contest.screens.sendinvitations.selectcontacts;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.intrepid.contest.models.Contact;

class SelectContactsAdapter extends RecyclerView.Adapter<SelectContactsViewHolder> {

    private final List<Contact> contactsList = new ArrayList<>();
    private final SelectContactsViewHolder.ContactClickListener onContactClickListener;
    private final boolean displayContactSelection;

    public SelectContactsAdapter(@NonNull SelectContactsViewHolder.ContactClickListener onContactClickListener,
                                 boolean displayContactSelection) {
        this.onContactClickListener = onContactClickListener;
        this.displayContactSelection = displayContactSelection;
    }

    @Override
    public SelectContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectContactsViewHolder(parent, this.onContactClickListener);
    }

    @Override
    public void onBindViewHolder(SelectContactsViewHolder holder, int position) {
        holder.bindData(contactsList.get(position), displayContactSelection);
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public void updateContactList(@NonNull List<Contact> contactsList) {
        this.contactsList.clear();
        this.contactsList.addAll(contactsList);
        notifyDataSetChanged();
    }

    public void clear() {
        contactsList.clear();
    }

    public void onContactSelected() {
        notifyDataSetChanged();
    }
}
