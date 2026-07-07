package com.kumadev.kumastream.domain.repository

import com.kumadev.kumastream.domain.model.Category
import kotlinx.coroutines.flow.Flow

/** Domain-facing contract for category storage. */
interface CategoryRepository {

    /** All categories, name-ascending, as a reactive stream. */
    fun observeAll(): Flow<List<Category>>

    suspend fun getById(id: String): Category?

    suspend fun upsert(category: Category)

    suspend fun delete(category: Category)

    suspend fun deleteById(id: String)

    /** Seed the predefined set only if the table is empty (idempotent). */
    suspend fun seedDefaultsIfEmpty()
}
