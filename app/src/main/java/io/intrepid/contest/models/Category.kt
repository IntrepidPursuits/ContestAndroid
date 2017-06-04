package io.intrepid.contest.models

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class Category : Parcelable {
    val id: UUID? = null
    var name: String? = null
    var description: String? = null

    constructor(name: String, description: String) {
        this.name = name
        this.description = description
    }

    protected constructor(`in`: Parcel) {
        name = `in`.readString()
        description = `in`.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(obj: Any?): Boolean {
        try {
            val otherCategory = obj as Category?
            return this.name == otherCategory!!.name && this.description == otherCategory.description
        } catch (exception: ClassCastException) {
            return false
        }
    }

    companion object {
        val CREATOR: Parcelable.Creator<Category> = object : Parcelable.Creator<Category> {
            override fun createFromParcel(`in`: Parcel): Category {
                return Category(`in`)
            }

            override fun newArray(size: Int): Array<Category?> {
                return arrayOfNulls(size)
            }
        }
    }
}
