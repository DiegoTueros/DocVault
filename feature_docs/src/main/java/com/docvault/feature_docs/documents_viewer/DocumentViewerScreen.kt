package com.docvault.feature_docs.documents_viewer

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
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

                state.bytes?.let { data ->
                    val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                    var scale = remember { mutableStateOf(1f) }
                    var offsetX = remember { mutableStateOf(0f) }
                    var offsetY = remember { mutableStateOf(0f) }

                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer(
                                scaleX = scale.value,
                                scaleY = scale.value,
                                translationX = offsetX.value,
                                translationY = offsetY.value
                            )
                            .pointerInput(Unit) {
                                detectTransformGestures { _, pan, zoom, _ ->
                                    scale.value *= zoom
                                    offsetX.value += pan.x
                                    offsetY.value += pan.y
                                }
                            }
                    )
                }
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