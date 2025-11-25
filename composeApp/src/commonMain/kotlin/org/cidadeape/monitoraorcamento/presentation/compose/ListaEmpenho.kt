package org.cidadeape.monitoraorcamento.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow
import org.cidadeape.monitoraorcamento.common.AppColors
import org.cidadeape.monitoraorcamento.common.LoadingState
import org.cidadeape.monitoraorcamento.common.Util
import org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho
import org.cidadeape.monitoraorcamento.presentation.AppViewModel

@Composable
fun ListaEmpenhos(
    stateTotalEmpenhado: MutableStateFlow<LoadingState<String>>,
    stateListaEmpenhos: MutableStateFlow<LoadingState<List<Empenho>>>,
    listName: String? = null,
    onNavigateBackFromEmpenho: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val totalEmpenhosState = stateTotalEmpenhado.collectAsState()
        val listaEmpenhosState = stateListaEmpenhos.collectAsState()

        when (val state = totalEmpenhosState.value) {
            is LoadingState.Failure -> Text(state.message)
            is LoadingState.Success -> {

                Text(
                    modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 0.dp),
                    textAlign = TextAlign.Start,
                    text = "Total empenhado (líquido):"
                )

                Text(
                    modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 0.dp),
                    textAlign = TextAlign.Start,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    text = state.response
                )
            }

            else -> {}
        }

        when (val state = listaEmpenhosState.value) {
            is LoadingState.Success -> {
                if (state.response.isNotEmpty()) {
                    ButtonBaixarEmpenhos(state.response, nomeArquivo = listName ?: "empenhos")
                }
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
                            EmpenhoRow(empenho, onNavigateBackFromEmpenho)
                            Spacer(modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(AppColors.SuperLightGray)
                            )
                        }
                    }
                }
            }
            is LoadingState.Loading -> Text("Carregando")
            is LoadingState.NotStarted -> {}
        }

    }
}

@Composable
fun EmpenhoRow(
    empenho: Empenho,
    onNavigateUpFromEmpenho: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                AppViewModel.navigateToEmpenho(empenho, onNavigateUpFromEmpenho)
            }
            .padding(16.dp)
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
            text = "Projeto / Atividade: ${empenho.codProjetoAtividade} - ${empenho.txDescricaoProjetoAtividade}",
            fontSize = 12.sp
        )

        Text(
            text = "Item / Despesa: ${empenho.codItemDespesa} - ${empenho.txDescricaoItemDespesa}",
            fontSize = 12.sp
        )

        Text(
            text = "Fonte do recurso: ${empenho.codFonteRecurso} - ${empenho.txDescricaoFonteRecurso}",
            fontSize = 12.sp
        )

        Text(
            text = "Órgão: ${empenho.codOrgao} - ${empenho.txDescricaoOrgao}",
            fontSize = 12.sp
        )

    }
}