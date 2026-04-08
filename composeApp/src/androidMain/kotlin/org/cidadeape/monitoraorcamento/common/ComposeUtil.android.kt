package org.cidadeape.monitoraorcamento.common

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho

@Composable
actual fun ShowMessage(message: String) {
    Toast.makeText(LocalContext.current, message, Toast.LENGTH_SHORT).show()
}

@Composable
actual fun OpenInNewWindow(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    LocalContext.current.startActivity(intent)
}

@Composable
actual fun DownloadCsv(
    fileName: String,
    listaEmpenhos: List<Empenho>
) {
}

actual object Logger {

    actual fun e(tag: String, message: String, throwable: Throwable?) {
        if (throwable != null) {
            Log.e(tag, message, throwable)
        } else {
            Log.e(tag, message)
        }
    }

    actual fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    actual fun i(tag: String, message: String) {
        Log.i(tag, message)
    }
}