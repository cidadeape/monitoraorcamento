package org.cidadeape.monitoraorcamento.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho
import org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetoAtividade
import org.cidadeape.monitoraorcamento.presentation.screen.GrupoState
import org.cidadeape.monitoraorcamento.presentation.screen.Screen

object Navigation {

    var screenState: MutableState<Screen> = mutableStateOf(Screen.Home())

    fun navigateToHome() {
        screenState.value = Screen.Home()
    }

    fun navigateToFundurb() {
        screenState.value = Screen.Fundurb()
    }

    fun navigateToBuscaProjeto() {
        screenState.value = Screen.BuscaProjeto(::navigateToHome)
    }

    fun navigateToBuscaEmpenho() {
        screenState.value = Screen.BuscaEmpenho(::navigateToHome)
    }

    fun navigateToProjetoAtividade(projetoAtividade: ProjetoAtividade, navigateUp: (() -> Unit)?) {
        screenState.value = Screen.ProjetoAtividade(
            projetoAtividade,
            true,
            navigateUp ?: ::navigateToHome
        )
    }

    fun navigateToEmpenho(empenho: Empenho, navigateUp: (() -> Unit)?) {
        screenState.value = Screen.Empenho(
            empenho,
            true,
            navigateUp ?: ::navigateToHome
        )
    }

    fun navigateToGrupo(grupoId: String, grupoNome: String) {
        screenState.value = Screen.Grupo(
            grupoId,
            grupoNome,
            true,
            ::navigateToHome
        )
    }
}