package com.docvault.domain.model

data class DocumentAccess(
    val documentId: String,
    val accessedAt: Long
)