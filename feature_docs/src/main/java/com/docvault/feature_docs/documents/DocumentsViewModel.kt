package com.docvault.feature_docs.documents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.docvault.domain.model.Document
import com.docvault.domain.model.DocumentType
import com.docvault.domain.usecase.GetDocumentFileUseCase
import com.docvault.domain.usecase.GetDocumentsByTypeUseCase
import com.docvault.domain.usecase.GetDocumentsUseCase
import com.docvault.domain.usecase.SaveDocumentUseCase
import com.docvault.feature_docs.documents.interactor.DocumentsEvent
import com.docvault.feature_docs.documents.interactor.DocumentsIntent
import com.docvault.feature_docs.documents.interactor.DocumentsState
import com.docvault.feature_docs.documents_viewer.interactor.DocumentViewerState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class DocumentsViewModel(
    private val getDocumentsUseCase: GetDocumentsUseCase,
    private val getDocumentsByTypeUseCase: GetDocumentsByTypeUseCase,
    private val saveDocumentUseCase: SaveDocumentUseCase,
    private val getDocumentFileUseCase: GetDocumentFileUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DocumentsState())
    val state: StateFlow<DocumentsState> = _state

    private val _event = MutableSharedFlow<DocumentsEvent>()
    val event = _event

    private val _viewerState =
        MutableStateFlow(DocumentViewerState())

    val viewerState: StateFlow<DocumentViewerState> =
        _viewerState

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
                saveDocument(intent.name, intent.fileBytes, intent.type)
            }

            is DocumentsIntent.OpenDocument -> {
                openDocument(intent.document)
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

    private fun filterDocuments(type: DocumentType?) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                selectedFilter = type,
                isLoading = true
            )

            val documents = if (type == null) {
                getDocumentsUseCase()
            } else {
                getDocumentsByTypeUseCase(type)
            }

            _state.value = _state.value.copy(
                documents = documents,
                isLoading = false
            )
        }
    }

    private fun navigateToAddDocument() {
        viewModelScope.launch {
            _event.emit(DocumentsEvent.NavigateToAddDocument)
        }
    }

    private fun saveDocument(
        name: String,
        fileBytes: ByteArray,
        type: DocumentType
    ) {
        viewModelScope.launch {
            val document = Document(
                id = UUID.randomUUID().toString(),
                name = name,
                type = type,
                encryptedPath = "",
                createdAt = System.currentTimeMillis()
            )

            saveDocumentUseCase(document, fileBytes)

            loadDocuments()
        }
    }

    private fun openDocument(
        document: Document
    ) {
        viewModelScope.launch {

            val bytes =
                getDocumentFileUseCase(
                    document.encryptedPath
                )

            _viewerState.value =
                DocumentViewerState(
                    bytes = bytes,
                    type = document.type
                )

            _event.emit(
                DocumentsEvent.OpenDocumentViewer
            )
        }
    }
}