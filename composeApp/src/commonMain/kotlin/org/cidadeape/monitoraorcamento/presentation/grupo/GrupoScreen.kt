package org.cidadeape.monitoraorcamento.presentation.grupo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.cidadeape.monitoraorcamento.common.AppColors
import org.cidadeape.monitoraorcamento.common.LoadingState
import org.cidadeape.monitoraorcamento.common.Util
import org.cidadeape.monitoraorcamento.common.colorizedText
import org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetoAtividade
import org.cidadeape.monitoraorcamento.presentation.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrupoScreen(
    appViewModel: AppViewModel,
    grupoState: GrupoState
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        val refreshing by remember { grupoState.refreshing }

        PullToRefreshBox(
            isRefreshing = refreshing,
            onRefresh = {
                appViewModel.loadGrupo(grupoState)
            }
        ) {

            val stateTotalEmpenhado = grupoState.stateTotalEmpenhadoGrupo.collectAsState()

            val text: String = when (val state = stateTotalEmpenhado.value) {
                is LoadingState.NotStarted -> "-"
                is LoadingState.Loading -> "Carregando..."
                is LoadingState.Success -> Util.formatToCurrency(state.response.total)
                is LoadingState.Failure -> state.message
            }

            val lista = grupoState.listaProjetosAtividades

            Column {

                Text(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.Purple,
                    text = "Total empenhado: $text"
                )

                LazyColumn (
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    items(lista) { projetoAtividade ->
                        ProjetoAtividadeRow(
                            appViewModel = appViewModel,
                            projetoAtividadeState = projetoAtividade,
                            navigateUp = { appViewModel.navigateToGrupo(grupoState) }
                        )
                        Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background(AppColors.SuperLightGray))
                    }
                }
            }
        }
    }
}

@Composable
fun ProjetoAtividadeRow(
    appViewModel: AppViewModel,
    projetoAtividadeState: ProjetoAtividadeState,
    navigateUp: () -> Unit
) {

    val projetoState = projetoAtividadeState.stateProjeto.collectAsState()
    val totalEmpenhadoState = projetoAtividadeState.stateTotalEmpenhado.collectAsState()

    Row(
        modifier = Modifier.fillMaxWidth()
            .clickable {
                val state = projetoState.value as? LoadingState.Success<ProjetoAtividade>
                state?.let {
                    appViewModel.navigateToProjetoAtividade(state.response, navigateUp)
                }
            }.padding(16.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {

            Text(
                text = when (val state = projetoState.value) {
                    is LoadingState.Loading -> {
                        colorizedText(text = "Carregando...", color = Color.Black)
                    }
                    is LoadingState.Success -> {
                        colorizedText(
                            text = state.response.txtDescricaoProjetoAtividade,
                            color = Color.Black
                        )
                    }
                    is LoadingState.Failure -> {
                        colorizedText(text = state.message, color = Color.Red)
                    }
                    else -> colorizedText(text = "-", color = Color.Black)
                }
            )

            Text (
                fontSize = 12.sp,
                text = "Codigo: ${projetoAtividadeState.codigo}"
            )

            Text(
                modifier = Modifier.fillMaxWidth().padding(0.dp, 0.dp, 0.dp, 0.dp),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                text = when (val state = totalEmpenhadoState.value) {
                        is LoadingState.Loading -> {
                            colorizedText(text = "Carregando...", color = Color.Black)
                        }
                        is LoadingState.Success -> {
                            colorizedText(text = Util.formatToCurrency(state.response.total), color = Color.Black)
                        }
                        is LoadingState.Failure -> {
                            colorizedText(text = state.message, color = Color.Red)
                        }
                        else -> colorizedText(text = "-", color = Color.Black)
                    }
            )
        }
    }
}

