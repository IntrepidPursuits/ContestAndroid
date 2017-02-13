package io.intrepid.contest.screens.sendinvitations.selectcontacts;

import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.intrepid.contest.R;
import io.intrepid.contest.models.Contact;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SelectContactsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.select_contacts_photo_image_view)
    ImageView photoImageView;
    @BindView(R.id.select_contacts_name_text_view)
    TextView nameTextView;
    @BindView(R.id.select_contacts_phone_text_view)
    TextView phoneTextView;
    @BindView(R.id.select_contacts_email_text_view)
    TextView emailTextView;

    private Contact viewHolderContact;

    public SelectContactsViewHolder(ViewGroup parent, ContactClickListener onContactClickListener) {
        super(inflateView(parent));

        ButterKnife.bind(this, itemView);

        itemView.setOnClickListener(view -> onContactClickListener.onContactClick(viewHolderContact));
    }

    private static View inflateView(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return layoutInflater.inflate(R.layout.select_contacts_row_item, parent, false);
    }

    public void bindData(Contact contact) {
        viewHolderContact = contact;
        nameTextView.setText(contact.getName());

        if (!contact.getEmail().isEmpty()) {
            emailTextView.setText(contact.getEmail());
            emailTextView.setVisibility(VISIBLE);
            phoneTextView.setVisibility(GONE);
        } else {
            phoneTextView.setText(contact.getPhone());
            phoneTextView.setVisibility(VISIBLE);
            emailTextView.setVisibility(GONE);
        }

        if (contact.isSelected()) {
            photoImageView.setImageResource(R.drawable.selected_contact_icon);
        } else {
            byte[] photo = contact.getPhoto();
            if (photo == null) {
                photoImageView.setImageResource(R.drawable.default_user_photo);
            } else {
                photoImageView.setImageBitmap(BitmapFactory.decodeByteArray(photo, 0, photo.length));
            }
        }
    }

    public interface ContactClickListener {
        void onContactClick(Contact contact);
    }
}
