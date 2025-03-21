package org.cidadeape.monitoraorcamento.presentation.projeto_atividade

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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
    private val sofApi: ApiSof = ApiSof()
): ViewModel() {

    lateinit var projetoAtividade: ProjetoAtividade
    var projetoAtividadeState = ProjetoAtividadeState()

    fun load(projetoAtividade: ProjetoAtividade) {
        this.projetoAtividade = projetoAtividade
        projetoAtividadeState.codigo.value = projetoAtividade.codProjetoAtividade
        projetoAtividadeState.nome.value = projetoAtividade.txtDescricaoProjetoAtividade

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
        val codigo: MutableState<String?> = mutableStateOf( null),
        val nome: MutableState<String?> = mutableStateOf(null),
        var stateTotalEmpenhado: MutableStateFlow<LoadingState<String>> = MutableStateFlow(
            LoadingState.NotStarted()),
        var stateListaEmpenhos: MutableStateFlow<LoadingState<List<Empenho>>> = MutableStateFlow(
            LoadingState.NotStarted())
    )

    @Suppress("UNCHECKED_CAST")
    class Factory: ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
            return ProjetoAtividadeViewModel() as T
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
