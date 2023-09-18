package com.xs.firenotesapp.data.news

import com.google.firebase.firestore.PropertyName

data class NewsData(
    @get:PropertyName("id")
    var id: String = "",
    @get:PropertyName("description") @set:PropertyName("description")
    var description: String = "",
    @get:PropertyName("headline") @set:PropertyName("headline")
    var headline: String = "",
    @get:PropertyName("author") @set:PropertyName("author")
    var author: String = "",
    @get:PropertyName("date") @set:PropertyName("date")
    var date: String = "",
    @get:PropertyName("userID") @set:PropertyName("userID")
    var userID:String = "Guest"

    ){
    fun toMap(): Map<String, Any> {
        return mapOf(
            "headline" to headline,
            "description" to description,
        )
    }
}

