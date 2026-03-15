package com.docvault.feature_docs.documents.interactor

sealed interface DocumentsEvent {
    data object NavigateToAddDocument : DocumentsEvent
}