package com.docvault.domain.usecase

import com.docvault.domain.repository.DocumentRepository

class RegisterAccessUseCase(
    private val repository: DocumentRepository
) {
    suspend operator fun invoke(documentId: String) =
        repository.registerAccess(documentId)
}