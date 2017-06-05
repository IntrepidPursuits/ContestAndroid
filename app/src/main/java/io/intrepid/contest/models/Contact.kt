package io.intrepid.contest.models

class Contact {
    var id: Long = 0
    var name: String? = null
    var phone: String? = null
    var email: String? = null
    var photo: ByteArray? = null
    var isSelected = false
    var isEnabled = true
}
