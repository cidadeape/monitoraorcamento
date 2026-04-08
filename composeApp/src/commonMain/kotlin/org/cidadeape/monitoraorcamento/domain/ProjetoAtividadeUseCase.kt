package org.cidadeape.monitoraorcamento.domain

import org.cidadeape.monitoraorcamento.common.LoadingState
import org.cidadeape.monitoraorcamento.data.Ano
import org.cidadeape.monitoraorcamento.data.ApiSof
import org.cidadeape.monitoraorcamento.data.IApiSof
import org.cidadeape.monitoraorcamento.domain.model.TotalDespesas
import org.cidadeape.monitoraorcamento.presentation.screen.ProjetoAtividadeRowState

class ProjetoAtividadeUseCase(
    ano: Ano,
    val apiSof: IApiSof = ApiSof(ano.nome)
) {

    suspend fun loadProjetoAtividadeComTotalDespesas(projetoAtividadeRowState: ProjetoAtividadeRowState) {
        if (projetoAtividadeRowState.stateProjeto.value !is LoadingState.Success) loadProjetoNome(projetoAtividadeRowState)

        loadTotalDespesas(projetoAtividadeRowState)
    }

    private suspend fun loadProjetoNome(projetoAtividadeRowState: ProjetoAtividadeRowState) {

        projetoAtividadeRowState.stateProjeto.value = LoadingState.Loading()
        projetoAtividadeRowState.stateProjeto.value = try {
            LoadingState.Loading<String>()
            val nome = apiSof
                .getProjetoAtividade(codProjetoAtividade = projetoAtividadeRowState.codigo)
                .lstProjetosAtividades[0]
            LoadingState.Success(nome)
        } catch (e: Exception) {
            e.printStackTrace()
            LoadingState.Failure("Erro ao carregar projeto/atividade: ${e::class.simpleName}")
        }
    }

    private suspend fun loadTotalDespesas(projetoAtividadeRowState: ProjetoAtividadeRowState) {

        try {
            projetoAtividadeRowState.stateTotalDespesas.value = LoadingState.Loading()

            val despesasResponse = apiSof
                .getDespesa(
                    codProjetoAtividade = projetoAtividadeRowState.codigo,
                    codOrgao = projetoAtividadeRowState.codOrgao
                )

            val totalDespesas = TotalDespesas.fromDespesasResponse(despesasResponse)

            projetoAtividadeRowState.stateTotalDespesas.value = LoadingState.Success(totalDespesas)
        } catch (e: Exception) {
            e.printStackTrace()
            projetoAtividadeRowState.stateTotalDespesas.value = LoadingState.Failure("Erro ao carregar despesas: ${e::class.simpleName}")
        }
    }
}