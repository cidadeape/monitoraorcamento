package org.cidadeape.monitoraorcamento.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.cidadeape.monitoraorcamento.common.AppButtonColors
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    currentScreen: Screen
) {
    TopAppBar(
        title = {
            Box(
                contentAlignment = Alignment.CenterStart
            ) {
                Image(
                    modifier = Modifier.height(32.dp).wrapContentWidth(),
                    painter = painterResource(Res.drawable.cidadeape_redondo_transp_texto_branco),
                    contentDescription = "logo da cidadeapé"
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    text = "Monitoramento do Orçamento Municipal - 2025"
                )
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

@Composable
fun TopMenu() {

    Row(modifier = Modifier.fillMaxWidth()) {
        Button(
            modifier = Modifier.weight(1f).padding(8.dp),
            shape = RectangleShape,
            colors = AppButtonColors.BlueButton,
            onClick = { AppViewModel.navigateToHome() }
        ) {
            Text(
                fontWeight = FontWeight.Bold,
                text = "Mobilidade Geral"
            )
        }

        Button(
            modifier = Modifier.weight(1f).padding(8.dp),
            shape = RectangleShape,
            colors = AppButtonColors.BlueButton,
            onClick = { AppViewModel.navigateToFundurb() }
        ) {
            Text(
                fontWeight = FontWeight.Bold,
                text = "FUNDURB"
            )
        }

//        Button(
//            modifier = Modifier.weight(1f),
//            onClick = {  }
//        ) {
//            Text("FMDT")
//        }
//
//        Button(
//            modifier = Modifier.weight(1f),
//            onClick = {  }
//        ) {
//            Text("Subprefeituras")
//        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App(viewModel: AppViewModel = viewModel<AppViewModel>(factory = AppViewModel.Factory())) {

    val fundurbViewModel = FundurbViewModel()

    val screenState by remember { AppViewModel.screenState }

    MaterialTheme {
        Scaffold(
            topBar = {
                AppTopBar(screenState)
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
                    listOf("Mobilidade Geral", "Fundurb").forEachIndexed { index, tabName ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = {
                                if (index == 0) {
                                    AppViewModel.navigateToHome()
                                } else if (index == 1) {
                                    AppViewModel.navigateToFundurb()
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
                        is Screen.Home -> HomeScreen(viewModel)
                        is Screen.Fundurb -> FundurbScreen(fundurbViewModel)
                        is Screen.BuscaProjeto -> BuscaProjetoScreen(viewModel)
                        is Screen.BuscaEmpenho -> BuscaEmpenhoScreen()
                        is Screen.Grupo -> GrupoScreen(viewModel, screen.grupoState)
                        is Screen.ProjetoAtividade -> ProjetoAtividadeScreen(
                            viewModel<ProjetoAtividadeViewModel>(
                                factory = ProjetoAtividadeViewModel.Factory(),
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
