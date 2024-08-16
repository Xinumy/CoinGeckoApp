package com.kurbatov.coingeckoapp.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ImageInfo(
    @SerializedName("large")
    @Expose
    val large: String?
)
