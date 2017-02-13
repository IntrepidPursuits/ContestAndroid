package io.intrepid.contest.rest;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestApi {
    @POST("api/users")
    Observable<UserCreationResponse> createUser();

    @PATCH("api/invitations/{code}/redeem")
    Observable<RedeemInvitationResponse> redeemInvitationCode(@Path("code") String code,
                                                              @Body RedeemInvitationRequest redeemInvitationRequest);

    @POST("api/contests")
    Observable<ContestWrapper> submitContest(@Body ContestWrapper contest);

    @POST("api/contests/{contestId}/entries")
    Observable<EntryResponse> createEntry(@Path("contestId") String contestId, @Body EntryRequest entryRequest);

    @GET("api/contests/{contestId}/status")
    Observable<ContestStatusResponse> getContestStatus(@Path("contestId") String contestId);

    @GET("api/contests/{contestId}")
    Observable<ContestWrapper> getContestDetails(@Path("contestId") String contestId);
}
