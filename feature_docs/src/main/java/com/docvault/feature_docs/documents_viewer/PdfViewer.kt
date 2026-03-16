package com.docvault.feature_docs.documents_viewer

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import java.io.File

@Composable
fun PdfViewer(
    bytes: ByteArray?
) {

    if (bytes == null) return

    val file = File.createTempFile("temp", ".pdf")

    file.writeBytes(bytes)

    val parcel =
        ParcelFileDescriptor.open(
            file,
            ParcelFileDescriptor.MODE_READ_ONLY
        )

    val renderer = PdfRenderer(parcel)

    val page = renderer.openPage(0)

    val bitmap = Bitmap.createBitmap(
        page.width,
        page.height,
        Bitmap.Config.ARGB_8888
    )

    page.render(
        bitmap,
        null,
        null,
        PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
    )

    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = null,
        modifier = Modifier.fillMaxSize()
    )
}