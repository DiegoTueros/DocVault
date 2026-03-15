package com.docvault.data.repository

import com.docvault.data.local.dao.DocumentAccessDao
import com.docvault.data.local.dao.DocumentDao
import com.docvault.data.mapper.toDomain
import com.docvault.data.mapper.toEntity
import com.docvault.domain.model.Document
import com.docvault.domain.model.DocumentAccess
import com.docvault.domain.model.DocumentType
import com.docvault.domain.repository.DocumentRepository

class DocumentRepositoryImpl(
    private val documentDao: DocumentDao,
    private val accessDao: DocumentAccessDao
) : DocumentRepository {

    override suspend fun getDocuments(): List<Document> {
        return documentDao.getDocuments().map { it.toDomain() }
    }

    override suspend fun getDocumentsByType(type: DocumentType): List<Document> {
        return documentDao.getDocumentsByType(type.name).map { it.toDomain() }
    }

    override suspend fun addDocument(document: Document) {
        documentDao.insertDocument(document.toEntity())
    }

    override suspend fun deleteDocument(documentId: String) {
        documentDao.deleteDocument(documentId)
    }

    override suspend fun registerAccess(documentId: String) {
        accessDao.insertAccess(
            com.docvault.data.local.entity.DocumentAccessEntity(
                documentId = documentId,
                accessedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun getDocumentAccessHistory(documentId: String): List<DocumentAccess> {
        return accessDao.getAccessHistory(documentId)
            .map {
                DocumentAccess(
                    documentId = it.documentId,
                    accessedAt = it.accessedAt
                )
            }
    }
}