package com.docvault.domain.usecase

import com.docvault.domain.model.Document
import com.docvault.domain.model.DocumentType
import com.docvault.domain.repository.DocumentRepository

class GetDocumentsByTypeUseCase(
    private val repository: DocumentRepository
) {
    suspend operator fun invoke(type: DocumentType): List<Document> {
        return repository.getDocumentsByType(type)
    }
}