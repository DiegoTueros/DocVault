package com.docvault.data.local.dao

import androidx.room.*
import com.docvault.data.local.entity.DocumentAccessEntity

@Dao
interface DocumentAccessDao {
    @Insert
    suspend fun insertAccess(access: DocumentAccessEntity)

    @Query("SELECT * FROM document_access WHERE documentId = :documentId ORDER BY accessedAt DESC")
    suspend fun getAccessHistory(documentId: String): List<DocumentAccessEntity>
}