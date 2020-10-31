package com.example.mvp.model

import com.google.gson.annotations.SerializedName


data class Cast(
        @SerializedName("cast_id") var castId: Int,
        @SerializedName("character") var character: String,
        @SerializedName("name") var name: String,
        @SerializedName("profile_path") var profilePath: String
)