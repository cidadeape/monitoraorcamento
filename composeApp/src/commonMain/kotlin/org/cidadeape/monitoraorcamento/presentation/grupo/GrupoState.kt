package org.cidadeape.monitoraorcamento.presentation.grupo

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import org.cidadeape.monitoraorcamento.common.LoadingState
import kotlinx.coroutines.flow.MutableStateFlow
import org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetoAtividade

data class GrupoState (
    val nome: String,
    val listaProjetosAtividades: List<ProjetoAtividadeState>,
    val refreshing: MutableState<Boolean> = mutableStateOf(false),
    val statePagoTotal: MutableStateFlow<LoadingState<Double>> = MutableStateFlow(LoadingState.NotStarted()),
    val statePagoCapital: MutableStateFlow<LoadingState<Double>> = MutableStateFlow(LoadingState.NotStarted()),
    val stateEmpenhadoLiquidoTotal: MutableStateFlow<LoadingState<Double>> = MutableStateFlow(LoadingState.NotStarted())
)

data class ProjetoAtividadeState (
    val codigo: String,
    var stateProjeto: MutableStateFlow<LoadingState<ProjetoAtividade>> = MutableStateFlow(LoadingState.NotStarted()),
    var stateDespesasTotal: MutableStateFlow<LoadingState<TotalDespesas>> = MutableStateFlow(LoadingState.NotStarted()),
    var stateDespesasCapital: MutableStateFlow<LoadingState<TotalDespesas>> = MutableStateFlow(LoadingState.NotStarted())
)

data class TotalDespesas(
    var orcadoInicial: Double,
    var orcadoAtualizado: Double,
    var empenhadoLiquido: Double,
    var pago: Double
)
