package org.cidadeape.monitoraorcamento.data.model.despesa

data class Despesa(
    val valLiquidado: Double,
    val valPagoRestos: Double,
    val valSuplementado: Double,
    val valPagoExercicio: Double,
    val valCanceladoReserva: Double,
    val valDescongelado: Double,
    val valReservado: Double,
    val valEmpenhadoLiquido: Double,
    val valTotalEmpenhado: Double,
    val valReservadoLiquido: Double,
    val valAnuladoEmpenho: Double,
    val valDisponivel: Double,
    val valOrcadoInicial: Double,
    val valCongelado: Double,
    val valReduzido: Double,
    val valOrcadoAtualizado: Double
)
