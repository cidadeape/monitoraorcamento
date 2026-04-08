package org.cidadeape.monitoraorcamento.presentation.screen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.cidadeape.monitoraorcamento.common.LoadingState
import org.cidadeape.monitoraorcamento.data.Ano
import org.cidadeape.monitoraorcamento.data.ApiSof
import org.cidadeape.monitoraorcamento.data.IApiSof
import org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetoAtividade
import org.cidadeape.monitoraorcamento.domain.ProjetoAtividadeUseCase
import org.cidadeape.monitoraorcamento.presentation.BaseViewModel

class BuscaProjetoScreenVM(
    ano: Ano,
    private val apiSof: IApiSof = ApiSof(ano.nome),
    private val projetoAtividadeUseCase: ProjetoAtividadeUseCase = ProjetoAtividadeUseCase(ano)
): BaseViewModel() {

    val fullList: MutableStateFlow<LoadingState<List<ProjetoAtividade>>> = MutableStateFlow(
        LoadingState.NotStarted())

    val listaCustomizadaProjetosAtividades = mutableStateListOf<ProjetoAtividadeRowState>()

    init {

        launchCoroutine {
            loadSearchList()
        }
    }

    private suspend fun loadSearchList() {
        fullList.value = LoadingState.Loading()
        fullList.value =
            try {
                val projetosAtividades = apiSof.getProjetoAtividade(codProjetoAtividade = null).lstProjetosAtividades
                LoadingState.Success(projetosAtividades)
            } catch (e: Exception) {
                e.printStackTrace()
                LoadingState.Failure("Erro ao carregar projeto/atividade: ${e::class.simpleName}")
            }
    }


    fun removerDaListaCustomizada(projetoAtividadeRowState: ProjetoAtividadeRowState) {

        listaCustomizadaProjetosAtividades.remove(projetoAtividadeRowState)
    }

    fun adicionarAListaCustomizada(projetoAtividade: ProjetoAtividade) {

        if (listaCustomizadaProjetosAtividades
                .map { it.codigo }
                .contains(projetoAtividade.codProjetoAtividade)
        ) return

        val projetoAtividadeRowState = ProjetoAtividadeRowState(
            codigo = projetoAtividade.codProjetoAtividade,
            stateProjeto = MutableStateFlow(LoadingState.Success(projetoAtividade))
        )

        listaCustomizadaProjetosAtividades.add(0, projetoAtividadeRowState)
        viewModelScope.launch(Dispatchers.Default) {
            projetoAtividadeUseCase.loadProjetoAtividadeComTotalDespesas(projetoAtividadeRowState)
        }
    }
}