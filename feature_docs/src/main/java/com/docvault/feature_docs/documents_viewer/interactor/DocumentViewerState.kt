package com.docvault.feature_docs.documents_viewer.interactor

import com.docvault.domain.model.DocumentType

data class DocumentViewerState(
    val bytes: ByteArray? = null,
    val type: DocumentType? = null
)