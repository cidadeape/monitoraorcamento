package org.cidadeape.monitoraorcamento.domain.model

import org.cidadeape.monitoraorcamento.data.model.despesa.DespesaResponse

data class TotalDespesas(
    var orcadoInicial: Double,
    var orcadoAtualizado: Double,
    var empenhadoLiquido: Double,
    var pago: Double
) {
    companion object {
        fun fromDespesasResponse(despesasResponse: DespesaResponse): TotalDespesas {

            return if (despesasResponse.metaDados.txtStatus == "SEM REGISTROS") {
                TotalDespesas(0.0, 0.0, 0.0, 0.0)
            } else {
                val despesas = despesasResponse.lstDespesas[0]
                TotalDespesas(
                    empenhadoLiquido = despesas.valEmpenhadoLiquido,
                    pago = despesas.valPagoRestos + despesas.valPagoExercicio,
                    orcadoInicial = despesas.valOrcadoInicial,
                    orcadoAtualizado = despesas.valOrcadoAtualizado
                )
            }
        }
    }
}
