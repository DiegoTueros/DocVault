package com.docvault.feature_docs.documents.interactor

import com.docvault.domain.model.DocumentType

sealed interface DocumentsEvent {
    data object NavigateToAddDocument : DocumentsEvent

    data class OpenDocumentViewer(
        val bytes: ByteArray,
        val type: DocumentType
    ) : DocumentsEvent
}