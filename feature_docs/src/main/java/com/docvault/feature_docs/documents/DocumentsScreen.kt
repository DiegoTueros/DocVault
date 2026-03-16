package com.docvault.feature_docs.documents

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.docvault.domain.model.DocumentType
import com.docvault.feature_docs.componets.DocumentItem
import com.docvault.feature_docs.documents.interactor.DocumentsEvent
import com.docvault.feature_docs.documents.interactor.DocumentsIntent
import com.docvault.feature_docs.documents.interactor.DocumentsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DocumentsScreen(
    state: DocumentsState,
    event: kotlinx.coroutines.flow.SharedFlow<DocumentsEvent>,
    onIntent: (DocumentsIntent) -> Unit,
    onNavigateToViewer: () -> Unit
) {

    val context = LocalContext.current

    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    LaunchedEffect(Unit) {
        if (locationPermissionState.status.isGranted) {
            locationPermissionState.launchPermissionRequest()
        }
    }

    LaunchedEffect(Unit) {
        onIntent(DocumentsIntent.LoadDocuments)
    }

    LaunchedEffect(Unit) {
        event.collect { event ->
            when (event) {

                is DocumentsEvent.OpenDocumentViewer -> {
                    onNavigateToViewer()
                }

                is DocumentsEvent.RequestBiometricAuth -> {

                    val executor =
                        ContextCompat.getMainExecutor(context)

                    val activity = context as? FragmentActivity
                        ?: return@collect

                    val biometricPrompt =
                        BiometricPrompt(
                            activity,
                            executor,
                            object : BiometricPrompt.AuthenticationCallback() {

                                override fun onAuthenticationSucceeded(
                                    result: BiometricPrompt.AuthenticationResult
                                ) {

                                    onIntent(
                                        DocumentsIntent.BiometricSuccess(
                                            event.document
                                        )
                                    )
                                }
                            }
                        )

                    val promptInfo =
                        BiometricPrompt.PromptInfo.Builder()
                            .setTitle("Authenticate")
                            .setSubtitle("Confirm to open document")
                            .setNegativeButtonText("Cancel")
                            .build()

                    biometricPrompt.authenticate(promptInfo)
                }

                else -> {}
            }
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->

        uri?.let {

            val bytes =
                context.contentResolver
                    .openInputStream(it)
                    ?.readBytes()

            bytes?.let { data ->

                val mimeType = context.contentResolver.getType(it)

                val type =
                    if (mimeType == "application/pdf")
                        DocumentType.PDF
                    else
                        DocumentType.IMAGE

                val name =
                    if (type == DocumentType.PDF)
                        "document_${System.currentTimeMillis()}.pdf"
                    else
                        "document_${System.currentTimeMillis()}.jpg"

                onIntent(
                    DocumentsIntent.SaveDocument(
                        name = name,
                        fileBytes = data,
                        type = type
                    )
                )
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    launcher.launch("*/*")
                }
            ) {
                Text("+")
            }
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Documents",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {

                items(state.documents.size) { index ->

                    DocumentItem(
                        document = state.documents[index],
                        onClick = {
                            onIntent(
                                DocumentsIntent.OpenDocument(
                                    state.documents[index]
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}