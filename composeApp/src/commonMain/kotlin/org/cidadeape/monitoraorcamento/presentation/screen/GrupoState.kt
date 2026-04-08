package org.cidadeape.monitoraorcamento.presentation.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import org.cidadeape.monitoraorcamento.common.LoadingState
import kotlinx.coroutines.flow.MutableStateFlow
import org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetoAtividade
import org.cidadeape.monitoraorcamento.domain.model.TotalDespesas

data class GrupoState (
    val id: String,
    val nome: String,
    val listaProjetosAtividades: List<ProjetoAtividadeRowState>,
    val refreshing: MutableState<Boolean> = mutableStateOf(false),
    val statePagoTotal: MutableStateFlow<LoadingState<Double>> = MutableStateFlow(LoadingState.NotStarted()),
    val stateEmpenhadoLiquidoTotal: MutableStateFlow<LoadingState<Double>> = MutableStateFlow(LoadingState.NotStarted())
)

data class ProjetoAtividadeRowState (
    val codigo: String,
    val codOrgao: Int? = null,
    var stateProjeto: MutableStateFlow<LoadingState<ProjetoAtividade>> = MutableStateFlow(LoadingState.NotStarted()),
    var stateTotalDespesas: MutableStateFlow<LoadingState<TotalDespesas>> = MutableStateFlow(LoadingState.NotStarted()),
)

