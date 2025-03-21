package org.cidadeape.monitoraorcamento.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho
import org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetoAtividade
import kotlin.reflect.KClass

class AppViewModel: ViewModel() {

    var screenState: MutableState<Screen> = mutableStateOf(Screen.Home())

    fun navigateToHome() {
        screenState.value = Screen.Home()
    }

    fun navigateToProjetoAtividade(projetoAtividade: ProjetoAtividade) {
        screenState.value = Screen.ProjetoAtividade(
            projetoAtividade,
            true,
            ::navigateToHome
        )
    }

    fun navigateToEmpenho(projetoAtividade: ProjetoAtividade, empenho: Empenho) {
        screenState.value = Screen.Empenho(
            empenho,
            true,
            { navigateToProjetoAtividade(projetoAtividade) }
        )
    }

    @Suppress("UNCHECKED_CAST")
    class Factory: ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
            return AppViewModel() as T
        }
    }

}