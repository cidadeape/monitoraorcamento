package org.cidadeape.monitoraorcamento.presentation

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho
import org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetoAtividade
import org.jetbrains.compose.ui.tooling.preview.Preview

var screen: MutableState<Screen> = mutableStateOf(Screen.Home())

@Composable
fun AppTopBar(
    currentScreen: Screen
) {
    TopAppBar(
        title = { Text(currentScreen.title) },
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

            when (val screen = screenState) {
                is Screen.Home -> HomeScreen(HomeViewModel())
                is Screen.ProjetoAtividade -> ProjetoAtividadeScreen(
                    viewModel<ProjetoAtividadeViewModel>(
                        factory = ProjetoAtividadeViewModel.Factory(screen.projetoAtividade)
                    )
                )
                is Screen.Empenho -> TODO()
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