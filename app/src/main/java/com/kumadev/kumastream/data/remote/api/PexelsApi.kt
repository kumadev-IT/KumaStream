package com.kumadev.kumastream.data.remote.api

import com.kumadev.kumastream.data.remote.dto.PexelsSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Pexels REST endpoints. The API key is attached by an OkHttp interceptor
 * (see `di/NetworkModule`), so it isn't a parameter here.
 */
interface PexelsApi {

    @GET("v1/search")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = DEFAULT_PER_PAGE,
    ): PexelsSearchResponse

    companion object {
        const val BASE_URL = "https://api.pexels.com/"
        const val DEFAULT_PER_PAGE = 30
    }
}
