package io.intrepid.contest.models

import com.google.gson.annotations.SerializedName

class Adjudication {
    @SerializedName("entries")
    var entryBallots: List<EntryBallot>? = null
}
