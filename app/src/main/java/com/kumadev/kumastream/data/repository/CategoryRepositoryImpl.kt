package com.kumadev.kumastream.data.repository

import com.kumadev.kumastream.data.local.dao.CategoryDao
import com.kumadev.kumastream.data.local.mapper.toDomain
import com.kumadev.kumastream.data.local.mapper.toEntity
import com.kumadev.kumastream.data.local.seed.DefaultCategories
import com.kumadev.kumastream.domain.model.Category
import com.kumadev.kumastream.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
) : CategoryRepository {

    override fun observeAll(): Flow<List<Category>> =
        categoryDao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun getById(id: String): Category? =
        categoryDao.getById(id)?.toDomain()

    override suspend fun upsert(category: Category) {
        // Preserve any stored description when overwriting an existing row.
        val description = categoryDao.getById(category.id)?.description
        categoryDao.upsert(category.toEntity(description = description))
    }

    override suspend fun delete(category: Category) =
        categoryDao.delete(category.toEntity())

    override suspend fun deleteById(id: String) =
        categoryDao.deleteById(id)

    override suspend fun seedDefaultsIfEmpty() {
        if (categoryDao.count() == 0) {
            categoryDao.upsertAll(DefaultCategories.ENTITIES)
        }
    }
}
