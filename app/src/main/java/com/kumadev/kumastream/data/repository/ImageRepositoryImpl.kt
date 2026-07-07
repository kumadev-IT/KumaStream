package com.kumadev.kumastream.data.repository

import com.kumadev.kumastream.data.remote.api.PexelsApi
import com.kumadev.kumastream.data.remote.mapper.toDomainOrNull
import com.kumadev.kumastream.domain.model.ImageResult
import com.kumadev.kumastream.domain.repository.ImageRepository
import javax.inject.Inject
import javax.inject.Singleton

/** Pexels-backed [ImageRepository]. Maps DTOs to domain and drops unusable rows. */
@Singleton
class ImageRepositoryImpl @Inject constructor(
    private val api: PexelsApi,
) : ImageRepository {

    override suspend fun searchImages(query: String, page: Int): List<ImageResult> {
        if (query.isBlank()) return emptyList()
        return api.searchPhotos(query = query.trim(), page = page)
            .photos
            .mapNotNull { it.toDomainOrNull() }
    }
}
