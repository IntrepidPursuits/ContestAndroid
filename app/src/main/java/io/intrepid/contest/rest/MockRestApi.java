package io.intrepid.contest.rest;

import android.support.annotation.NonNull;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.intrepid.contest.models.ActiveContestListResponse;
import io.intrepid.contest.models.Category;
import io.intrepid.contest.models.Contest;
import io.intrepid.contest.models.Entry;
import io.intrepid.contest.models.Participant;
import io.intrepid.contest.models.ParticipationType;
import io.intrepid.contest.models.RankedEntryResult;
import io.intrepid.contest.models.User;
import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Path;

import static io.intrepid.contest.models.ParticipationType.CONTESTANT;
import static io.intrepid.contest.models.ParticipationType.CREATOR;
import static io.intrepid.contest.models.ParticipationType.JUDGE;

class MockRestApi implements RestApi {
    private static final String TEST_JUDGE_CODE = "judge";
    private static final String TEST_CONTESTANT_CODE = "tester";
    private static final String TEST_ENTRY_IMAGE = "https://www.chowstatic.com/assets/2014/09/30669_spicy_slow_cooker_beef_chili_3000x2000.jpg";

    private final UUID userId;
    private UUID contestId;
    private String contestTitle = "Contest Title";
    private String contestDescription = "Contest Description";
    private int numGetContestStatusCallsForParticipant;
    private static final int NUMBER_OF_MOCK_CONTESTS = 5;
    private Map<String, Contest> ongoingContestMap;

    MockRestApi() {
        userId = UUID.randomUUID();
        contestId = UUID.randomUUID();
        numGetContestStatusCallsForParticipant = 0;
        populateOngoingContestMapData();
    }

    private void populateOngoingContestMapData() {
        ongoingContestMap = new HashMap<>(NUMBER_OF_MOCK_CONTESTS);
        for (int i = 0; i < NUMBER_OF_MOCK_CONTESTS; i++) {
            ParticipationType participationType = i == 0
                    ? CREATOR :
                    (i % 2 == 0 ? CONTESTANT : JUDGE);

            String title = i % 2 == 0 ? "Chili Cookoff" : "Intrepid Cookoff";
            String description = i % 2 == 0 ? "Annual Cookoff " : "Test Cookoff ";
            Contest contest = makeContest(title, description, participationType);
            ongoingContestMap.put(String.valueOf(contest.getId()), contest);
        }
    }

    private Contest makeContest(String title, String description, ParticipationType participationType) {
        Contest contest = new Contest();
        contest.setTitle(title);
        contest.setDescription(description);
        contest.setParticipationType(participationType);
        List<Category> contestCategories = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            contestCategories.add(new Category("Category " + i, "This is category " + i));
        }
        contest.setCategories(contestCategories);
        contest.setEntries(makeListOfEntries());
        return contest;
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
        } else if (code.equals(TEST_CONTESTANT_CODE)) {
            return Observable.just(getValidRedeemInvitationResponse(ParticipationType.CONTESTANT));
        }
        return Observable.just(new RedeemInvitationResponse());
    }

    @Override
    public Observable<ContestWrapper> closeSubmissions(@Path("id") String id) {
        Contest contest = ongoingContestMap.containsKey(id) ?
                ongoingContestMap.get(id) :
                makeValidContest();
        contest.setSubmissionsClosedAt(new Date());
        return Observable.just(new ContestWrapper(contest));
    }

    private Contest makeValidContest() {
        Iterator<Contest> iterator = ongoingContestMap.values().iterator();
        return iterator.hasNext()
                ? iterator.next() :
                makeContest(contestTitle, contestDescription, CONTESTANT);
    }

    public Observable<ContestWrapper> endContest(String id) {
        Contest contest = ongoingContestMap.containsKey(id) ? ongoingContestMap.get(id) : makeValidContest();
        contest.setEndedDate(new Date());
        return Observable.just(new ContestWrapper(contest));
    }

    @Override
    public Observable<ContestWrapper> submitContest(@Body ContestWrapper contestWrapper) {
        return Observable.just(contestWrapper);
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
            case 1:
                return Observable.just(getValidStatusResponseWaitingForSubmissions());
            case 2:
                return Observable.just(getValidStatusResponseWaitingForScores());
            default:
                numGetContestStatusCallsForParticipant = 0;
                return Observable.just(getValidStatusResponseResultsAvailable());
        }
    }

    private List<Entry> makeListOfEntries() {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Entry entry = new Entry();
            entry.setTitle("Test Entry " + i);
            entry.setPhotoUrl(TEST_ENTRY_IMAGE);
            entries.add(entry);
        }
        return entries;
    }

    @Override
    public Observable<ContestWrapper> getContestDetails(@Path("contestId") String contestId) {
        Contest contest = ongoingContestMap.containsKey(contestId) ?
                ongoingContestMap.get(contestId) :
                makeValidContest();
        return Observable.just(new ContestWrapper(contest));
    }

    private ContestResultResponse getValidContestResultResponse() {
        RankedEntryResult firstPlace = new RankedEntryResult();
        firstPlace.setTitle("Awesome Sauce");
        firstPlace.setOverallScore(4.25f);
        firstPlace.setRank(1);
        firstPlace.setPhotoUrl(
                "https://search.chow.com/thumbnail/800/600/www.chowstatic.com/assets/recipe_photos/10828_smoked_chili.jpg");

        RankedEntryResult secondPlace = new RankedEntryResult();
        secondPlace.setTitle("Dank Chili");
        secondPlace.setOverallScore(4.05f);
        secondPlace.setRank(2);
        secondPlace.setPhotoUrl("https://upload.wikimedia.org/wikipedia/commons/0/0f/Pot-o-chili.jpg");

        RankedEntryResult thirdPlace = new RankedEntryResult();
        thirdPlace.setTitle("Chill Chili");
        thirdPlace.setOverallScore(3.50f);
        thirdPlace.setRank(3);
        thirdPlace.setPhotoUrl(
                "http://cf.familyfreshmeals.com/wp-content/uploads/2015/10/Easy-Crockpot-Chili-Step-2.png");

        RankedEntryResult fourthPlace = new RankedEntryResult();
        fourthPlace.setTitle("Sweet Chili");
        fourthPlace.setOverallScore(3.25f);
        fourthPlace.setRank(4);

        List<RankedEntryResult> rankedScoredEntries = new ArrayList<>();
        rankedScoredEntries.add(firstPlace);
        rankedScoredEntries.add(secondPlace);
        rankedScoredEntries.add(thirdPlace);
        rankedScoredEntries.add(fourthPlace);

        return new ContestResultResponse(rankedScoredEntries);
    }

    @Override
    public Observable<ContestResultResponse> getContestResults(@Path("contestId") String contestId) {
        if (contestId == null) {
            return Observable.error(new Throwable());
        }
        return Observable.just(getValidContestResultResponse());
    }

    @Override
    public Observable<Response<Void>> adjudicate(@Path("contestId") String contestId,
                                                 @Body AdjudicateRequest adjudicateRequest) {
        if (contestId == null) {
            return Observable.just(Response.error(HttpURLConnection.HTTP_NOT_FOUND,
                                                  ResponseBody.create(MediaType.parse("application/json"),
                                                                      "{\"errors\":[\"Error\"]}")));
        }
        ongoingContestMap.put(contestId, makeValidContest()); //replace scored contest
        return Observable.just(Response.success(null));
    }

    @Override
    public Observable<ActiveContestListResponse> getActiveContests() {
        List<Contest> contests = new ArrayList<>();
        contests.addAll(ongoingContestMap.values());
        return Observable.just(new ActiveContestListResponse(contests));
    }
}
