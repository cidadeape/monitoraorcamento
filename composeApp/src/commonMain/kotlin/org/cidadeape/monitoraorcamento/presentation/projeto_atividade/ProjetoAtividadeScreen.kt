package org.cidadeape.monitoraorcamento.presentation.projeto_atividade

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.cidadeape.monitoraorcamento.common.AppColors
import org.cidadeape.monitoraorcamento.common.DownloadCsv
import org.cidadeape.monitoraorcamento.common.LoadingState
import org.cidadeape.monitoraorcamento.common.TextTitle
import org.cidadeape.monitoraorcamento.common.Util
import org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho
import org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetoAtividade
import org.cidadeape.monitoraorcamento.presentation.AppViewModel

@Composable
fun ProjetoAtividadeScreen(
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

        Column (modifier = Modifier.padding(24.dp, 0.dp, 8.dp, 0.dp)) {
            ListaEmpenhos(viewModel)
        }
    }
}

@Composable
fun ListaEmpenhos(
    viewModel: ProjetoAtividadeViewModel
) {

    val stateProjetoAtividade = viewModel.projetoAtividadeState
    val totalEmpenhosState = stateProjetoAtividade.stateTotalEmpenhado.collectAsState()
    val listaEmpenhosState = stateProjetoAtividade.stateListaEmpenhos.collectAsState()

    Text(
        modifier = Modifier.fillMaxWidth().padding(0.dp, 0.dp, 0.dp, 4.dp),
        textAlign = TextAlign.Start,
        text = "Total empenhado líquido em 2025:"
    )

    when (val state = totalEmpenhosState.value) {
        is LoadingState.Failure -> Text(state.message)
        is LoadingState.Success -> {
            Text(
                modifier = Modifier.fillMaxWidth().padding(0.dp, 0.dp, 0.dp, 16.dp),
                textAlign = TextAlign.Start,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                text = state.response
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

                var downloadCsv by remember { mutableStateOf(false) }
                Button(
                    modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 16.dp),
                    onClick = {
                        downloadCsv = true
                    }
                ) {
                    Text("Baixar lista de empenhos")
                }
                if (downloadCsv) {
                    DownloadCsv(viewModel.projetoAtividade, state.response)
                    downloadCsv = false
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    for (empenho in state.response) {
                        EmpenhoRow(viewModel.projetoAtividade, empenho)
                        Spacer(modifier = Modifier.fillMaxWidth().padding(0.dp, 8.dp, 0.dp, 8.dp).height(1.dp).background(AppColors.SuperLightGray))
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
    projetoAtividade: ProjetoAtividade,
    empenho: Empenho
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                AppViewModel.navigateToEmpenho(projetoAtividade, empenho)
            }
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Nº ${empenho.codEmpenho} - ${empenho.datEmpenho?.substring(0, 10) ?: ""}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = Util.formatToCurrency(empenho.valEmpenhadoLiquido),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = "Item / Despesa: ${empenho.codItemDespesa} - ${empenho.txDescricaoItemDespesa}",
            fontSize = 12.sp
        )

        Text(
            text = "Fonte do recurso: ${empenho.codFonteRecurso} - ${empenho.txDescricaoFonteRecurso}",
            fontSize = 12.sp
        )

        Text(
            text = "Grupo de despesa: ${empenho.codGrupo} - ${empenho.txDescricaoGrupoDespesa}",
            fontSize = 12.sp
        )

    }
}

@Composable
fun CellRow(value: String?, fontSize: TextUnit = 14.sp, fontWeight: FontWeight = FontWeight.Normal) {
    Text(text = value?: "null", fontSize = fontSize, fontWeight = fontWeight)
}
