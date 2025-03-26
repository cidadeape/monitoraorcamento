package org.cidadeape.monitoraorcamento.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.cidadeape.monitoraorcamento.common.AppColors
import org.cidadeape.monitoraorcamento.common.OpenInNewWindow
import org.cidadeape.monitoraorcamento.presentation.busca.BuscaScreen
import org.cidadeape.monitoraorcamento.presentation.empenho.EmpenhoScreen
import org.cidadeape.monitoraorcamento.presentation.grupo.GrupoScreen
import org.cidadeape.monitoraorcamento.presentation.home.HomeScreen
import org.cidadeape.monitoraorcamento.presentation.projeto_atividade.ProjetoAtividadeScreen
import org.cidadeape.monitoraorcamento.presentation.projeto_atividade.ProjetoAtividadeViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    currentScreen: Screen
) {
    TopAppBar(
        title = {
            Text(
                maxLines = 1,
                overflow = TextOverflow.Clip,
                text = currentScreen.title
            ) },
        colors = TopAppBarColors(
            containerColor = AppColors.Purple,
            scrolledContainerColor = AppColors.Purple,
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
@Preview
fun App(viewModel: AppViewModel = viewModel<AppViewModel>(factory = AppViewModel.Factory())) {

    val screenState by remember { viewModel.screenState }

    MaterialTheme {
        Scaffold(
            topBar = {
                AppTopBar(screenState)
            },
        ) { innerPadding ->

            Column {
                Box(modifier = Modifier.weight(1f).padding(innerPadding)) {

                    when (val screen = screenState) {
                        is Screen.Home -> HomeScreen(viewModel)
                        is Screen.Busca -> BuscaScreen(viewModel)
                        is Screen.Grupo -> GrupoScreen(viewModel, screen.grupoState)
                        is Screen.ProjetoAtividade -> ProjetoAtividadeScreen(
                            viewModel,
                            viewModel<ProjetoAtividadeViewModel>(
                                factory = ProjetoAtividadeViewModel.Factory()
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
    var openApiSof by remember { mutableStateOf(false) }

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = AppColors.SuperLightGray)
            .clickable { openApiSof = true }
            .padding(16.dp, 8.dp, 16.dp, 16.dp),
        textAlign = TextAlign.Center,
        fontStyle = FontStyle.Italic,
        fontSize = 14.sp,
        color = AppColors.DarkGray,
        text = "Dados em tempo real da API-SOF - Sistema de Execução Orçamentária da Prefeitura de São Paulo"
    )

    if (openApiSof) {
        OpenInNewWindow("https://capital.sp.gov.br/web/fazenda/contaspublicas/apisof")
        openApiSof = false
    }
}
