package com.diegotueros.docvault

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.diegotueros.docvault.di.DocumentsViewModelFactory
import com.diegotueros.docvault.navigation.DocVaultNavHost
import com.docvault.feature_docs.documents.DocumentsViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(
            this,
            DocumentsViewModelFactory(applicationContext)
        )[DocumentsViewModel::class.java]

        window.setFlags(-
            android.view.WindowManager.LayoutParams.FLAG_SECURE,
            android.view.WindowManager.LayoutParams.FLAG_SECURE
        )

        setContent {
            DocVaultNavHost(
                viewModel = viewModel
            )
        }
    }
}