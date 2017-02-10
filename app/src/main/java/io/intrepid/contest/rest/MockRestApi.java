package io.intrepid.contest.rest;

import android.support.annotation.NonNull;

import java.util.UUID;

import io.intrepid.contest.models.Contest;
import io.intrepid.contest.models.Entry;
import io.intrepid.contest.models.Participant;
import io.intrepid.contest.models.ParticipationType;
import io.intrepid.contest.models.User;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Path;

import static io.reactivex.Observable.error;

public class MockRestApi implements RestApi {
    public static final String TEST_JUDGE_CODE = "judge";

    private final UUID userId;
    private UUID contestId;
    private String contestTitle;
    private int numGetContestStatusCallsForParticipant;

    public MockRestApi() {
        userId = UUID.randomUUID();
        contestId = UUID.randomUUID();
        contestTitle = "Contest title";
        numGetContestStatusCallsForParticipant = 0;
    }

    @NonNull
    private UserCreationResponse getValidUserCreationResponse() {
        UserCreationResponse userCreationResponse = new UserCreationResponse();
        userCreationResponse.user = new User();
        userCreationResponse.user.setId(userId);
        return userCreationResponse;
    }

    @Override
    public Observable<UserCreationResponse> createUser() {
        return Observable.just(getValidUserCreationResponse());
    }

    @NonNull
    private RedeemInvitationResponse getValidRedeemInvitationResponse(ParticipationType participationType) {
        RedeemInvitationResponse redeemInvitationResponse = new RedeemInvitationResponse();
        redeemInvitationResponse.participant = new Participant();
        redeemInvitationResponse.participant.setContestId(contestId);
        redeemInvitationResponse.participant.setParticipationType(participationType);
        return redeemInvitationResponse;
    }

    @Override
    public Observable<RedeemInvitationResponse> redeemInvitationCode(@Path("code") String code,
                                                                     @Body RedeemInvitationRequest redeemInvitationRequest) {
        if (contestId == null) {
            return Observable.error(new Throwable());
        }

        // Participant changed
        numGetContestStatusCallsForParticipant = 0;

        if (code.equals(TEST_JUDGE_CODE)) {
            return Observable.just(getValidRedeemInvitationResponse(ParticipationType.JUDGE));
        }
        return Observable.just(getValidRedeemInvitationResponse(ParticipationType.CONTESTANT));
    }

    @NonNull
    private EntryResponse getValidEntryResponse() {
        Entry entry = new Entry();
        entry.id = UUID.randomUUID();
        EntryResponse entryResponse = new EntryResponse();
        entryResponse.setEntry(entry);
        return entryResponse;
    }

    @Override
    public Observable<EntryResponse> createEntry(@Path("contestId") String contestId, @Body EntryRequest entryRequest) {
        return Observable.just(getValidEntryResponse());
    }

    @NonNull
    private ContestStatusResponse getValidEntryResponseWaitingForSubmissions() {
        ContestStatusResponse response = new ContestStatusResponse();
        response.setSubmissionData(false, 5, 10);
        response.setJudgeData(false, 0, 6);
        return response;
    }

    @NonNull
    private ContestStatusResponse getValidEntryResponseWaitingForScores() {
        ContestStatusResponse response = new ContestStatusResponse();
        response.setSubmissionData(true, 10, 10);
        response.setJudgeData(false, 2, 6);
        return response;
    }

    @Override
    public Observable<ContestStatusResponse> getContestStatus(@Path("contestId") String contestId) {
        numGetContestStatusCallsForParticipant++;

        if (numGetContestStatusCallsForParticipant == 1) {
            return Observable.just(getValidEntryResponseWaitingForSubmissions());
        }
        return Observable.just(getValidEntryResponseWaitingForScores());
    }

    @NonNull
    private ContestResponse getValidContestResponse() {
        ContestResponse response = new ContestResponse();
        response.contest = new Contest();
        response.contest.setTitle(contestTitle);
        return response;
    }

    @Override
    public Observable<ContestResponse> getContestDetails(@Path("contestId") String contestId) {
        return Observable.just(getValidContestResponse());
    }
}
