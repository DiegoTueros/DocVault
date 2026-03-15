package com.docvault.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.docvault.data.local.dao.DocumentAccessDao
import com.docvault.data.local.dao.DocumentDao
import com.docvault.data.local.entity.DocumentAccessEntity
import com.docvault.data.local.entity.DocumentEntity

@Database(
    entities = [
        DocumentEntity::class,
        DocumentAccessEntity::class
    ],
    version = 1
)

abstract class DocVaultDatabase : RoomDatabase() {

    abstract fun documentDao(): DocumentDao

    abstract fun documentAccessDao(): DocumentAccessDao
}