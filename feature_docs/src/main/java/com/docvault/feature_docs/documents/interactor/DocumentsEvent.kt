package com.docvault.feature_docs.documents.interactor

import com.docvault.domain.model.Document

sealed interface DocumentsEvent {
    data object NavigateToAddDocument : DocumentsEvent

    data object OpenDocumentViewer : DocumentsEvent

    data class RequestBiometricAuth(
        val document: Document
    ) : DocumentsEvent
}