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
import androidx.lifecycle.viewmodel.compose.viewModel
import org.cidadeape.monitoraorcamento.common.AppColors
import org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho
import org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetoAtividade
import org.jetbrains.compose.ui.tooling.preview.Preview

var screen: MutableState<Screen> = mutableStateOf(Screen.Home())

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    currentScreen: Screen
) {
    TopAppBar(
        title = { Text(currentScreen.title) },
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
fun App() {

    val screenState by remember { screen }

    MaterialTheme {
        Scaffold(
            topBar = {
                AppTopBar(screenState)
            },
        ) { innerPadding ->

            Box(modifier = Modifier.padding(innerPadding)) {

                when (val screen = screenState) {
                    is Screen.Home -> HomeScreen(
                        viewModel<HomeViewModel> (
                            factory = HomeViewModel.Factory()
                        )
                    )
                    is Screen.ProjetoAtividade -> ProjetoAtividadeScreen(
                        viewModel<ProjetoAtividadeViewModel>(
                            factory = ProjetoAtividadeViewModel.Factory()
                        ),
                        screen.projetoAtividade
                    )
                    is Screen.Empenho -> TODO()
                }
            }
        }
    }
}

fun navigateToHome() {
    screen.value = Screen.Home()
}

fun navigateToProjetoAtividade(projetoAtividade: ProjetoAtividade) {
    screen.value = Screen.ProjetoAtividade(
        projetoAtividade,
        true,
        ::navigateToHome
    )
}

fun navigateToEmpenho(projetoAtividade: ProjetoAtividade, empenho: Empenho) {
    screen.value = Screen.Empenho(
        empenho,
        true,
        { navigateToProjetoAtividade(projetoAtividade) }
    )
}