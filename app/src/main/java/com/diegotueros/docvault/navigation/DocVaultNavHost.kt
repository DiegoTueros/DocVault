package com.diegotueros.docvault.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.docvault.feature_docs.documents.DocumentsScreen
import com.docvault.feature_docs.documents.DocumentsViewModel
import com.docvault.feature_docs.documents_viewer.DocumentViewerScreen

@Composable
fun DocVaultNavHost(
    viewModel: DocumentsViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = DocVaultRoutes.DOCUMENTS
    ) {
        composable(DocVaultRoutes.DOCUMENTS) {

            DocumentsScreen(
                state = viewModel.state.collectAsState().value,
                event = viewModel.event,
                onIntent = viewModel::onIntent,
                onNavigateToViewer = {
                    navController.navigate(DocVaultRoutes.VIEWER)
                }
            )
        }

        composable(DocVaultRoutes.VIEWER) {
            DocumentViewerScreen(
                state = viewModel.viewerState.collectAsState().value
            )
        }
    }
}