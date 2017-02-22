package io.intrepid.contest.screens.sendinvitations.invitationintro;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import io.intrepid.contest.R;
import io.intrepid.contest.base.BaseFragment;
import io.intrepid.contest.base.PresenterConfiguration;
import io.intrepid.contest.screens.sendinvitations.SendInvitationsActivity;
import io.intrepid.contest.screens.sendinvitations.SendInvitationsActivityContract;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class InvitationIntroFragment extends BaseFragment<InvitationIntroContract.Presenter>
        implements InvitationIntroContract.View {

    private static final String PACKAGE_KEY = "package";

    @BindView(R.id.send_invitations_icon)
    ImageView sendInvitationsIcon;
    @BindView(R.id.send_invitations_intro_text_view)
    TextView sendInvitationsIntroTextView;

    @NonNull
    @Override
    public InvitationIntroContract.Presenter createPresenter(PresenterConfiguration configuration) {
        SendInvitationsActivityContract activity = (SendInvitationsActivity) getActivity();

        return new InvitationIntroPresenter(this, configuration, activity.hasContactPermissions());
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_invitation_intro;
    }

    @Override
    public void showSelectContactsMessage() {
        sendInvitationsIcon.setVisibility(VISIBLE);
        sendInvitationsIntroTextView.setText(getResources().getString(R.string.invite_contestants_intro));
    }

    @Override
    public void showPermissionDeniedMessage(@StringRes int wrappingStringResource, @StringRes int clickableResource) {
        String clickableString = getString(clickableResource);
        String fullString = getString(wrappingStringResource, clickableString);
        SpannableString spannableString = new SpannableString(fullString);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                           Uri.fromParts(PACKAGE_KEY, getActivity().getPackageName(), null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };

        int indexClickStart = fullString.indexOf(clickableString);
        int indexClickEnd = indexClickStart + clickableString.length();

        spannableString.setSpan(clickableSpan, indexClickStart, indexClickEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(), indexClickStart, indexClickEnd, 0);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.colorAccent)),
                                indexClickStart, indexClickEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        sendInvitationsIntroTextView.setText(spannableString);
        sendInvitationsIntroTextView.setMovementMethod(LinkMovementMethod.getInstance());

        sendInvitationsIcon.setVisibility(GONE);
    }
}
