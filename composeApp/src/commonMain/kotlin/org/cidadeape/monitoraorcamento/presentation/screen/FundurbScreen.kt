package org.cidadeape.monitoraorcamento.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import org.cidadeape.monitoraorcamento.common.AppColors
import org.cidadeape.monitoraorcamento.common.LoadingState
import org.cidadeape.monitoraorcamento.common.TextTitle
import org.cidadeape.monitoraorcamento.common.Util
import org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho
import org.cidadeape.monitoraorcamento.domain.model.TotalDespesas
import org.cidadeape.monitoraorcamento.presentation.AppViewModel
import org.cidadeape.monitoraorcamento.presentation.compose.ButtonBaixarEmpenhos
import org.cidadeape.monitoraorcamento.presentation.compose.ListaProjetoAtividade

@Composable
fun FundurbScreen(
    fundurbViewModel: FundurbViewModel
) {
    fundurbViewModel.initialize()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextTitle(
            modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 8.dp),
            text = "FUNDURB",
        )

        val stateTotalDespesas = fundurbViewModel.stateTotalDespesas.collectAsState()
        val stateListEmpenho = fundurbViewModel.stateListEmpenho.collectAsState()

        CabecalhoFundurb(stateTotalDespesas, stateListEmpenho)

        val listaState = fundurbViewModel.stateListProjetosAtividadesRowState.collectAsState()

        when (val state = listaState.value) {
            is LoadingState.NotStarted -> {}
            is LoadingState.Loading -> {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "Carregando Projetos / Atividades..."
                )
            }
            is LoadingState.Success -> {
                ListaProjetoAtividade(
                    state.response,
                    navigateUp = { AppViewModel.navigateToFundurb() }
                )
            }
            is LoadingState.Failure -> {
                Text(
                    modifier = Modifier.padding(16.dp),
                    color = Color.Red,
                    text = state.message
                )
            }
        }
    }
}

@Composable
private fun CabecalhoFundurb(
    stateTotalDespesas: State<LoadingState<TotalDespesas>>,
    stateEmpenhoList: State<LoadingState<List<Empenho>>>
) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        when (val state = stateTotalDespesas.value) {
            is LoadingState.NotStarted -> {}
            is LoadingState.Loading -> {
                Text("Carregando totais...")
            }
            is LoadingState.Success -> {
                TotaisFundurb(state.response)
            }
            is LoadingState.Failure -> {
                Text(
                    color = Color.Red,
                    text = state.message
                )
            }
        }

        when (val state = stateEmpenhoList.value) {
            is LoadingState.Success -> ButtonBaixarEmpenhos(state.response, nomeArquivo = "Empenhos FUNDURB")
            else -> {}
        }
    }
}

@Composable
private fun TotaisFundurb(
    totalDespesas: TotalDespesas
) {
    Text(
        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 8.dp),
        textAlign = TextAlign.Start,
        color = AppColors.DarkGray,
        text = buildAnnotatedString {
            append("Orçado: ")
            if (totalDespesas.orcadoInicial != totalDespesas.orcadoAtualizado) {
                withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                    append(Util.formatToCurrency(totalDespesas.orcadoInicial))
                }
                append(" / ")
            }
            append(Util.formatToCurrency(totalDespesas.orcadoAtualizado))
        }
    )

    Text(
        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 8.dp),
        textAlign = TextAlign.Start,
        color = AppColors.DarkGray,
        text = "Empenhado (líquido): ${Util.formatToCurrency(totalDespesas.empenhadoLiquido)}"
    )

    Text(
        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 8.dp),
        textAlign = TextAlign.Start,
        fontWeight = FontWeight.Bold,
        color = AppColors.MediumBlue,
        text = "Pago: ${Util.formatToCurrency(totalDespesas.pago)}"
    )
}
