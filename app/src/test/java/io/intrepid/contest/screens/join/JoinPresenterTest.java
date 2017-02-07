package io.intrepid.contest.screens.join;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import io.intrepid.contest.models.Participant;
import io.intrepid.contest.models.ParticipationType;
import io.intrepid.contest.rest.RedeemInvitationResponse;
import io.intrepid.contest.screens.join.JoinContract.View;
import io.intrepid.contest.testutils.BasePresenterTest;
import io.reactivex.Observable;

import static io.reactivex.Observable.error;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JoinPresenterTest extends BasePresenterTest<JoinPresenter> {
    @Mock
    View mockView;

    private JoinContract.Presenter presenter;
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

        presenter.onSubmitButtonClicked("CODE");
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

        presenter.onSubmitButtonClicked("judge");
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showContestStatusScreen();
    }

    @Test
    public void onSubmitButtonClickedShouldShowInvalidCodeErrorMessageWhenCodeIsInvalid() {
        RedeemInvitationResponse mockRedeemInvitationResponse = mock(RedeemInvitationResponse.class);
        when(mockRestApi.redeemInvitationCode(any(), any())).thenReturn(Observable.just(mockRedeemInvitationResponse));

        presenter.onSubmitButtonClicked("CODE");
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showInvalidCodeErrorMessage();
    }

    @Test
    public void onSubmitButtonClickedShouldShowApiErrorMessageWhenApiCallThrowsError() throws HttpException {
        when(mockRestApi.redeemInvitationCode(any(), any())).thenReturn(error(throwable));

        presenter.onSubmitButtonClicked("CODE");
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showMessage(any(int.class));
    }

    @Test
    public void onSubmitButtonClickedShouldShowApiErrorMessageWhenApiCallThrowsErrorForJudgeCode() throws HttpException {
        when(mockRestApi.redeemInvitationCode(any(), any())).thenReturn(error(throwable));

        presenter.onSubmitButtonClicked("judge");
        testConfiguration.triggerRxSchedulers();

        verify(mockView).showMessage(any(int.class));
    }
}