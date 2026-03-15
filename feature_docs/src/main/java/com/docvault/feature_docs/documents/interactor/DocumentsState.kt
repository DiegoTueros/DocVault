package com.docvault.feature_docs.documents.interactor

import com.docvault.domain.model.Document
import com.docvault.domain.model.DocumentType

data class DocumentsState(
    val documents: List<Document> = emptyList(),
    val selectedFilter: DocumentType? = null,
    val isLoading: Boolean = false
)