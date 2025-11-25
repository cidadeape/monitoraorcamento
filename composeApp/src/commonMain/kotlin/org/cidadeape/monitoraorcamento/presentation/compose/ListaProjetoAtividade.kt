package org.cidadeape.monitoraorcamento.presentation.compose

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.sp
import org.cidadeape.monitoraorcamento.common.AppColors
import org.cidadeape.monitoraorcamento.common.LoadingState
import org.cidadeape.monitoraorcamento.common.Util
import org.cidadeape.monitoraorcamento.common.colorizedText
import org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetoAtividade
import org.cidadeape.monitoraorcamento.presentation.AppViewModel
import org.cidadeape.monitoraorcamento.presentation.screen.ProjetoAtividadeRowState

@Composable
fun ListaProjetoAtividade(
    lista: List<ProjetoAtividadeRowState>,
    navigateUp: () -> Unit
) {

    LazyColumn (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        items(lista) { projetoAtividade ->
            ProjetoAtividadeRow(
                projetoAtividadeRowState = projetoAtividade,
                navigateUp = navigateUp
            )
            Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background(AppColors.SuperLightGray))
        }
    }
}

@Composable
fun ProjetoAtividadeRow(
    projetoAtividadeRowState: ProjetoAtividadeRowState,
    navigateUp: () -> Unit
) {

    val projetoState = projetoAtividadeRowState.stateProjeto.collectAsState()
    val despesasTotalState = projetoAtividadeRowState.stateTotalDespesas.collectAsState()

    Row(
        modifier = Modifier.fillMaxWidth()
            .clickable {
                val state = projetoState.value as? LoadingState.Success<ProjetoAtividade>
                state?.let {
                    AppViewModel.navigateToProjetoAtividade(state.response, navigateUp)
                }
            }.padding(16.dp)
    ) {
        Column(modifier = Modifier.weight(1f).align(Alignment.CenterVertically)) {

            Text("Projeto / Atividade")

            Text(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
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

            Text(
                fontSize = 12.sp,
                text = "Codigo: ${projetoAtividadeRowState.codigo}"
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {

            when (val state = despesasTotalState.value) {
                is LoadingState.Loading -> {
                    Text(colorizedText(text = "Carregando...", color = Color.Black))
                }
                is LoadingState.Success -> {

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        fontSize = 12.sp,
                        text = buildAnnotatedString {
                            append("Orçado: ")
                            if (state.response.orcadoInicial != state.response.orcadoAtualizado) {
                                withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                                    append(Util.formatToCurrency(state.response.orcadoInicial))
                                }
                                append(" / ")
                            }
                            append(Util.formatToCurrency(state.response.orcadoAtualizado))
                        }
                    )

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        fontSize = 12.sp,
                        text = "Empenhado (líquido): ${Util.formatToCurrency(state.response.empenhadoLiquido)}"
                    )

                    Text(
                        modifier = Modifier.fillMaxWidth().padding(0.dp, 0.dp, 0.dp, 0.dp),
                        textAlign = TextAlign.End,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        text = colorizedText(
                            text = "Pago: ${Util.formatToCurrency(state.response.pago)}",
                            color = Color.Black
                        )
                    )
                }
                is LoadingState.Failure -> {
                    Text(colorizedText(text = state.message, color = Color.Red))
                }
                else -> Text(colorizedText(text = "-", color = Color.Black))
            }
        }
    }
}

