package com.docvault.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.docvault.data.local.entity.DocumentEntity

@Dao
interface DocumentDao {
    @Query("SELECT * FROM documents")
    suspend fun getDocuments(): List<DocumentEntity>

    @Query("SELECT * FROM documents WHERE type = :type")
    suspend fun getDocumentsByType(type: String): List<DocumentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(document: DocumentEntity)

    @Query("DELETE FROM documents WHERE id = :documentId")
    suspend fun deleteDocument(documentId: String)

    @Query("SELECT * FROM documents WHERE id = :documentId LIMIT 1")
    suspend fun getDocumentById(documentId: String): DocumentEntity?
}