package com.example.mvp.model

import com.google.gson.annotations.SerializedName


class Cast(@field:SerializedName("cast_id") var castId: Int, @field:SerializedName("character") var character: String, @field:SerializedName("name") var name: String, @field:SerializedName("profile_path") var profilePath: String) {

    override fun toString(): String {
        return "Cast{" +
                "castId=" + castId +
                ", character='" + character + '\'' +
                ", name='" + name + '\'' +
                ", profilePath='" + profilePath + '\'' +
                '}'
    }
}