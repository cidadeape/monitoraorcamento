package org.cidadeape.monitoraorcamento.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.cidadeape.monitoraorcamento.common.LoadingState
import org.cidadeape.monitoraorcamento.common.Util
import org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho

@Composable
fun ProjetoAtividadeScreen(viewModel: ProjetoAtividadeViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp)
        ) {

            Text(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                text = viewModel.projetoAtividadeState.nome
            )

            Text(
                fontSize = 12.sp,
                fontStyle = FontStyle.Italic,
                text = "Código: ${viewModel.projetoAtividadeState.codigo}"
            )
        }

        Column (modifier = Modifier.padding(24.dp, 0.dp, 24.dp, 0.dp)) {
            ListaEmpenhos(
                viewModel.projetoAtividadeState
            )
        }
    }
}

@Composable
fun ListaEmpenhos(
    stateProjetoAtividade: ProjetoAtividadeViewModel.ProjetoAtividadeState
) {

    val listaEmpenhosState = stateProjetoAtividade.stateListaEmpenhos.collectAsState()
    val totalEmpenhosState = stateProjetoAtividade.stateTotalEmpenhado.collectAsState()

    when (val state = listaEmpenhosState.value) {
        is LoadingState.Failure -> Text(state.message)
        is LoadingState.Success -> {
            if(state.response.isEmpty()) {
                Text("Nenhum empenho realizado")
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    for (empenho in state.response) {
                        EmpenhoRow(empenho)
                    }
                }
            }
        }
        is LoadingState.Loading -> Text("Carregando")
        is LoadingState.NotStarted -> {}
    }

    when (val state = totalEmpenhosState.value) {
        is LoadingState.Failure -> Text(state.message)
        is LoadingState.Success -> {
            Text(
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 16.dp),
                textAlign = TextAlign.Center,
                text = "Total empenhado (2025): ${state.response}"
            )
        }
        else -> {}
    }
}

@Composable
fun EmpenhoRow(empenho: Empenho) {
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CellRow(empenho.datEmpenho?.substring(0, 10), fontWeight = FontWeight.Bold)
        CellRow(Util.formatToCurrency(empenho.valTotalEmpenhado), fontWeight = FontWeight.Bold)
    }
    Row (modifier = Modifier.fillMaxWidth().padding(0.dp, 0.dp, 0.dp, 6.dp)) {
        CellRow(empenho.txDescricaoElemento, fontSize = 12.sp)
    }
}

@Composable
fun CellRow(value: String?, fontSize: TextUnit = 14.sp, fontWeight: FontWeight = FontWeight.Normal) {
    Text(text = value?: "null", fontSize = fontSize, fontWeight = fontWeight)
}
