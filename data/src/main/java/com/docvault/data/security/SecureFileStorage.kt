package com.docvault.data.security

import android.content.Context
import java.io.File

class SecureFileStorage(
    private val context: Context,
    private val cryptoManager: CryptoManager
) {

    private val directory = File(context.filesDir, "secure_docs")

    init {
        if (!directory.exists()) {
            directory.mkdirs()
        }
    }

    fun saveFile(
        fileName: String,
        data: ByteArray
    ): String {
        val (encryptedData, iv) =
            cryptoManager.encrypt(data)

        val file = File(directory, fileName)

        file.outputStream().use { stream ->

            stream.write(iv.size)
            stream.write(iv)
            stream.write(encryptedData)

        }

        return file.absolutePath
    }

    fun readFile(path: String): ByteArray {
        val file = File(path)
        val bytes = file.readBytes()
        val ivSize = bytes[0].toInt()
        val iv = bytes.copyOfRange(
            1,
            1 + ivSize
        )
        val encryptedData =
            bytes.copyOfRange(
                1 + ivSize,
                bytes.size
            )

        return cryptoManager.decrypt(
            encryptedData,
            iv
        )
    }

    fun deleteFile(path: String) {
        val file = File(path)

        if (file.exists()) {
            file.delete()
        }
    }
}