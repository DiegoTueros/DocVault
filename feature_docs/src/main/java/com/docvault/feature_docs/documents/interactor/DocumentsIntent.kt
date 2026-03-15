package com.docvault.feature_docs.documents.interactor

import com.docvault.domain.model.DocumentType

sealed interface DocumentsIntent {
    data object LoadDocuments : DocumentsIntent
    data class FilterDocuments(
        val type: DocumentType?
    ) : DocumentsIntent
    data object AddDocumentClicked : DocumentsIntent
}