package com.example.data.repository.asclient

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class User {
    @SerializedName("id")
    @Expose
    val id = 0

    @SerializedName("dog")
    @Expose
    val dog: Dog? = null

    @SerializedName("dogs")
    @Expose
    val dogs: List<Dog>? = null

    @SerializedName("is_active")
    @Expose
    val isActive = false

    @SerializedName("assistances")
    @Expose
    val assistances = 0

    @SerializedName("is_premium")
    @Expose
    val isPremium = false

    @SerializedName("wants_newsletter")
    @Expose
    val isWantsNewsletter = false

    @SerializedName("name")
    @Expose
    val name: String? = null

    @SerializedName("email")
    @Expose
    val email: String? = null

    @SerializedName("location")
    @Expose
    val location: Location? = null

    @SerializedName("email_verified_at")
    @Expose
    val emailVerifiedAt: String? = null

    @SerializedName("api_token")
    @Expose
    val apiKey: String? = null
}

class Dog {
    @SerializedName("id")
    @Expose
    var id = 0

    @SerializedName("breed_id")
    @Expose
    var breedId = 0

    @SerializedName("is_mongrel")
    @Expose
    var isMongrel = false

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("genre")
    @Expose
    var genre: String? = null

    @SerializedName("traits")
    @Expose
    var traits: ArrayList<String>? = null

    @SerializedName("born_at")
    @Expose
    var bornAt: String? = null
}


class Location {
    @SerializedName("postal_code")
    var postalCode: String? = null

    @SerializedName("city")
    var city: String? = null

    @SerializedName("country")
    var country: String? = null
}
