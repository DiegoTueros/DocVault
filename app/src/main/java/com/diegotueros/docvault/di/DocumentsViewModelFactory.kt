package com.diegotueros.docvault.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.docvault.feature_docs.documents.DocumentsViewModel

class DocumentsViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val getDocuments = AppModule.provideGetDocumentsUseCase(context)

        val getDocumentsByType = AppModule.provideGetDocumentsByTypeUseCase(context)

        val saveDocument = AppModule.provideSaveDocumentUseCase(context)

        val getDocumentFile = AppModule.provideGetDocumentFileUseCase(context)

        val getDocumentAccessHistory = AppModule.provideGetDocumentAccessHistoryUseCase(context)

        val registerAccess = AppModule.provideRegisterAccessUseCase(context)

        return DocumentsViewModel(
            getDocuments,
            getDocumentsByType,
            saveDocument,
            getDocumentFile,
            registerAccess,
            getDocumentAccessHistory,
            context
        ) as T
    }
}