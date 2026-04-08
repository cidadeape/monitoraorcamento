package org.cidadeape.monitoraorcamento.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.datetime.Clock.System
import org.cidadeape.monitoraorcamento.common.AppColors
import org.cidadeape.monitoraorcamento.common.colorizedText
import org.cidadeape.monitoraorcamento.presentation.screen.BuscaEmpenhoScreen
import org.cidadeape.monitoraorcamento.presentation.screen.BuscaProjetoScreen
import org.cidadeape.monitoraorcamento.presentation.screen.EmpenhoScreen
import org.cidadeape.monitoraorcamento.presentation.screen.FundurbScreen
import org.cidadeape.monitoraorcamento.presentation.screen.FundurbViewModel
import org.cidadeape.monitoraorcamento.presentation.screen.GrupoScreen
import org.cidadeape.monitoraorcamento.presentation.screen.HomeScreen
import org.cidadeape.monitoraorcamento.presentation.screen.ProjetoAtividadeScreen
import org.cidadeape.monitoraorcamento.presentation.screen.ProjetoAtividadeViewModel
import org.cidadeape.monitoraorcamento.presentation.screen.Screen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import monitoraorcamento.composeapp.generated.resources.Res
import monitoraorcamento.composeapp.generated.resources.cidadeape_redondo_transp_texto_branco
import org.cidadeape.monitoraorcamento.common.AppButtonColors
import org.cidadeape.monitoraorcamento.common.Logger
import org.cidadeape.monitoraorcamento.data.Ano
import org.cidadeape.monitoraorcamento.presentation.screen.BuscaProjetoScreenVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    currentScreen: Screen,
    ano: Ano,
    onChangeAno: (ano: Ano) -> Unit
) {

    var buttonColor2025 by remember {mutableStateOf(AppButtonColors.BlueButton)}
    var buttonColor2026 by remember {mutableStateOf(AppButtonColors.GreenButton)}

    if (ano == Ano._2025){
        buttonColor2025 = AppButtonColors.GreenButton
        buttonColor2026 = AppButtonColors.BlueButton
    } else if (ano == Ano._2026) {
        buttonColor2025 = AppButtonColors.BlueButton
        buttonColor2026 = AppButtonColors.GreenButton
    }

    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.height(32.dp).wrapContentWidth(),
                    painter = painterResource(Res.drawable.cidadeape_redondo_transp_texto_branco),
                    contentDescription = "logo da cidadeapé"
                )
                Text(
                    modifier = Modifier.weight(1f).padding(32.dp, 0.dp, 0.dp, 0.dp),
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    text = "Monitoramento do Orçamento Municipal"
                )
                Button(
                    onClick = { onChangeAno(Ano._2025) },
                    colors = buttonColor2025
                ) {
                    Text(text = "2025")
                }
                Button(
                    modifier = Modifier.padding(32.dp, 0.dp, 64.dp, 0.dp),
                    onClick = { onChangeAno(Ano._2026) },
                    colors = buttonColor2026
                ) {
                    Text(text = "2026")
                }
            }
        },
        colors = TopAppBarColors(
            containerColor = AppColors.MediumBlue,
            scrolledContainerColor = AppColors.MediumBlue,
            navigationIconContentColor = Color.White,
            titleContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        navigationIcon = {
            if (currentScreen.canNavigateBack) {
                IconButton(onClick = currentScreen.navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {

    var ano by remember { mutableStateOf(Ano._2025) }

    Logger.i("App", "Ano: ${ano.nome}")

    val homeViewModel = HomeViewModel(ano)

    val buscaProjetoViewModel = BuscaProjetoScreenVM(ano)

    val fundurbViewModel = FundurbViewModel(ano)

    val screenState by remember { Navigation.screenState }

    MaterialTheme {
        Scaffold(
            topBar = {
                AppTopBar(screenState, ano, {newAno -> ano = newAno})
            },
        ) { innerPadding ->

//            val navController = rememberNavController()
//            val startDestination = Screen.Home()
//            var selectedDestination by rememberSaveable { mutableIntStateOf(0) }

            var selectedTabIndex by remember { mutableIntStateOf(0) }

            Column(
                modifier = Modifier.padding(innerPadding)
            ) {

                PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
                    listOf("Comparativo Mobilidade", "FUNDURB", "Busca de Projetos/Atividades").forEachIndexed { index, tabName ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = {
                                if (index == 0) {
                                    Navigation.navigateToHome()
                                } else if (index == 1) {
                                    Navigation.navigateToFundurb()
                                } else if (index == 2) {
                                    Navigation.navigateToBuscaProjeto()
                                }
                                selectedTabIndex = index
                             },
                            text = {
                                Text(text = tabName)
                            }
                        )
                    }
                }

                Box(modifier = Modifier.weight(1f)) {

                    when (val screen = screenState) {
                        is Screen.Home -> HomeScreen(homeViewModel)
                        is Screen.Fundurb -> FundurbScreen(fundurbViewModel)
                        is Screen.BuscaProjeto -> BuscaProjetoScreen(buscaProjetoViewModel)
                        is Screen.BuscaEmpenho -> BuscaEmpenhoScreen(ano)
                        is Screen.Grupo -> {

                            Logger.i("App", "GrupoScreen recalled")

                            GrupoScreen(screen.grupoId, homeViewModel)
                        }
                        is Screen.ProjetoAtividade -> ProjetoAtividadeScreen(
                            viewModel<ProjetoAtividadeViewModel>(
                                factory = ProjetoAtividadeViewModel.Factory(ano),
                            ),
                            screen.projetoAtividade
                        )
                        is Screen.Empenho -> EmpenhoScreen(screen.empenho)
                    }
                }
                Box(modifier = Modifier.wrapContentHeight()) {
                    Footer()
                }
            }
        }
    }
}

@Composable
fun Footer() {

    Column {
        Box(modifier = Modifier.wrapContentHeight()) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = AppColors.SuperLightYellow)
                    .padding(16.dp, 2.dp, 16.dp, 4.dp),
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic,
                fontSize = 14.sp,
                color = AppColors.DarkYellow,
                text = "Ferramenta em fase de teste"
            )
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = AppColors.SuperLightGray)
                .padding(16.dp, 8.dp, 16.dp, 16.dp),
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Italic,
            fontSize = 14.sp,
            color = AppColors.DarkGray,
            text = buildAnnotatedString {

                append("Criado pela ")
                withLink(LinkAnnotation.Url(url = "https://www.cidadeape.org")) {
                    append(colorizedText(AppColors.MediumBlue, "Cidadeapé - cidadeape.org"))
                }
                append(" | Dados da ")
                withLink(LinkAnnotation.Url(url = "https://capital.sp.gov.br/web/fazenda/contaspublicas/apisof")) {
                    append(colorizedText(AppColors.MediumBlue, "API-SOF - Prefeitura de São Paulo"))
                }
            }
        )
    }
}
