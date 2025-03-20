package org.cidadeape.monitoraorcamento.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.cidadeape.monitoraorcamento.common.LoadingState
import kotlinx.coroutines.flow.MutableStateFlow
import org.cidadeape.monitoraorcamento.common.Util
import org.cidadeape.monitoraorcamento.data.ApiSof
import org.cidadeape.monitoraorcamento.data.IApiSof
import org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetoAtividade

class HomeViewModel(
    private val sofApi: IApiSof = ApiSof()
): ViewModel() {

    val listaProjetosAtividades = mutableStateListOf(
            ProjetoAtividadeState("1137"),
            ProjetoAtividadeState("1169"),
            ProjetoAtividadeState("3757"),
            ProjetoAtividadeState("1097"),
            ProjetoAtividadeState("1098"),
            ProjetoAtividadeState("1099"),
            ProjetoAtividadeState("2095")
    )

    val fullList: MutableStateFlow<LoadingState<List<ProjetoAtividade>>> = MutableStateFlow(LoadingState.NotStarted())

    init {
        loadAll()
        loadList()
    }

    fun removeFromList(projetoAtividadeState: ProjetoAtividadeState) {

        listaProjetosAtividades.remove(projetoAtividadeState)
    }

    fun addToList(projetoAtividade: ProjetoAtividade) {
        val projetoAtividadeState = ProjetoAtividadeState(
            codigo = projetoAtividade.codProjetoAtividade,
            stateProjeto = MutableStateFlow(LoadingState.Success(projetoAtividade))
        )

        listaProjetosAtividades.add(0, projetoAtividadeState)
        loadProjetoAtividade(projetoAtividadeState)
    }

    private fun loadAll() = launchCoroutine {
        fullList.value = LoadingState.Loading()
        fullList.value =
        try {
            val projetosAtividades = sofApi.getProjetoAtividade("2025", null).lstProjetosAtividades
            LoadingState.Success(projetosAtividades)
        } catch (e: Exception) {
            e.printStackTrace()
            LoadingState.Failure("Erro ao carregar projeto/atividade: ${e::class.simpleName}")
        }
    }

    private fun loadList() {

        for (projAtividade in listaProjetosAtividades) {
            loadProjetoAtividade(projAtividade)
        }
    }

    private fun loadProjetoAtividade(projetoAtividadeState: ProjetoAtividadeState) = launchCoroutine {
        if (projetoAtividadeState.stateProjeto.value !is LoadingState.Success) loadProjeto(projetoAtividadeState)

        loadTotalEmpenhos(projetoAtividadeState)
    }

    private fun loadProjeto(projetoAtividadeState: ProjetoAtividadeState) = launchCoroutine {

        projetoAtividadeState.stateProjeto.value = LoadingState.Loading()
        projetoAtividadeState.stateProjeto.value = try {
            LoadingState.Loading<String>()
            val nome = sofApi
                .getProjetoAtividade("2025", projetoAtividadeState.codigo)
                .lstProjetosAtividades[0]
            LoadingState.Success(nome)
        } catch (e: Exception) {
            e.printStackTrace()
            LoadingState.Failure("Erro ao carregar projeto/atividade: ${e::class.simpleName}")
        }
    }

    private fun loadTotalEmpenhos(projetoAtividadeState: ProjetoAtividadeState) = launchCoroutine {

        projetoAtividadeState.stateTotalEmpenhado.value = LoadingState.Loading()

        projetoAtividadeState.stateTotalEmpenhado.value = try {
            val listaEmpenhos = sofApi
                .getEmpenhos("2025", "12", projetoAtividadeState.codigo)
                .lstEmpenhos

            val totalEmpenhado = listaEmpenhos.sumOf { it.valEmpenhadoLiquido.toDouble() }
            LoadingState.Success(Util.formatToCurrency(totalEmpenhado))
        } catch (e: Exception) {
            e.printStackTrace()
            LoadingState.Failure("Erro ao carregar empenhos: ${e::class.simpleName}")
        }
    }

    data class ProjetoAtividadeState (
        val codigo: String,
        var stateProjeto: MutableStateFlow<LoadingState<ProjetoAtividade>> = MutableStateFlow(LoadingState.NotStarted()),
        var stateTotalEmpenhado: MutableStateFlow<LoadingState<String>> = MutableStateFlow(LoadingState.NotStarted())
    )

    private fun launchCoroutine(block: suspend  () -> Unit) =
        viewModelScope.launch(Dispatchers.Default) {
            block.invoke()
        }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}
