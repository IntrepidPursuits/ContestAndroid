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

public class SelectContactsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.select_contacts_photo_image_view)
    ImageView photoImageView;
    @BindView(R.id.select_contacts_name_text_view)
    TextView nameTextView;
    @BindView(R.id.select_contacts_phone_text_view)
    TextView phoneTextView;
    @BindView(R.id.select_contacts_email_text_view)
    TextView emailTextView;

    public SelectContactsViewHolder(ViewGroup parent) {
        super(inflateView(parent));

        ButterKnife.bind(this, itemView);
    }

    private static View inflateView(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return layoutInflater.inflate(R.layout.select_contacts_row_item, parent, false);
    }

    public void bindData(Contact contact) {
        nameTextView.setText(contact.getName());
        phoneTextView.setText(contact.getPhone());
        emailTextView.setText(contact.getEmail());

        byte[] photo = contact.getPhoto();
        if (photo != null) {
            photoImageView.setImageBitmap(BitmapFactory.decodeByteArray(photo, 0, photo.length));
        }
    }
}
