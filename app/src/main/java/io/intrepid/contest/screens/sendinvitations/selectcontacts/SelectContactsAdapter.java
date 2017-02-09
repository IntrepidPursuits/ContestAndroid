package io.intrepid.contest.screens.sendinvitations.selectcontacts;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

import io.intrepid.contest.models.Contact;

public class SelectContactsAdapter extends RecyclerView.Adapter<SelectContactsViewHolder> {

    private ArrayList<Contact> contactsList;

    public SelectContactsAdapter(@NonNull ArrayList<Contact> contactsList) {
        updateContactList(contactsList);
    }

    @Override
    public SelectContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectContactsViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(SelectContactsViewHolder holder, int position) {
        holder.bindData(contactsList.get(position));
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public void updateContactList(@NonNull ArrayList<Contact> contactsList) {
        this.contactsList = contactsList;
    }

    public void clear() {
        contactsList.clear();
    }
}
