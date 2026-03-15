package com.docvault.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "document_access")
data class DocumentAccessEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val documentId: String,
    val accessedAt: Long
)