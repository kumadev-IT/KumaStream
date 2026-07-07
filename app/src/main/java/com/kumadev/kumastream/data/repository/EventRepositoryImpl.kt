package com.kumadev.kumastream.data.repository

import com.kumadev.kumastream.data.local.dao.CategoryDao
import com.kumadev.kumastream.data.local.dao.EventDao
import com.kumadev.kumastream.data.local.entity.CategoryEntity
import com.kumadev.kumastream.data.local.entity.EventEntity
import com.kumadev.kumastream.data.local.mapper.toDomain
import com.kumadev.kumastream.data.local.mapper.toEntity
import com.kumadev.kumastream.data.local.mapper.toEpochMillis
import com.kumadev.kumastream.domain.model.Event
import com.kumadev.kumastream.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepositoryImpl @Inject constructor(
    private val eventDao: EventDao,
    private val categoryDao: CategoryDao,
) : EventRepository {

    override fun observeAll(): Flow<List<Event>> =
        eventDao.observeAll().joinCategories()

    override fun observeUpcoming(now: LocalDateTime): Flow<List<Event>> =
        eventDao.observeUpcoming(now.toEpochMillis()).joinCategories()

    override fun observeArchived(now: LocalDateTime): Flow<List<Event>> =
        eventDao.observeArchived(now.toEpochMillis()).joinCategories()

    override fun observeByCategory(categoryId: String): Flow<List<Event>> =
        eventDao.observeByCategory(categoryId).joinCategories()

    override fun observeById(id: String): Flow<Event?> =
        combine(eventDao.observeById(id), categoryDao.observeAll()) { event, categories ->
            event?.resolve(categories.associateBy { it.id })
        }

    override suspend fun getById(id: String): Event? {
        val entity = eventDao.getById(id) ?: return null
        val category = categoryDao.getById(entity.categoryId) ?: return null
        return entity.toDomain(category.toDomain())
    }

    override suspend fun upsert(event: Event) {
        val now = System.currentTimeMillis()
        val existingCreatedAt = eventDao.getById(event.id)?.createdAt
        eventDao.upsert(
            event.toEntity(
                createdAt = existingCreatedAt ?: now,
                updatedAt = now,
            ),
        )
    }

    override suspend fun setCompleted(id: String, completed: Boolean) =
        eventDao.setCompleted(id, completed, System.currentTimeMillis())

    override suspend fun delete(event: Event) =
        eventDao.delete(event.toEntity())

    override suspend fun deleteById(id: String) =
        eventDao.deleteById(id)

    // --- helpers ------------------------------------------------------------

    /**
     * Joins each event to its category, streaming both tables so the list
     * updates when either changes. Events whose category is missing (shouldn't
     * happen — FK is RESTRICT) are dropped rather than crashing the stream.
     */
    private fun Flow<List<EventEntity>>.joinCategories(): Flow<List<Event>> =
        combine(categoryDao.observeAll()) { events, categories ->
            val byId = categories.associateBy { it.id }
            events.mapNotNull { it.resolve(byId) }
        }

    private fun EventEntity.resolve(categoriesById: Map<String, CategoryEntity>): Event? =
        categoriesById[categoryId]?.let { toDomain(it.toDomain()) }
}
