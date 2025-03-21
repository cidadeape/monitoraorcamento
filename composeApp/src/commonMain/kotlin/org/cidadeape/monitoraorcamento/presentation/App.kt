package org.cidadeape.monitoraorcamento.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import org.cidadeape.monitoraorcamento.common.AppColors
import org.cidadeape.monitoraorcamento.presentation.empenho.EmpenhoScreen
import org.cidadeape.monitoraorcamento.presentation.home.HomeScreen
import org.cidadeape.monitoraorcamento.presentation.home.HomeViewModel
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

            Box(modifier = Modifier.padding(innerPadding)) {

                when (val screen = screenState) {
                    is Screen.Home -> HomeScreen(
                        viewModel,
                        viewModel<HomeViewModel> (
                            factory = HomeViewModel.Factory()
                        )
                    )
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
        }
    }
}
