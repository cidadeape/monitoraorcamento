package org.cidadeape.monitoraorcamento.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.cidadeape.monitoraorcamento.common.LoadingState
import org.cidadeape.monitoraorcamento.common.Util
import org.cidadeape.monitoraorcamento.data.ApiSof
import org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho
import org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetoAtividade
import kotlin.reflect.KClass

class ProjetoAtividadeViewModel(
    private val projetoAtividade: ProjetoAtividade,
    private val sofApi: ApiSof = ApiSof()
): ViewModel() {

    val projetoAtividadeState = ProjetoAtividadeState(
        projetoAtividade.codProjetoAtividade,
        projetoAtividade.txtDescricaoProjetoAtividade
    )

    fun initialize(projetoAtividade: ProjetoAtividade) {

    }

    init {
        loadEmpenhos(projetoAtividade.codProjetoAtividade)
    }

    private fun loadEmpenhos(codProjetoAtividade: String) {
        projetoAtividadeState.stateListaEmpenhos.value = LoadingState.Loading()
        projetoAtividadeState.stateTotalEmpenhado.value = LoadingState.Loading()

        launchCoroutine {
            try {
                val listaEmpenhos = sofApi.getEmpenhos("2025", "12", codProjetoAtividade).lstEmpenhos
                projetoAtividadeState.stateListaEmpenhos.value = LoadingState.Success(listaEmpenhos)
                val totalEmpenhado = listaEmpenhos.sumOf { it.valEmpenhadoLiquido.toDouble() }
                projetoAtividadeState.stateTotalEmpenhado.value = LoadingState.Success(Util.formatToCurrency(totalEmpenhado))
            } catch (e: Exception) {
                e.printStackTrace()
                projetoAtividadeState.stateListaEmpenhos.value = LoadingState.Failure("Erro ao carregar empenhos: ${e::class.simpleName}")
                projetoAtividadeState.stateTotalEmpenhado.value = LoadingState.Failure("Erro ao carregar empenhos.")
            }
        }
    }

    data class ProjetoAtividadeState (
        val codigo: String,
        val nome: String,
        var stateTotalEmpenhado: MutableStateFlow<LoadingState<String>> = MutableStateFlow(
            LoadingState.NotStarted()),
        var stateListaEmpenhos: MutableStateFlow<LoadingState<List<Empenho>>> = MutableStateFlow(
            LoadingState.NotStarted())
    )

    class Factory(private val projetoAtividade: ProjetoAtividade): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
            return ProjetoAtividadeViewModel(projetoAtividade) as T
        }
    }

    private fun launchCoroutine(block: suspend  () -> Unit) =
        viewModelScope.launch(Dispatchers.Default) {
            block.invoke()
        }

    companion object {
        private const val TAG = "ProjetoAtividadeViewModel"
    }
}
