package org.cidadeape.monitoraorcamento.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import monitoraorcamento.composeapp.generated.resources.Res
import monitoraorcamento.composeapp.generated.resources.refresh_24dp
import org.cidadeape.monitoraorcamento.common.AppColors
import org.cidadeape.monitoraorcamento.common.LoadingState
import org.cidadeape.monitoraorcamento.common.Util
import org.cidadeape.monitoraorcamento.common.colorizedText
import org.cidadeape.monitoraorcamento.presentation.AppViewModel
import org.cidadeape.monitoraorcamento.presentation.grupo.GrupoState
import org.jetbrains.compose.resources.imageResource

@Composable
fun HomeScreen(
    appViewModel: AppViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
    ) {

        Text(
            modifier = Modifier.fillMaxWidth().padding(16.dp).clickable {
                appViewModel.navigateToBusca()
            },
            color = AppColors.Purple,
            textAlign = TextAlign.Start,
            text = "> Busca customizada de Projetos e Atividades"
        )

        Text(
            modifier = Modifier.fillMaxWidth().padding(16.dp, 16.dp, 16.dp, 4.dp),
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Bold,
            color = AppColors.Purple,
            text = "Valores pagos em 2025 por tema:"
        )

        val listaGrupos = remember { appViewModel.listaGrupos }

        for (grupoState in listaGrupos) {
            GrupoRow(appViewModel, grupoState)
            Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background(AppColors.SuperLightGray))
        }
    }
}


@Composable
fun GrupoRow(
    appViewModel: AppViewModel,
    grupoState: GrupoState
) {

    val pagoTotalState = grupoState.statePagoTotal.collectAsState()
    val pagoCapitalState = grupoState.statePagoCapital.collectAsState()
    val empenhadoTotalState = grupoState.stateEmpenhadoLiquidoTotal.collectAsState()

    Row(
        modifier = Modifier.fillMaxWidth()
            .clickable { appViewModel.navigateToGrupo(grupoState) }
            .padding(16.dp)
    ) {
        Column(modifier = Modifier
            .weight(1f)
            .align(Alignment.CenterVertically)
        ) {
            Text(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                text = grupoState.nome
            )
        }

        Column(modifier = Modifier
            .weight(1f)
            .align(Alignment.CenterVertically)
            .padding(0.dp, 0.dp, 16.dp, 0.dp)
        ) {

            when (val state = pagoTotalState.value) {
                is LoadingState.Loading -> {
                    Text(colorizedText(text = "Carregando...", color = Color.Black))
                }

                is LoadingState.Success -> {
                    Text(
                        modifier = Modifier.wrapContentWidth().padding(0.dp, 8.dp, 0.dp, 0.dp).align(Alignment.End),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        fontSize = 18.sp,
                        text = colorizedText(
                            text = Util.formatToCurrency(state.response),
                            color = Color.Black
                        )
                    )
                }

                is LoadingState.Failure -> {
                    Text(colorizedText(text = state.message, color = Color.Red))
                }

                else -> Text(colorizedText(text = "-", color = Color.Black))
            }

            when (val state = pagoCapitalState.value) {
                is LoadingState.Loading -> {
                    Text(colorizedText(text = "Carregando...", color = Color.Black))
                }

                is LoadingState.Success -> {
                    Text(
                        modifier = Modifier.wrapContentWidth().align(Alignment.End),
                        fontSize = 12.sp,
                        textAlign = TextAlign.End,
                        text = "Investimentos: ${Util.formatToCurrency(state.response)}"
                    )
                }

                is LoadingState.Failure -> {
                    Text(colorizedText(text = state.message, color = Color.Red))
                }

                else -> Text(colorizedText(text = "-", color = Color.Black))
            }

            when (val state = empenhadoTotalState.value) {
                is LoadingState.Loading -> {
                    Text(colorizedText(text = "Carregando...", color = Color.Black))
                }

                is LoadingState.Success -> {
                    Text(
                        modifier = Modifier.wrapContentWidth().align(Alignment.End),
                        fontSize = 12.sp,
                        textAlign = TextAlign.End,
                        text = "Total empenhado: ${Util.formatToCurrency(state.response)}"
                    )
                }

                is LoadingState.Failure -> {
                    Text(colorizedText(text = state.message, color = Color.Red))
                }

                else -> Text(colorizedText(text = "-", color = Color.Black))
            }
        }

        Column(modifier = Modifier
            .wrapContentWidth()
            .align(Alignment.CenterVertically)
        ) {

            Image(
                modifier = Modifier
                    .padding(8.dp)
                    .size(24.dp)
                    .clickable { appViewModel.loadGrupo(grupoState) },
                bitmap = imageResource(Res.drawable.refresh_24dp),
                contentDescription = "Atualizar"
            )
        }
    }
}
