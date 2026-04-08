package org.cidadeape.monitoraorcamento.presentation

import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.joinAll
import org.cidadeape.monitoraorcamento.common.LoadingState
import org.cidadeape.monitoraorcamento.common.Logger
import org.cidadeape.monitoraorcamento.data.Ano
import org.cidadeape.monitoraorcamento.domain.ProjetoAtividadeUseCase
import org.cidadeape.monitoraorcamento.presentation.screen.GrupoState
import org.cidadeape.monitoraorcamento.presentation.screen.ProjetoAtividadeRowState

class HomeViewModel(
    ano: Ano,
    private val projetoAtividadeUseCase: ProjetoAtividadeUseCase = ProjetoAtividadeUseCase(ano)
): BaseViewModel() {

    private val grupoAsfalto = GrupoState(
        id = "asfalto",
        nome = "Asfalto",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeRowState("1137"),
            ProjetoAtividadeRowState("2340")
        ))

    private val grupoObrasViarias = GrupoState(
        id = "obras_viarias",
        nome = "Obras Viárias",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeRowState("5100"),
            ProjetoAtividadeRowState("5105")
        ))

    private val grupoCalcadas = GrupoState(
        id = "calcadas",
        nome = "Calçadas",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeRowState("1169")
        ))

    private val grupoBicicleta = GrupoState(
        id = "ciclovias_e_ciclofaixas",
        nome = "Ciclovias e Ciclofaixas",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeRowState("1097"),
            ProjetoAtividadeRowState("1098"),
            ProjetoAtividadeRowState("2093"),
            ProjetoAtividadeRowState("2098")
        ))

    private val grupoSegurancaViaria = GrupoState(
        id = "seguranca_viaria",
        nome = "Segurança Viária",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeRowState("3757"),
            ProjetoAtividadeRowState("3664"),
            ProjetoAtividadeRowState("4656"),
            ProjetoAtividadeRowState("4703")
        ))

    private val grupoOnibusCorredores = GrupoState(
        id = "onibus_corredores",
        nome = "Ônibus - Corredores e Faixas",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeRowState("1099"),
            ProjetoAtividadeRowState("1100"),
            ProjetoAtividadeRowState("2099"),
            ProjetoAtividadeRowState("5391"),
            ProjetoAtividadeRowState("5392"),
//            ProjetoAtividadeState("5393"),
        ))

    private val grupoOnibusTerminais = GrupoState(
        id = "onibus_terminais",
        nome = "Ônibus - Terminais",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeRowState("1095"),
            ProjetoAtividadeRowState("1096"),
            ProjetoAtividadeRowState("2096"),
//            ProjetoAtividadeState("3749"),
            ProjetoAtividadeRowState("4663"),
        ))

    private val grupoOnibusFrota = GrupoState(
        id = "onibus_frota",
        nome = "Ônibus - Frota",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeRowState("1800"),
//            ProjetoAtividadeState("3800"),
//            ProjetoAtividadeState("3801")
        ))

    private val grupoOnibusCompensacoes = GrupoState(
        id = "onibus_compensacao_tarifaria",
        nome = "Ônibus - Compensação tarifária",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeRowState("4701")
        ))

    private val grupoMeioAmbiente = GrupoState(
        id = "onibus_meio_ambiente",
        nome = "Meio ambiente",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeRowState("1702"),
            ProjetoAtividadeRowState("1703"),
            ProjetoAtividadeRowState("1704"),
            ProjetoAtividadeRowState("1705"),
            ProjetoAtividadeRowState("1706"),
            ProjetoAtividadeRowState("2324"),
            ProjetoAtividadeRowState("2386"),
            ProjetoAtividadeRowState("2702"),
            ProjetoAtividadeRowState("2703"),
            ProjetoAtividadeRowState("2704"),
            ProjetoAtividadeRowState("2705"),
            ProjetoAtividadeRowState("3355"),
            ProjetoAtividadeRowState("5087"),
            ProjetoAtividadeRowState("6007"),
            ProjetoAtividadeRowState("6009"),
            ProjetoAtividadeRowState("6010"),
            ProjetoAtividadeRowState("6651"),
            ProjetoAtividadeRowState("6655"),
            ProjetoAtividadeRowState("6659"),
            ProjetoAtividadeRowState("6660"),
            ProjetoAtividadeRowState("6663"),
            ProjetoAtividadeRowState("6669"),
            ProjetoAtividadeRowState("6681"),
            ProjetoAtividadeRowState("6682"),
            ProjetoAtividadeRowState("6686"),
            ProjetoAtividadeRowState("7117"),
            ProjetoAtividadeRowState("7127")
        ))

    val listaGrupos = mutableStateListOf(
        grupoCalcadas,
        grupoSegurancaViaria,
        grupoAsfalto,
        grupoBicicleta,
        grupoOnibusCorredores,
        grupoOnibusTerminais,
        grupoOnibusFrota,
        grupoOnibusCompensacoes,
        grupoObrasViarias,
        grupoMeioAmbiente
    )

    init {
        Logger.i("App", "HomeScreen initialize()")
        cancel()
        loadAllGrupos()
    }

    private fun loadAllGrupos() {
        for (grupo in listaGrupos) {
            Logger.i("App", "HomeScreenViewModel loadGrupo: ${grupo.nome}")
            loadGrupo(grupo)
        }
    }

    fun loadGrupo(grupoState: GrupoState) = launchCoroutine {
        grupoState.refreshing.value = true
        grupoState.statePagoTotal.value = LoadingState.Loading()
        grupoState.stateEmpenhadoLiquidoTotal.value = LoadingState.Loading()

        Logger.i("App", "HomeScreenViewModel loadGrupo: ${grupoState.nome} Loading")

        val jobs = grupoState.listaProjetosAtividades.map {
            launchCoroutine {
                projetoAtividadeUseCase.loadProjetoAtividadeComTotalDespesas(it)
            }
        }
        jobs.joinAll()

        var pagoGrupo = 0.0
        var empenhadoLiquidoGrupo = 0.0

        var failedDespesasTotal = false

        grupoState.listaProjetosAtividades.forEach {

            failedDespesasTotal = when (val state = it.stateTotalDespesas.value) {
                is LoadingState.Success -> {
                    empenhadoLiquidoGrupo += state.response.empenhadoLiquido
                    pagoGrupo += state.response.pago
                    false
                }
                else -> true
            }
        }

        if (failedDespesasTotal) {
            grupoState.stateEmpenhadoLiquidoTotal.value = LoadingState.Failure("Erro ao carregar despesas totais")
            grupoState.statePagoTotal.value = LoadingState.Failure("Erro ao carregar despesas totais")
        } else {
            grupoState.stateEmpenhadoLiquidoTotal.value = LoadingState.Success(empenhadoLiquidoGrupo)
            grupoState.statePagoTotal.value = LoadingState.Success(pagoGrupo)
        }

        grupoState.refreshing.value = false
    }

}
