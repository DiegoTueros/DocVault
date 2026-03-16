package com.docvault.domain.usecase

import com.docvault.domain.repository.DocumentRepository

class GetDocumentAccessHistoryUseCase(
    private val repository: DocumentRepository
) {
    suspend operator fun invoke(documentId: String) =
        repository.getDocumentAccessHistory(documentId)
}