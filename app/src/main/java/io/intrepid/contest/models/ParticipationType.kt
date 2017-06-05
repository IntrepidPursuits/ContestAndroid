package io.intrepid.contest.models

import com.google.gson.annotations.SerializedName

enum class ParticipationType {
    CREATOR,
    @SerializedName("contestant")
    CONTESTANT,
    @SerializedName("judge")
    JUDGE
}
