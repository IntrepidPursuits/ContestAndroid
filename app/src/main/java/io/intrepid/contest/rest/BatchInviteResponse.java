package io.intrepid.contest.rest;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BatchInviteResponse {
    @SerializedName("invitations")
    public List<InvitationResponse> invitationResponses;
}
