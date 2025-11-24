package org.cidadeape.monitoraorcamento.presentation.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.todayIn
import org.cidadeape.monitoraorcamento.common.AppButtonColors
import org.cidadeape.monitoraorcamento.common.DownloadCsv
import org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho

@Composable
fun ButtonBaixarEmpenhos(
    listaEmpenhos: List<Empenho>,
    nomeArquivo: String = "empenhos"
) {
    var downloadCsv by remember { mutableStateOf(false) }
    Button(
        colors = AppButtonColors.GreenButton,
        modifier = Modifier.padding(16.dp),
        onClick = {
            downloadCsv = true
        }
    ) {
        Text("Baixar lista de empenhos")
    }
    if (downloadCsv) {
        val today = Clock.System.todayIn(kotlinx.datetime.TimeZone.currentSystemDefault())
        val date = "${today.year}${today.monthNumber+1}${today.dayOfMonth}"
        DownloadCsv("$date $nomeArquivo", listaEmpenhos)
        downloadCsv = false
    }
}