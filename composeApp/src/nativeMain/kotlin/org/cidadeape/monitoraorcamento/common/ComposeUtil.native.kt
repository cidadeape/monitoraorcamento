package org.cidadeape.monitoraorcamento.common

import androidx.compose.runtime.Composable
import org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho

@Composable
actual fun ShowMessage(message: String) {
}

@Composable
actual fun OpenInNewWindow(url: String) {
}

@Composable
actual fun DownloadCsv(
    fileName: String,
    listaEmpenhos: List<Empenho>
) {
}