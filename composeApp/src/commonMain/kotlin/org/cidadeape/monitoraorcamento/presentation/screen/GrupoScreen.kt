package org.cidadeape.monitoraorcamento.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.cidadeape.monitoraorcamento.common.AppColors
import org.cidadeape.monitoraorcamento.common.LoadingState
import org.cidadeape.monitoraorcamento.common.TextTitle
import org.cidadeape.monitoraorcamento.common.Util
import org.cidadeape.monitoraorcamento.presentation.AppViewModel
import org.cidadeape.monitoraorcamento.presentation.compose.ListaProjetoAtividade

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
                appViewModel.reloadGrupo(grupoState)
            }
        ) {

            val stateTotalEmpenhado = grupoState.stateEmpenhadoLiquidoTotal.collectAsState()
            val stateTotalPago = grupoState.statePagoTotal.collectAsState()

            val textEmpenhadoLiquido: String = when (val state = stateTotalEmpenhado.value) {
                is LoadingState.NotStarted -> "-"
                is LoadingState.Loading -> "Carregando..."
                is LoadingState.Success -> Util.formatToCurrency(state.response)
                is LoadingState.Failure -> state.message
            }

            val textPago: String = when (val state = stateTotalPago.value) {
                is LoadingState.NotStarted -> "-"
                is LoadingState.Loading -> "Carregando..."
                is LoadingState.Success -> Util.formatToCurrency(state.response)
                is LoadingState.Failure -> state.message
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                TextTitle(
                    modifier = Modifier.padding(16.dp),
                    text = "Dotações agrupadas pelo tema: ${grupoState.nome}",
                )

                Text(
                    modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 0.dp),
                    text = "Total Empenhado (líquido): $textEmpenhadoLiquido"
                )

                Text(
                    modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 16.dp),
                    fontWeight = FontWeight.Bold,
                    color = AppColors.MediumBlue,
                    text = "Total Pago: $textPago"
                )

                val lista = grupoState.listaProjetosAtividades

                ListaProjetoAtividade(
                    lista,
                    navigateUp = { AppViewModel.navigateToGrupo(grupoState) }
                )
            }
        }
    }
}
