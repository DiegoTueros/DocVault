package com.docvault.feature_docs.documents_viewer

import android.graphics.Bitmap
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
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

@Composable
fun DocumentViewerScreen(
    state: DocumentViewerState
) {

    when (state.type) {

        DocumentType.IMAGE -> {

            state.bytes?.let { data ->
                val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                val canvas = Canvas(mutableBitmap)
                val paint = Paint().apply {
                    color = Color.RED
                    alpha = 80 // semi-transparente
                    textSize = 40f
                    isAntiAlias = true
                }
                val watermarkText = state.location ?: "Unknown Street"
                canvas.drawText(watermarkText, 20f, 50f, paint)

                var scale = remember { mutableStateOf(1f) }
                var offsetX = remember { mutableStateOf(0f) }
                var offsetY = remember { mutableStateOf(0f) }

                Image(
                    bitmap = mutableBitmap.asImageBitmap(),
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

        DocumentType.PDF -> {

            PdfViewer(
                bytes = state.bytes
            )

        }

        else -> {}

    }

}