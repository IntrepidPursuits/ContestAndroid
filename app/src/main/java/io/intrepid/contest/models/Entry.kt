package io.intrepid.contest.models

import android.support.v4.util.SparseArrayCompat
import java.util.*

open class Entry {
    var id: UUID
    var title: String? = null
    var createdAt: Date? = null
    var updatedAt: Date? = null
    var photoUrl: String? = null
    @Transient private val mapOfIndexToScore = SparseArrayCompat<Int>()
    @Transient private var categoriesSize: Int = 0

    init {
        id = UUID.randomUUID()
    }

    val isCompletelyScored: Boolean
        get() = mapOfIndexToScore.size() == categoriesSize

    val ratingAverage: Int
        get() {
            var sum = 0
            for (index in 0..mapOfIndexToScore.size() - 1) {
                sum += mapOfIndexToScore.get(index)
            }
            return sum / categoriesSize
        }

    fun getScoreAt(categoryIndex: Int): Int {
        return mapOfIndexToScore.get(categoryIndex)
    }

    fun setCategoriesSize(categoriesSize: Int) {
        this.categoriesSize = categoriesSize
    }

    fun acceptScore(position: Int, newRating: Int) {
        mapOfIndexToScore.put(position, newRating)
    }
}
