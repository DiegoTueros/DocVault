package com.docvault.data.repository

import com.docvault.data.local.dao.DocumentAccessDao
import com.docvault.data.local.dao.DocumentDao
import com.docvault.data.mapper.toDomain
import com.docvault.data.mapper.toEntity
import com.docvault.data.security.SecureFileStorage
import com.docvault.domain.model.Document
import com.docvault.domain.model.DocumentAccess
import com.docvault.domain.model.DocumentType
import com.docvault.domain.repository.DocumentRepository

class DocumentRepositoryImpl(
    private val documentDao: DocumentDao,
    private val accessDao: DocumentAccessDao,
    private val secureFileStorage: SecureFileStorage
) : DocumentRepository {

    override suspend fun getDocuments(): List<Document> {
        return documentDao
            .getDocuments()
            .map { it.toDomain() }
    }

    override suspend fun getDocumentsByType(type: DocumentType): List<Document> {
        return documentDao
            .getDocumentsByType(type.name)
            .map { it.toDomain() }
    }

    override suspend fun addDocument(document: Document) {
        documentDao.insertDocument(document.toEntity())
    }

    override suspend fun deleteDocument(documentId: String) {

        val entity = documentDao.getDocumentById(documentId)
            ?: return

        secureFileStorage.deleteFile(entity.encryptedPath)

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
        return accessDao
            .getAccessHistory(documentId)
            .map {
                DocumentAccess(
                    documentId = it.documentId,
                    accessedAt = it.accessedAt
                )
            }
    }

    override suspend fun saveDocumentFile(
        document: Document,
        fileBytes: ByteArray
    ) {

        val fileName = "${document.id}.enc"

        val encryptedPath = secureFileStorage.saveFile(
            fileName = fileName,
            data = fileBytes
        )

        val entity = document.copy(
            encryptedPath = encryptedPath
        )

        documentDao.insertDocument(
            entity.toEntity()
        )
    }
}