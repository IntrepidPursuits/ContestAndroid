package io.intrepid.contest.models

import java.util.ArrayList
import java.util.UUID

open class EntryBallot(private val entryId: UUID) {
    private val scores = ArrayList<Score>()

    fun addScore(score: Score) {
        scores.add(score)
    }

    fun setScore(scoreIndex: Int, newRating: Int) {
        val score = scores[scoreIndex]
        score.scoreValue = newRating
    }

    fun getScores(): List<Score> {
        return scores
    }

    val isCompletelyScored: Boolean
        get() {
            for (score in scores) {
                if (score.scoreValue == 0) {
                    return false
                }
            }
            return true
        }
}
