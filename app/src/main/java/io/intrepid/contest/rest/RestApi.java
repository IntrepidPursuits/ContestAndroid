package io.intrepid.contest.rest;

import io.intrepid.contest.models.ActiveContestListResponse;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestApi {
    @POST("api/users")
    Observable<UserCreationResponse> createUser();

    @POST("api/contests/{contestId}/batch_invite")
    Observable<BatchInviteResponse> batchInvite(@Path("contestId") String contestId,
                                                @Body BatchInviteRequest batchInviteRequest);

    @PATCH("api/invitations/{code}/redeem")
    Observable<RedeemInvitationResponse> redeemInvitationCode(@Path("code") String code,
                                                              @Body RedeemInvitationRequest redeemInvitationRequest);

    @PATCH("api/admin/contests/{id}/close_submissions")
    Observable<ContestWrapper> closeSubmissions(@Path("id") String id);

    @PATCH("api/admin/contests/{id}/end")
    Observable<ContestWrapper> endContest(@Path("id") String id);

    @POST("api/contests")
    Observable<ContestWrapper> submitContest(@Body ContestWrapper contest);

    @POST("api/contests/{contestId}/entries")
    Observable<EntryResponse> createEntry(@Path("contestId") String contestId, @Body EntryRequest entryRequest);

    @GET("api/contests/{contestId}/status")
    Observable<ContestStatusResponse> getContestStatus(@Path("contestId") String contestId);

    @GET("api/contests/{contestId}")
    Observable<ContestWrapper> getContestDetails(@Path("contestId") String contestId);

    @GET("api/contests/{contestId}/results")
    Observable<ContestResultResponse> getContestResults(@Path("contestId") String contestId);

    @POST("api/contests/{contestId}/adjudicate")
    Observable<Response<Void>> adjudicate(@Path("contestId") String contestId,
                                          @Body AdjudicateRequest adjudicateRequest);

    @GET("/api/{currentUser}/contests")
    Observable<ActiveContestListResponse> getActiveContests(@Path("currentUser") String currentUser);
}
