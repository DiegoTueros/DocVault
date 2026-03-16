package com.docvault.feature_docs.documents

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.core.content.ContextCompat
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
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.UUID

class DocumentsViewModel(
    private val getDocumentsUseCase: GetDocumentsUseCase,
    private val getDocumentsByTypeUseCase: GetDocumentsByTypeUseCase,
    private val saveDocumentUseCase: SaveDocumentUseCase,
    private val getDocumentFileUseCase: GetDocumentFileUseCase,
    private val applicationContext: Context
) : ViewModel() {

    private val _state = MutableStateFlow(DocumentsState())
    val state: StateFlow<DocumentsState> = _state

    private val _event = MutableSharedFlow<DocumentsEvent>()
    val event = _event

    private val _viewerState =
        MutableStateFlow(DocumentViewerState())

    val viewerState: StateFlow<DocumentViewerState> =
        _viewerState

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)

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

            is DocumentsIntent.BiometricSuccess -> {
                onBiometricSuccess(intent.document)
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

            _event.emit(
                DocumentsEvent.RequestBiometricAuth(
                    document
                )
            )
        }
    }

    fun onBiometricSuccess(
        document: Document
    ) {
        viewModelScope.launch {

            val bytes = getDocumentFileUseCase(document.encryptedPath)
            val location = getLocation()

            _viewerState.value = DocumentViewerState(
                bytes = bytes,
                type = document.type,
                location = location
            )

            _event.emit(
                DocumentsEvent.OpenDocumentViewer
            )
        }
    }

    suspend fun getLocation(): String {
        return suspendCancellableCoroutine { cont ->
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        val geocoder = Geocoder(applicationContext)
                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        val street = addresses?.firstOrNull()?.thoroughfare ?: "Unknown Street"
                        cont.resume(street, null)
                    } else {
                        cont.resume("Unknown Street", null)
                    }
                }.addOnFailureListener {
                    cont.resume("Unknown Street", null)
                }
            } else {
                // Si no hay permisos, devuelve "Unknown Street"
                cont.resume("Unknown Street", null)
            }
        }
    }
}