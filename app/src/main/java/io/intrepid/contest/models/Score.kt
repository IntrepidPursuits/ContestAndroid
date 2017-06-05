package io.intrepid.contest.models

import com.google.gson.annotations.SerializedName

import java.util.UUID

class Score(category: Category, score: Int) {
    var scoreValue: Int = 0
        internal set
    @SerializedName("scoring_category_id")
    private val categoryId: UUID?
    @Transient val categoryName: String?
    @Transient val categoryDescription: String?

    init {
        this.categoryId = category.id
        this.categoryName = category.name
        this.categoryDescription = category.description
        this.scoreValue = score
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Score) {
            return false
        }
        return scoreValue == other.scoreValue
    }
}
