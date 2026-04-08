package org.cidadeape.monitoraorcamento.presentation.screen

import kotlinx.coroutines.flow.MutableStateFlow
import org.cidadeape.monitoraorcamento.common.LoadingState
import org.cidadeape.monitoraorcamento.common.Logger
import org.cidadeape.monitoraorcamento.data.Ano
import org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho
import org.cidadeape.monitoraorcamento.domain.OrgaoUseCase
import org.cidadeape.monitoraorcamento.domain.model.TotalDespesas
import org.cidadeape.monitoraorcamento.presentation.BaseViewModel

class FundurbViewModel(
    ano: Ano,
    private val orgaoUseCase: OrgaoUseCase = OrgaoUseCase(ano = ano.nome, codOrgao = COD_ORGAO_FUNDURB)
): BaseViewModel() {

    val stateTotalDespesas: MutableStateFlow<LoadingState<TotalDespesas>> = MutableStateFlow(LoadingState.NotStarted())

    val stateListProjetosAtividadesRowState: MutableStateFlow<LoadingState<List<ProjetoAtividadeRowState>>> = MutableStateFlow(LoadingState.NotStarted())

    val stateListEmpenho: MutableStateFlow<LoadingState<List<Empenho>>> = MutableStateFlow(LoadingState.NotStarted())

    fun initialize() {

        Logger.d(TAG, "initialize()")
//        cancel()

//        if (stateTotalDespesas.value !is LoadingState.Success)
//            loadTotalDespesasFundurb()
//
//        if (stateListProjetosAtividadesRowState.value !is LoadingState.Success)
//            loadProjetosAtividadesComDespesas()
//
//        if (stateListEmpenho.value !is LoadingState.Success)
//            loadListaEmpenhos()
    }

    private fun loadListaEmpenhos() = launchCoroutine {
        Logger.i(TAG, "loadListaEmpenho started")
        stateListEmpenho.value = LoadingState.Loading()
        try {
            stateListEmpenho.value = LoadingState.Success(orgaoUseCase.getEmpenhos())
            Logger.i(TAG, "loadListaEmpenho successful")
        } catch (e: Exception) {
            e.printStackTrace()
            stateListEmpenho.value = LoadingState.Failure("Erro ao carregar lista de empenhos Fundurb.")
            Logger.e(TAG, "loadListaEmpenho failed with message: ${e.message}")
        }
    }

    private fun loadTotalDespesasFundurb() = launchCoroutine {
        Logger.i(TAG, "loadTotalDespesasFundurb started")
        stateTotalDespesas.value = LoadingState.Loading()
        try {
            stateTotalDespesas.value = LoadingState.Success(orgaoUseCase.getTotalDespesas())
            Logger.i(TAG, "loadTotalDespesasFundurb successful")
        } catch (e: Exception) {
            e.printStackTrace()
            stateTotalDespesas.value =
                LoadingState.Failure("Erro ao carregar totais")

            Logger.e(TAG, "loadTotalDespesasFundurb failed with message: ${e.message}")
        }
    }

    private fun loadProjetosAtividadesComDespesas() = launchCoroutine {
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
        Logger.i(TAG, "loadProjetosAtividades started")
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
            Logger.i(TAG, "loadProjetosAtividades successful")
        } catch (e: Exception) {
            e.printStackTrace()
            stateListProjetosAtividadesRowState.value = LoadingState.Failure(
                "Erro ao carregar projetos/atividades"
            )
            Logger.e(TAG, "loadProjetosAtividades failed with message: ${e.message}")
        }
    }

    private suspend fun loadTotalDespesasProjetosAtividades() {

        val listProjAtivRowState = stateListProjetosAtividadesRowState.value
                as? LoadingState.Success ?: return

        Logger.i(TAG, "loadTotalDespesasProjetosAtividades started")

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
        Logger.i(TAG, "loadTotalDespesasProjetosAtividades finished")
    }

    companion object {
        private const val TAG = "FundurbViewModel"
        private const val COD_ORGAO_FUNDURB = 98
    }
}
