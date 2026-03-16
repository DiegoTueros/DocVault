package com.docvault.domain.usecase

import com.docvault.domain.repository.DocumentRepository

class GetDocumentFileUseCase(
    private val repository: DocumentRepository
) {
    suspend operator fun invoke(
        path: String
    ): ByteArray {
        return repository.getDocumentFile(path)
    }
}