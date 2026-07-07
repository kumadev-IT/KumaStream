package com.kumadev.kumastream.domain.repository

import com.kumadev.kumastream.domain.model.ImageResult

/**
 * Provider-agnostic image search (design §5.8). The concrete implementation
 * (currently Pexels) lives in the data layer behind this interface so the
 * provider can be swapped without touching the UI. Implementations throw on
 * network/HTTP failure; callers decide how to degrade (design: dismiss and save
 * without an image).
 */
interface ImageRepository {

    /**
     * Searches images for [query]. [page] is 1-based. Returns an empty list when
     * there are no matches.
     */
    suspend fun searchImages(query: String, page: Int = 1): List<ImageResult>
}
