package com.docvault.domain.usecase

import com.docvault.domain.repository.DocumentRepository

class GetDocumentFileUseCase(
    private val repository: DocumentRepository
) {
    suspend operator fun invoke(
        encryptedPath: String
    ): ByteArray {
        return repository.getDocumentFile(
            encryptedPath
        )
    }
}