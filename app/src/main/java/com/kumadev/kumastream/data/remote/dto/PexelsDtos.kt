package com.kumadev.kumastream.data.remote.dto

import com.google.gson.annotations.SerializedName

/** Top-level response of Pexels `GET /v1/search`. */
data class PexelsSearchResponse(
    @SerializedName("page") val page: Int = 0,
    @SerializedName("per_page") val perPage: Int = 0,
    @SerializedName("total_results") val totalResults: Int = 0,
    @SerializedName("photos") val photos: List<PexelsPhoto> = emptyList(),
)

/** A single Pexels photo. */
data class PexelsPhoto(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("photographer") val photographer: String? = null,
    @SerializedName("alt") val alt: String? = null,
    @SerializedName("src") val src: PexelsSrc? = null,
)

/** The set of pre-rendered sizes Pexels returns for each photo. */
data class PexelsSrc(
    @SerializedName("original") val original: String? = null,
    @SerializedName("large2x") val large2x: String? = null,
    @SerializedName("large") val large: String? = null,
    @SerializedName("medium") val medium: String? = null,
    @SerializedName("small") val small: String? = null,
    @SerializedName("portrait") val portrait: String? = null,
    @SerializedName("landscape") val landscape: String? = null,
    @SerializedName("tiny") val tiny: String? = null,
)
