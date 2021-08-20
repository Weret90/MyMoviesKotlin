package com.umbrella.mymovieskotlin.model


import com.google.gson.annotations.SerializedName

data class ReviewList(
    @SerializedName("results")
    val reviews: List<Review>
)

data class Review(
    @SerializedName("author")
    val author: String,
    @SerializedName("content")
    val content: String
)