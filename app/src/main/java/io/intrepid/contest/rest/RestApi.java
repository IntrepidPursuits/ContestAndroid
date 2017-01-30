package io.intrepid.contest.rest;

import io.intrepid.contest.models.InvitationResponse;
import io.reactivex.Observable;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface RestApi {
    @PATCH("invitations/{code}/redeem")
    Observable<InvitationResponse> redeemInvitationCode(@Path("code") String code);
}
