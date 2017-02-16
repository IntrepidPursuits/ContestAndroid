package io.intrepid.contest.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.intrepid.contest.models.Contact;
import io.intrepid.contest.models.ParticipationType;

public class InvitationRequest {
    private static final String EMAIL_KEY = "email";
    private static final String PHONE_KEY = "phone";
    final List<Map<String, String>> invitationList = new ArrayList<>();
    private String contestId;
    private ParticipationType invitationType;

    public InvitationRequest(String contestId, ParticipationType invitationType, List<Contact> contactList) {
        this.contestId = contestId;
        this.invitationType = invitationType;

        for (Contact contact : contactList) {
            Map<String, String> invitee = new HashMap<>();
            invitee.put(EMAIL_KEY, contact.getEmail());
            invitee.put(PHONE_KEY, contact.getPhone());
            invitationList.add(invitee);
        }
    }
}
