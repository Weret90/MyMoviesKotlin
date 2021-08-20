package com.umbrella.mymovieskotlin.model


import com.google.gson.annotations.SerializedName

data class TrailerList(
    @SerializedName("results")
    val trailers: List<Trailer>
)

data class Trailer(
    @SerializedName("key")
    val key: String,
    @SerializedName("name")
    val name: String
)
