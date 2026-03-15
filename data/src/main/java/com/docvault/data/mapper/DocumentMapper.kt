package com.docvault.data.mapper

import com.docvault.data.local.entity.DocumentEntity
import com.docvault.domain.model.Document
import com.docvault.domain.model.DocumentType

fun DocumentEntity.toDomain(): Document {
    return Document(
        id = id,
        name = name,
        type = DocumentType.valueOf(type),
        encryptedPath = encryptedPath,
        createdAt = createdAt
    )
}

fun Document.toEntity(): DocumentEntity {
    return DocumentEntity(
        id = id,
        name = name,
        type = type.name,
        encryptedPath = encryptedPath,
        createdAt = createdAt
    )
}