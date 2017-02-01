package io.intrepid.contest.rest;

import io.intrepid.contest.models.Entry;

public class EntryResponse {
    private Entry entry;

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }
}
