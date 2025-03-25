package org.cidadeape.monitoraorcamento.common

import androidx.compose.runtime.Composable
import kotlinx.browser.window

@Composable
actual fun ShowMessage(message: String) {
    window.alert(message)
}

@Composable
actual fun OpenInNewWindow(url: String) {
    window.open(url, "_blank")
}