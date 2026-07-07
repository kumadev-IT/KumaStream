package com.kumadev.kumastream.domain.model

/**
 * A single image from the image-search provider, in provider-agnostic form.
 * [thumbnailUrl] feeds the search grid; [fullUrl] is what gets cached onto an
 * [Event.imageUrl] when the user picks it. Attribution fields support the
 * provider's licence requirements (Pexels asks for photographer credit).
 */
data class ImageResult(
    val id: String,
    val thumbnailUrl: String,
    val fullUrl: String,
    val photographer: String,
    val alt: String,
)
