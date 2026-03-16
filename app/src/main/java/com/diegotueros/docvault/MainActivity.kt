package com.diegotueros.docvault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.diegotueros.docvault.di.DocumentsViewModelFactory
import com.docvault.feature_docs.documents.DocumentsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(
            this,
            DocumentsViewModelFactory(applicationContext)
        )[DocumentsViewModel::class.java]

        setContent {
            DocumentsRoute(viewModel)
        }
    }
}