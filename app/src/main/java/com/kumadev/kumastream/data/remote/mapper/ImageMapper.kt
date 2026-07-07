package com.kumadev.kumastream.data.remote.mapper

import com.kumadev.kumastream.data.remote.dto.PexelsPhoto
import com.kumadev.kumastream.domain.model.ImageResult

/**
 * Pexels [PexelsPhoto] → domain [ImageResult]. Picks `medium` for the grid tile
 * and `large` for the saved image, falling back through the available sizes so a
 * photo with an unusual `src` set still maps. Photos with no usable url are
 * dropped by the caller (this returns null).
 */
fun PexelsPhoto.toDomainOrNull(): ImageResult? {
    val src = src ?: return null
    val thumb = src.medium ?: src.small ?: src.tiny ?: src.large ?: src.original ?: return null
    val full = src.large ?: src.large2x ?: src.original ?: src.medium ?: thumb
    return ImageResult(
        id = id.toString(),
        thumbnailUrl = thumb,
        fullUrl = full,
        photographer = photographer.orEmpty(),
        alt = alt.orEmpty(),
    )
}
