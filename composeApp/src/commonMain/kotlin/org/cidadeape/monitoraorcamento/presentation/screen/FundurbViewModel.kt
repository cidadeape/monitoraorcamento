package org.cidadeape.monitoraorcamento.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.cidadeape.monitoraorcamento.common.LoadingState
import org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho
import org.cidadeape.monitoraorcamento.domain.OrgaoUseCase
import org.cidadeape.monitoraorcamento.domain.model.TotalDespesas

class FundurbViewModel(
    private val orgaoUseCase: OrgaoUseCase = OrgaoUseCase(codOrgao = COD_ORGAO_FUNDURB)
): ViewModel() {

    val stateTotalDespesas: MutableStateFlow<LoadingState<TotalDespesas>> = MutableStateFlow(LoadingState.NotStarted())

    val stateListProjetosAtividadesRowState: MutableStateFlow<LoadingState<List<ProjetoAtividadeRowState>>> = MutableStateFlow(LoadingState.NotStarted())

    val stateListEmpenho: MutableStateFlow<LoadingState<List<Empenho>>> = MutableStateFlow(LoadingState.NotStarted())

    fun initialize() {
        if (stateTotalDespesas.value !is LoadingState.Success)
            loadTotalDespesasFundurb()

        if (stateListProjetosAtividadesRowState.value !is LoadingState.Success)
            loadProjetosAtividadesComDespesas()

        if (stateListEmpenho.value !is LoadingState.Success)
            loadListaEmpenhos()
    }

    private fun loadListaEmpenhos() = viewModelScope.launch {
        stateListEmpenho.value = LoadingState.Loading()
        try {
            stateListEmpenho.value = LoadingState.Success(orgaoUseCase.getEmpenhos())
        } catch (e: Exception) {
            e.printStackTrace()
            stateListEmpenho.value = LoadingState.Failure("Erro ao carregar lista de empenhos Fundurb.")
        }
    }

    private fun loadTotalDespesasFundurb() = viewModelScope.launch {
        stateTotalDespesas.value = LoadingState.Loading()
        try {
            stateTotalDespesas.value = LoadingState.Success(orgaoUseCase.getTotalDespesas())
        } catch (e: Exception) {
            e.printStackTrace()
            stateTotalDespesas.value =
                LoadingState.Failure("Erro ao carregar totais")
        }
    }

    private fun loadProjetosAtividadesComDespesas() = viewModelScope.launch {
        loadProjetosAtividades()
        loadTotalDespesasProjetosAtividades()
        ordenarPorTotalPago()
    }

    private fun ordenarPorTotalPago() {
        val listProjAtivRowState = stateListProjetosAtividadesRowState.value
                as? LoadingState.Success ?: return

        val sortedList = listProjAtivRowState.response.sortedByDescending {
            val totalDespesas = it.stateTotalDespesas.value as? LoadingState.Success ?: return@sortedByDescending -1
            totalDespesas.response.pago.toInt()
        }

        stateListProjetosAtividadesRowState.value = LoadingState.Success(sortedList)
    }

    private suspend fun loadProjetosAtividades() {
        stateListProjetosAtividadesRowState.value = LoadingState.Loading()
        try {
            val projetosAtividades = orgaoUseCase.getProjetosAtividades()

            val listProjetoAtividadeRowState = projetosAtividades.map {
                ProjetoAtividadeRowState(
                    codigo = it.codProjetoAtividade,
                    codOrgao = COD_ORGAO_FUNDURB,
                    stateProjeto = MutableStateFlow(LoadingState.Success(it))
                )
            }

            stateListProjetosAtividadesRowState.value = LoadingState.Success(listProjetoAtividadeRowState)
        } catch (e: Exception) {
            e.printStackTrace()
            stateListProjetosAtividadesRowState.value = LoadingState.Failure(
                "Erro ao carregar projetos/atividades"
            )
        }
    }

    private suspend fun loadTotalDespesasProjetosAtividades() {
        val listProjAtivRowState = stateListProjetosAtividadesRowState.value
                as? LoadingState.Success ?: return

        listProjAtivRowState.response.forEach { projAtivRowState ->

            projAtivRowState.stateTotalDespesas.value = LoadingState.Loading()
            try {
                val totalDespesas = orgaoUseCase.getTotalDespesasProjAtividade(projAtivRowState.codigo)
                projAtivRowState.stateTotalDespesas.value = LoadingState.Success(totalDespesas)
            } catch (e: Exception) {
                projAtivRowState.stateTotalDespesas.value =
                    LoadingState.Failure("Erro ao carregar totais para projeto / atividade:" +
                            " ${projAtivRowState.codigo}")
            }
        }
    }

    companion object {
        private const val COD_ORGAO_FUNDURB = 98
    }
}
