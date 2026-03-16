package com.docvault.domain.usecase

import com.docvault.domain.model.Document
import com.docvault.domain.repository.DocumentRepository

class SaveDocumentUseCase(
    private val repository: DocumentRepository
) {
    suspend operator fun invoke(
        document: Document,
        fileBytes: ByteArray
    ) {
        repository.saveDocumentFile(document, fileBytes)
    }
}