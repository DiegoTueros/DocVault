package com.docvault.domain.usecase

import com.docvault.domain.model.Document
import com.docvault.domain.repository.DocumentRepository

class GetDocumentsUseCase(
    private val repository: DocumentRepository
) {
    suspend operator fun invoke(): List<Document> {
        return repository.getDocuments()
    }
}