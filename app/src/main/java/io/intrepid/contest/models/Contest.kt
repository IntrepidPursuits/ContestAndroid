package io.intrepid.contest.models

import android.os.Parcel
import com.google.gson.annotations.SerializedName
import timber.log.Timber
import java.util.*

open class Contest {
    @SerializedName("id")
    var id: UUID? = null
    var creatorId: UUID? = null
    @SerializedName("created_at")
    var creationDate: Date? = null
    @SerializedName("updated_at")
    var lastUpdatedDate: Date? = null
    @SerializedName("ended_at")
    var endedDate: Date? = null
    set(value) {
        this.endedDate = endedDate
    }
    var title: String? = null
    var description: String? = null
    @SerializedName("scoring_categories")
    var categories: List<Category>? = null
    var entries: List<Entry>? = null
    var submissionsClosedAt: Date? = null
    var participationType: ParticipationType? = null

    init {
        id = UUID.randomUUID()
        categories = ArrayList<Category>()
    }

    override fun toString(): String {
        return title + "/n" +
                description + "/n" +
                id + "/n" +
                creatorId + " " + creationDate +
                " /n size of categories was " + categories!!.size +
                "categories " + categories +
                " /n Last updated " + lastUpdatedDate +
                " /n Ended Data " + endedDate
    }

    class Builder {
        var categories: List<Category> = ArrayList()
        var title: String? = null
        var description: String? = null
        internal var contestId: UUID? = null
        internal var creatorId: UUID? = null
        internal var creationDate: Date? = null
        internal var lastUpdatedDate: Date? = null
        internal var endedDate: Date? = null

        constructor() : this(UUID(UUID_MIN_LIMIT.toLong(), UUID_MAX_LIMIT.toLong())) {}

        internal constructor(creatorId: UUID) {
            this.creatorId = creatorId
            this.contestId = UUID(UUID_MIN_LIMIT.toLong(), UUID_MAX_LIMIT.toLong())
        }

        protected constructor(`in`: Parcel) {
            title = `in`.readString()
            description = `in`.readString()
        }

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setDescription(description: String): Builder {
            this.description = description
            return this
        }

        fun build(): Contest {
            if (creationDate == null) {
                creationDate = Date()
            }
            if (lastUpdatedDate == null) {
                lastUpdatedDate = Date()
            }
            Timber.d(this.toString())
            val contest = Contest()
            contest.id = this.contestId
            contest.creatorId = this.creatorId
            contest.lastUpdatedDate = Date()
            contest.creationDate = this.creationDate
            contest.endedDate = this.endedDate
            contest.title = this.title
            contest.description = this.description
            contest.categories = this.categories
            return contest
        }

        companion object {
            internal val UUID_MIN_LIMIT = 0
            internal val UUID_MAX_LIMIT = Integer.MAX_VALUE
        }
    }
}
