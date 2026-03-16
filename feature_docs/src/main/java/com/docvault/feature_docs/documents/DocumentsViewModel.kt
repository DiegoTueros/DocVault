package com.docvault.feature_docs.documents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.docvault.domain.model.Document
import com.docvault.domain.model.DocumentType
import com.docvault.domain.usecase.GetDocumentsByTypeUseCase
import com.docvault.domain.usecase.GetDocumentsUseCase
import com.docvault.domain.usecase.SaveDocumentUseCase
import com.docvault.feature_docs.documents.interactor.DocumentsEvent
import com.docvault.feature_docs.documents.interactor.DocumentsIntent
import com.docvault.feature_docs.documents.interactor.DocumentsState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class DocumentsViewModel(
    private val getDocumentsUseCase: GetDocumentsUseCase,
    private val getDocumentsByTypeUseCase: GetDocumentsByTypeUseCase,
    private val saveDocumentUseCase: SaveDocumentUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DocumentsState())
    val state: StateFlow<DocumentsState> = _state

    private val _event = MutableSharedFlow<DocumentsEvent>()
    val event = _event

    fun onIntent(intent: DocumentsIntent) {

        when (intent) {

            DocumentsIntent.LoadDocuments -> {
                loadDocuments()
            }

            is DocumentsIntent.FilterDocuments -> {
                filterDocuments(intent.type)
            }

            DocumentsIntent.AddDocumentClicked -> {
                navigateToAddDocument()
            }

            is DocumentsIntent.SaveDocument -> {
                saveDocument(intent.name, intent.fileBytes)
            }
        }
    }

    private fun loadDocuments() {
        viewModelScope.launch {

            _state.value = _state.value.copy(
                isLoading = true
            )

            val documents = getDocumentsUseCase()

            _state.value = _state.value.copy(
                documents = documents,
                isLoading = false
            )
        }
    }

    private fun filterDocuments(type: com.docvault.domain.model.DocumentType?) {
        _state.value = _state.value.copy(
            selectedFilter = type
        )
    }

    private fun navigateToAddDocument() {
        kotlinx.coroutines.GlobalScope.launch {
            _event.emit(DocumentsEvent.NavigateToAddDocument)
        }
    }

    fun saveDocument(
        name: String,
        fileBytes: ByteArray
    ) {

        viewModelScope.launch {

            val document = Document(
                id = UUID.randomUUID().toString(),
                name = name,
                type = if (name.endsWith("pdf")) DocumentType.PDF else DocumentType.IMAGE,
                encryptedPath = "",
                createdAt = System.currentTimeMillis()
            )

            saveDocumentUseCase(document, fileBytes)

            loadDocuments()
        }
    }
}