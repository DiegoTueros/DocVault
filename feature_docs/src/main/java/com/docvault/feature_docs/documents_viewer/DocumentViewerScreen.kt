package com.docvault.feature_docs.documents_viewer

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import com.docvault.domain.model.DocumentType
import com.docvault.feature_docs.documents_viewer.interactor.DocumentViewerState

@Composable
fun DocumentViewerScreen(
    state: DocumentViewerState
) {

    when (state.type) {

        DocumentType.IMAGE -> {

            state.bytes?.let {

                val bitmap =
                    BitmapFactory.decodeByteArray(
                        it,
                        0,
                        it.size
                    )

                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        DocumentType.PDF -> {

            PdfViewer(
                bytes = state.bytes
            )

        }

        else -> {}

    }

}