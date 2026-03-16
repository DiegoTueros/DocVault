package com.docvault.feature_docs.documents.interactor

import com.docvault.domain.model.Document
import com.docvault.domain.model.DocumentType

sealed interface DocumentsIntent {

    data object LoadDocuments : DocumentsIntent

    data class FilterDocuments(
        val type: DocumentType?
    ) : DocumentsIntent

    data object AddDocumentClicked : DocumentsIntent

    data class SaveDocument(
        val name: String,
        val fileBytes: ByteArray,
        val type: DocumentType
    ) : DocumentsIntent

    data class OpenDocument(
        val document: Document
    ) : DocumentsIntent

    data class BiometricSuccess(
        val document: Document
    ) : DocumentsIntent
}