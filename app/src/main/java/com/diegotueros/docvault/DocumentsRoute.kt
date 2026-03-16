package com.diegotueros.docvault

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.docvault.feature_docs.documents.DocumentsScreen
import com.docvault.feature_docs.documents.DocumentsViewModel

@Composable
fun DocumentsRoute(
    viewModel: DocumentsViewModel
) {
    val state by viewModel.state.collectAsState()

    DocumentsScreen(
        state = state,
        onIntent = { intent ->
            viewModel.onIntent(intent)
        }
    )
}