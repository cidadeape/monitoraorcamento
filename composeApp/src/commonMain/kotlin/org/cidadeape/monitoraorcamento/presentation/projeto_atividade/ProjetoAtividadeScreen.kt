package org.cidadeape.monitoraorcamento.presentation.projeto_atividade

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
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
import org.cidadeape.monitoraorcamento.common.TextTitle
import org.cidadeape.monitoraorcamento.common.Util
import org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho
import org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetoAtividade
import org.cidadeape.monitoraorcamento.presentation.AppViewModel

@Composable
fun ProjetoAtividadeScreen(
    appViewModel: AppViewModel,
    viewModel: ProjetoAtividadeViewModel,
    projetoAtividade: ProjetoAtividade
) {
    viewModel.load(projetoAtividade)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp)
        ) {

            TextTitle(text = viewModel.projetoAtividadeState.nome.value ?: "-")

            Text(
                fontSize = 12.sp,
                fontStyle = FontStyle.Italic,
                text = "Código: ${viewModel.projetoAtividadeState.codigo.value ?: "-"}"
            )
        }

        Column (modifier = Modifier.padding(24.dp, 0.dp, 24.dp, 0.dp)) {
            ListaEmpenhos(appViewModel, viewModel)
        }
    }
}

@Composable
fun ListaEmpenhos(
    appViewModel: AppViewModel,
    viewModel: ProjetoAtividadeViewModel
) {

    val stateProjetoAtividade = viewModel.projetoAtividadeState
    val totalEmpenhosState = stateProjetoAtividade.stateTotalEmpenhado.collectAsState()
    val listaEmpenhosState = stateProjetoAtividade.stateListaEmpenhos.collectAsState()

    when (val state = totalEmpenhosState.value) {
        is LoadingState.Failure -> Text(state.message)
        is LoadingState.Success -> {
            Text(
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 16.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                text = "Total empenhado em 2025: ${state.response}"
            )
        }
        else -> {}
    }

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
                        EmpenhoRow(appViewModel, viewModel.projetoAtividade, empenho)
                    }
                }
            }
        }
        is LoadingState.Loading -> Text("Carregando")
        is LoadingState.NotStarted -> {}
    }

}

@Composable
fun EmpenhoRow(
    appViewModel: AppViewModel,
    projetoAtividade: ProjetoAtividade,
    empenho: Empenho
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                appViewModel.navigateToEmpenho(projetoAtividade, empenho)
            }
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            CellRow(empenho.datEmpenho?.substring(0, 10), fontWeight = FontWeight.Bold)
            CellRow(Util.formatToCurrency(empenho.valTotalEmpenhado), fontWeight = FontWeight.Bold)
        }
        Row (modifier = Modifier.fillMaxWidth().padding(0.dp, 0.dp, 0.dp, 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CellRow("Item / Despesa: ${empenho.codItemDespesa} - ${empenho.txDescricaoItemDespesa}", fontSize = 12.sp)
            CellRow("Fonte de recurso: ${empenho.codFonteRecurso} - ${empenho.txDescricaoFonteRecurso}", fontSize = 12.sp)
        }
    }
}

@Composable
fun CellRow(value: String?, fontSize: TextUnit = 14.sp, fontWeight: FontWeight = FontWeight.Normal) {
    Text(text = value?: "null", fontSize = fontSize, fontWeight = fontWeight)
}
