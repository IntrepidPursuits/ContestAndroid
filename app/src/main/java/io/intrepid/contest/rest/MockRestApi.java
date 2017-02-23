package io.intrepid.contest.rest;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Contest;
import io.intrepid.contest.models.Entry;
import io.intrepid.contest.models.Participant;
import io.intrepid.contest.models.ParticipationType;
import io.intrepid.contest.models.User;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Path;

public class MockRestApi implements RestApi {
    private static final String TEST_JUDGE_CODE = "judge";
    private static final String TEST_ENTRY_IMAGE = "https://www.chowstatic.com/assets/2014/09/30669_spicy_slow_cooker_beef_chili_3000x2000.jpg";

    private final UUID userId;
    private UUID contestId;
    private String contestTitle;
    private String contestDescription;
    private int numGetContestStatusCallsForParticipant;

    public MockRestApi() {
        userId = UUID.randomUUID();
        contestId = UUID.randomUUID();
        contestTitle = "Contest title";
        contestDescription = "Contest Description";
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

    @Override
    public Observable<BatchInviteResponse> batchInvite(@Path("contestId") String contestId,
                                                       @Body BatchInviteRequest batchInviteRequest) {
        if (contestId == null) {
            return Observable.error(new Throwable());
        }

        BatchInviteResponse batchInviteResponse = new BatchInviteResponse();
        batchInviteResponse.invitationResponses = new ArrayList<>();
        int numRequests = batchInviteRequest.invitations.invitationList.size();
        for (int i = 0; i < numRequests; i++) {
            InvitationResponse response = new InvitationResponse();
            response.code = "code" + i;
            batchInviteResponse.invitationResponses.add(response);
        }

        return Observable.just(batchInviteResponse);
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

    @Override
    public Observable<ContestWrapper> submitContest(@Body ContestWrapper contest) {
        return Observable.just(getValidContestResponse());
    }

    @NonNull
    private EntryResponse getValidEntryResponse() {
        EntryResponse entryResponse = new EntryResponse();
        entryResponse.setEntry(new Entry());
        return entryResponse;
    }

    @Override
    public Observable<EntryResponse> createEntry(@Path("contestId") String contestId, @Body EntryRequest entryRequest) {
        return Observable.just(getValidEntryResponse());
    }

    @NonNull
    private ContestStatusResponse getValidStatusResponseWaitingForSubmissions() {
        ContestStatusResponse response = new ContestStatusResponse();
        response.contestStatus = new ContestStatus();
        response.contestStatus.setSubmissionData(false, 5, 10);
        response.contestStatus.setJudgeData(false, 0, 6);
        return response;
    }

    @NonNull
    private ContestStatusResponse getValidStatusResponseWaitingForScores() {
        ContestStatusResponse response = new ContestStatusResponse();
        response.contestStatus = new ContestStatus();
        response.contestStatus.setSubmissionData(true, 10, 10);
        response.contestStatus.setJudgeData(false, 2, 6);
        return response;
    }

    @NonNull
    private ContestStatusResponse getValidStatusResponseResultsAvailable() {
        ContestStatusResponse response = new ContestStatusResponse();
        response.contestStatus = new ContestStatus();
        response.contestStatus.setSubmissionData(true, 10, 10);
        response.contestStatus.setJudgeData(true, 6, 6);
        return response;
    }

    @Override
    public Observable<ContestStatusResponse> getContestStatus(@Path("contestId") String contestId) {
        numGetContestStatusCallsForParticipant++;

        switch (numGetContestStatusCallsForParticipant) {
            case 1: return Observable.just(getValidStatusResponseWaitingForSubmissions());
            case 2: return Observable.just(getValidStatusResponseWaitingForScores());
            default:
                numGetContestStatusCallsForParticipant = 0;
                return Observable.just(getValidStatusResponseResultsAvailable());
        }
    }

    @NonNull
    private ContestWrapper getValidContestResponse() {
        Contest contest = new Contest();
        contest.setId(UUID.randomUUID());
        contest.setTitle(contestTitle);
        contest.setDescription(contestDescription);
        List<Category> contestCategories = contest.getCategories();
        for (int i = 0; i < 5; i++) {
            contestCategories.add(new Category("Category " + i, "This is category " + i));
        }
        contest.setEntries(makeListOfEntries());
        return new ContestWrapper(contest);
    }

    private List<Entry> makeListOfEntries() {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Entry entry = new Entry();
            entry.title = "Test Entry " + i;
            entry.photoUrl = TEST_ENTRY_IMAGE;
            entries.add(entry);
        }
        return entries;
    }

    @Override
    public Observable<ContestWrapper> getContestDetails(@Path("contestId") String contestId) {
        return Observable.just(getValidContestResponse());
    }
}
