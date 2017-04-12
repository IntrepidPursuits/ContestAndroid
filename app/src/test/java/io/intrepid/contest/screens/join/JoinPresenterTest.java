package io.intrepid.contest.screens.join;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.HttpURLConnection;
import java.util.UUID;

import io.intrepid.contest.models.Participant;
import io.intrepid.contest.models.ParticipationType;
import io.intrepid.contest.rest.RedeemInvitationResponse;
import io.intrepid.contest.screens.join.JoinContract.View;
import io.intrepid.contest.testutils.BasePresenterTest;
import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static io.reactivex.Observable.error;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JoinPresenterTest extends BasePresenterTest<JoinPresenter> {
    private static final String CODE = "CODE";
    private static final String JUDGE_CODE = "judge";
    private static final String VALID_POTENTIAL_CODE = "KbUEcko";
    private static final String INVALID_POTENTIAL_CODE = "kbuecko";

    @Mock
    View mockView;

    private JoinPresenter presenter;
    private Throwable throwable;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        throwable = new Throwable();
        presenter = new JoinPresenter(mockView, testConfiguration);
    }

    @Test
    public void onEntryCodeTextViewChangedShouldHideSubmitButtonWhenCodeIsEmpty() {
        String newCode = "";

        presenter.onEntryCodeTextChanged(newCode);

        verify(mockView).hideSubmitButton();
        verify(mockView, never()).showSubmitButton();
    }

    @Test
    public void onEntryCodeTextViewChangedShouldShowSubmitButtonWhenCodeIsNotEmpty() {
        String newCode = "1";

        presenter.onEntryCodeTextChanged(newCode);

        verify(mockView).showSubmitButton();
        verify(mockView, never()).hideSubmitButton();
    }

    @Test
    public void onSubmitButtonClickedShouldShowEntryNameScreenWhenCodeIsValidForContestant() {
        RedeemInvitationResponse redeemInvitationResponse = new RedeemInvitationResponse();
        redeemInvitationResponse.participant = new Participant();
        redeemInvitationResponse.participant.setContestId(UUID.randomUUID());
        redeemInvitationResponse.participant.setParticipationType(ParticipationType.CONTESTANT);
        when(mockRestApi.redeemInvitationCode(any(), any())).thenReturn(Observable.just(redeemInvitationResponse));

        presenter.onSubmitButtonClicked(CODE);
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showEntryNameScreen();
    }

    @Test
    public void onSubmitButtonClickedShouldShowContestStatusScreenWhenCodeIsValidForJudge() {
        RedeemInvitationResponse redeemInvitationResponse = new RedeemInvitationResponse();
        redeemInvitationResponse.participant = new Participant();
        redeemInvitationResponse.participant.setContestId(UUID.randomUUID());
        redeemInvitationResponse.participant.setParticipationType(ParticipationType.JUDGE);
        when(mockRestApi.redeemInvitationCode(any(), any())).thenReturn(Observable.just(redeemInvitationResponse));

        presenter.onSubmitButtonClicked(JUDGE_CODE);
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showContestStatusScreen();
    }

    @Test
    public void onSubmitButtonClickedShouldShowInvalidCodeErrorMessageWhenCodeIsInvalid() {
        RedeemInvitationResponse mockRedeemInvitationResponse = mock(RedeemInvitationResponse.class);
        when(mockRestApi.redeemInvitationCode(any(), any())).thenReturn(Observable.just(mockRedeemInvitationResponse));

        presenter.onSubmitButtonClicked(CODE);
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showInvalidCodeErrorMessage();
    }

    @Test
    public void onSubmitButtonClickedShouldShowInvalidCodeErrorMessageWhenApiCallThrowsNotFoundError() {
        HttpException notFoundException = new HttpException(Response.error(
                HttpURLConnection.HTTP_NOT_FOUND,
                ResponseBody.create(MediaType.parse("application/json"),
                                    "{\"errors\":[\"Couldn't find Invitation\"]}")));
        when(mockRestApi.redeemInvitationCode(any(), any())).thenReturn(error(notFoundException));

        presenter.onSubmitButtonClicked(CODE);
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showInvalidCodeErrorMessage();
    }

    @Test
    public void onSubmitButtonClickedShouldShowApiErrorMessageWhenApiCallThrowsUntreatedErrors() throws HttpException {
        when(mockRestApi.redeemInvitationCode(any(), any())).thenReturn(error(throwable));

        presenter.onSubmitButtonClicked(CODE);
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showMessage(any(int.class));
    }

    @Test
    public void onBackPressedShouldCauseViewToCancelJoinContest() {
        presenter.onBackPressed();
        verify(mockView).cancelJoinContest();
    }

    @Test
    public void onViewBoundShouldCauseViewToShowClipboardDataWhenClipboardDataIsValid() {
        when(mockView.getLastCopiedText()).thenReturn(VALID_POTENTIAL_CODE);

        presenter.onViewBound();

        verify(mockView).showClipboardData(VALID_POTENTIAL_CODE);
    }

    @Test
    public void onViewBoundShouldCauseViewToDoNothingWhenClipboardDataIsInvalid() {
        when(mockView.getLastCopiedText()).thenReturn(INVALID_POTENTIAL_CODE);

        presenter.onViewBound();

        verify(mockView, never()).showClipboardData(INVALID_POTENTIAL_CODE);
    }
}
