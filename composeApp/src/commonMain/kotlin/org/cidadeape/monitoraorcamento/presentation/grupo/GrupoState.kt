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
    val stateTotalEmpenhadoGrupo: MutableStateFlow<LoadingState<TotalEmpenhos>> = MutableStateFlow(LoadingState.NotStarted())
)

data class ProjetoAtividadeState (
    val codigo: String,
    var stateProjeto: MutableStateFlow<LoadingState<ProjetoAtividade>> = MutableStateFlow(LoadingState.NotStarted()),
    var stateTotalEmpenhado: MutableStateFlow<LoadingState<TotalEmpenhos>> = MutableStateFlow(LoadingState.NotStarted())
)

data class TotalEmpenhos(
    var total: Double,
    var despCorrentes: Double,
    var despCapital: Double,
    var resContingencia: Double,
)
