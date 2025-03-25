package org.cidadeape.monitoraorcamento.common

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun ShowMessage(message: String) {
    Toast.makeText(LocalContext.current, message, Toast.LENGTH_SHORT).show()
}

@Composable
actual fun OpenInNewWindow(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    LocalContext.current.startActivity(intent)
}