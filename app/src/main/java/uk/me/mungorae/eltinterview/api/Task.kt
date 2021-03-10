package uk.me.mungorae.eltinterview.api

import com.google.gson.annotations.SerializedName

data class Task(val id: Int, val name: String, val description: String, val type: Type)

enum class Type {
    @SerializedName("general")
    GENERAL,
    @SerializedName("hydration")
    HYDRATION,
    @SerializedName("medication")
    MEDICATION,
    @SerializedName("nutrition")
    NUTRITION,
}