package com.docvault.domain.model

data class Document(
    val id: String,
    val name: String,
    val type: DocumentType,
    val encryptedPath: String,
    val createdAt: Long
)