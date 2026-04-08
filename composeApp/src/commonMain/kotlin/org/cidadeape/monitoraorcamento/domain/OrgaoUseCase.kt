package org.cidadeape.monitoraorcamento.domain

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.cidadeape.monitoraorcamento.data.ApiSof
import org.cidadeape.monitoraorcamento.data.IApiSof
import org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho
import org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetoAtividade
import org.cidadeape.monitoraorcamento.domain.model.TotalDespesas

class OrgaoUseCase(
    private val ano: String,
    private val apiSof: IApiSof = ApiSof(ano),
    private val codOrgao: Int
) {

    private var totalDespesas: TotalDespesas? = null
    private var empenhosList: List<Empenho>? = null

    @Throws(Exception::class)
    suspend fun getProjetosAtividades(): List<ProjetoAtividade> {
        val empenhosList = getEmpenhos()

        return empenhosList
            .map { ProjetoAtividade(it.codProjetoAtividade, it.txDescricaoProjetoAtividade) }
            .distinct()
    }

    @Throws(Exception::class)
    suspend fun getEmpenhos(): List<Empenho> = Mutex().withLock {
        return empenhosList ?: apiSof.getEmpenhos(codOrgao = codOrgao)
            .also {
                this.empenhosList = it
            }
    }

    @Throws(Exception::class)
    suspend fun getTotalDespesas(): TotalDespesas {
        return totalDespesas
            ?: apiSof.getDespesa(codOrgao = codOrgao)
                .let { TotalDespesas.fromDespesasResponse(it) }
                .also {
                    this.totalDespesas = it
                }
    }

    @Throws(Exception::class)
    suspend fun getTotalDespesasProjAtividade(codProjAtividade: String): TotalDespesas {
        return apiSof.getDespesa(
            codProjetoAtividade = codProjAtividade,
            codOrgao = codOrgao
        ).let { TotalDespesas.fromDespesasResponse(it) }
    }
}
