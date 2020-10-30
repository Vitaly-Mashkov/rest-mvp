package com.example.mvp.model

import com.google.gson.annotations.SerializedName


class Credits {
    @SerializedName("cast")
    lateinit var cast: List<Cast>
}
