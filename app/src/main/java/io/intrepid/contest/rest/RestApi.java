package io.intrepid.contest.rest;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestApi {
    @PATCH("invitations/{code}/redeem")
    Observable<InvitationResponse> redeemInvitationCode(@Path("code") String code);

    @POST("contests/{contestId}/entries")
    Observable<EntryResponse> createEntry(@Path("contestId") String contestId, @Body EntryRequest entryRequest);

    @GET("contests/{contestId}/status")
    Observable<ContestStatusResponse> getContestStatus(@Path("contestId") String contestId);
}
