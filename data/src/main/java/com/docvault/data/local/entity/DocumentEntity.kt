package com.docvault.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "documents")
data class DocumentEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val type: String,
    val encryptedPath: String,
    val createdAt: Long
)