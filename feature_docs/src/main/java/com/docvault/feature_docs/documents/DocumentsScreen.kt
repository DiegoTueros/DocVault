package com.docvault.feature_docs.documents

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.docvault.feature_docs.documents.interactor.DocumentsIntent
import com.docvault.feature_docs.documents.interactor.DocumentsState

@Composable
fun DocumentsScreen(
    state: DocumentsState,
    onIntent: (DocumentsIntent) -> Unit
) {

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->

        uri?.let {

            val bytes =
                context.contentResolver
                    .openInputStream(it)
                    ?.readBytes()

            bytes?.let { data ->

                val name = "document_${System.currentTimeMillis()}"

                onIntent(
                    DocumentsIntent.SaveDocument(
                        name = name,
                        fileBytes = data
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

                items(state.documents.size) {

                    Text(
                        text = state.documents[it].name
                    )
                }
            }
        }
    }
}