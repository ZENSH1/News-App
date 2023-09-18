package com.xs.firenotesapp.data.users


data class UsersData(
    var id: String = "",
    var email: String = "",
    var name: String = "",
    var profileUri: String = "",
    var userRole: String = "",
    var age: String = ""


) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "email" to email,
            "name" to name,
            "profileUri" to profileUri,
            "userRole" to userRole,
            "age" to age,
            )
    }
}
