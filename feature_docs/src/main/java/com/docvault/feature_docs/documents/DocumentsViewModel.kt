package com.docvault.feature_docs.documents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.docvault.domain.usecase.GetDocumentsByTypeUseCase
import com.docvault.domain.usecase.GetDocumentsUseCase
import com.docvault.feature_docs.documents.interactor.DocumentsEvent
import com.docvault.feature_docs.documents.interactor.DocumentsIntent
import com.docvault.feature_docs.documents.interactor.DocumentsState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DocumentsViewModel(
    private val getDocumentsUseCase: GetDocumentsUseCase,
    private val getDocumentsByTypeUseCase: GetDocumentsByTypeUseCase
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
}