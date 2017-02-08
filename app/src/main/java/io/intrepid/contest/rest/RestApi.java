package io.intrepid.contest.rest;

import io.intrepid.contest.models.Contest;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestApi {
    @POST("users")
    Observable<UserCreationResponse> createUser();

    @PATCH("invitations/{code}/redeem")
    Observable<RedeemInvitationResponse> redeemInvitationCode(@Path("code") String code,
                                                              @Body RedeemInvitationRequest redeemInvitationRequest);

    @POST("/contests")
    Observable<ContestWrapper> submitContest(@Body ContestWrapper contest);

    @POST("contests/{contestId}/entries")
    Observable<EntryResponse> createEntry(@Path("contestId") String contestId, @Body EntryRequest entryRequest);

    @GET("contests/{contestId}/status")
    Observable<ContestStatusResponse> getContestStatus(@Path("contestId") String contestId);

    @GET("contests/{contestId}")
    Observable<ContestWrapper> getContestDetails(@Path("contestId") String contestId);
}
