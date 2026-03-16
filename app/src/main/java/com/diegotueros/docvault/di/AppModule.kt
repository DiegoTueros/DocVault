package com.diegotueros.docvault.di

import android.content.Context
import androidx.room.Room
import com.docvault.data.local.database.DocVaultDatabase
import com.docvault.data.repository.DocumentRepositoryImpl
import com.docvault.data.security.CryptoManager
import com.docvault.data.security.SecureFileStorage
import com.docvault.domain.repository.DocumentRepository
import com.docvault.domain.usecase.GetDocumentFileUseCase
import com.docvault.domain.usecase.GetDocumentsByTypeUseCase
import com.docvault.domain.usecase.GetDocumentsUseCase
import com.docvault.domain.usecase.SaveDocumentUseCase

object AppModule {

    private var database: DocVaultDatabase? = null

    fun provideDatabase(context: Context): DocVaultDatabase {
        if (database == null) {
            database = Room.databaseBuilder(
                context,
                DocVaultDatabase::class.java,
                "docvault_db"
            ).build()
        }

        return database!!
    }

    fun provideRepository(context: Context): DocumentRepository {

        val db = provideDatabase(context)

        val secureFileStorage = provideSecureFileStorage(context)

        return DocumentRepositoryImpl(
            db.documentDao(),
            db.documentAccessDao(),
            secureFileStorage
        )
    }

    fun provideSecureFileStorage(context: Context): SecureFileStorage {

        val cryptoManager = provideCryptoManager()

        return SecureFileStorage(
            context = context,
            cryptoManager = cryptoManager
        )
    }

    fun provideGetDocumentsUseCase(context: Context): GetDocumentsUseCase {
        return GetDocumentsUseCase(
            provideRepository(context)
        )
    }

    fun provideGetDocumentsByTypeUseCase(context: Context): GetDocumentsByTypeUseCase {
        return GetDocumentsByTypeUseCase(
            provideRepository(context)
        )
    }

    fun provideCryptoManager(): CryptoManager {
        return CryptoManager()
    }

    fun provideSaveDocumentUseCase(
        context: Context
    ): SaveDocumentUseCase {
        val repository = provideRepository(context)

        return SaveDocumentUseCase(repository)
    }

    fun provideGetDocumentFileUseCase(
        context: Context
    ): GetDocumentFileUseCase {
        val repository = provideRepository(context)

        return GetDocumentFileUseCase(repository)
    }
}