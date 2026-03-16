package com.docvault.domain.repository

import com.docvault.domain.model.Document
import com.docvault.domain.model.DocumentAccess
import com.docvault.domain.model.DocumentType

interface DocumentRepository {
    suspend fun getDocuments(): List<Document>
    suspend fun getDocumentsByType(type: DocumentType): List<Document>
    suspend fun addDocument(document: Document)
    suspend fun deleteDocument(documentId: String)
    suspend fun registerAccess(documentId: String)
    suspend fun getDocumentAccessHistory(documentId: String): List<DocumentAccess>
    suspend fun saveDocumentFile(document: Document, fileBytes: ByteArray)
    suspend fun getDocumentFile(path: String): ByteArray
}