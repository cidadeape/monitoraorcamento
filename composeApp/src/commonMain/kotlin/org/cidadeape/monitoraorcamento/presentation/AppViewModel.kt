package org.cidadeape.monitoraorcamento.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.cidadeape.monitoraorcamento.common.LoadingState
import org.cidadeape.monitoraorcamento.data.ApiSof
import org.cidadeape.monitoraorcamento.data.IApiSof
import org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho
import org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetoAtividade
import org.cidadeape.monitoraorcamento.domain.model.TotalDespesas
import org.cidadeape.monitoraorcamento.presentation.screen.GrupoState
import org.cidadeape.monitoraorcamento.presentation.screen.ProjetoAtividadeRowState
import org.cidadeape.monitoraorcamento.presentation.screen.Screen
import kotlin.reflect.KClass

class AppViewModel(
    private val sofApi: IApiSof = ApiSof()
): ViewModel() {

    private val grupoAsfalto = GrupoState(
        nome = "Asfalto",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeRowState("1137"),
            ProjetoAtividadeRowState("2340")
        ))

    private val grupoObrasViarias = GrupoState(
        nome = "Obras Viárias",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeRowState("5100"),
            ProjetoAtividadeRowState("5105")
        ))

    private val grupoCalcadas = GrupoState(
        nome = "Calçadas",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeRowState("1169")
        ))

    private val grupoBicicleta = GrupoState(
        nome = "Ciclovias e Ciclofaixas",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeRowState("1097"),
            ProjetoAtividadeRowState("1098"),
            ProjetoAtividadeRowState("2093"),
            ProjetoAtividadeRowState("2098")
        ))

    private val grupoSegurancaViaria = GrupoState(
        nome = "Segurança Viária",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeRowState("3757"),
            ProjetoAtividadeRowState("3664"),
            ProjetoAtividadeRowState("4656"),
            ProjetoAtividadeRowState("4703")
        ))

    private val grupoOnibusCorredores = GrupoState(
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
        nome = "Ônibus - Terminais",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeRowState("1095"),
            ProjetoAtividadeRowState("1096"),
            ProjetoAtividadeRowState("2096"),
//            ProjetoAtividadeState("3749"),
            ProjetoAtividadeRowState("4663"),
        ))

    private val grupoOnibusFrota = GrupoState(
        nome = "Ônibus - Frota",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeRowState("1800"),
//            ProjetoAtividadeState("3800"),
//            ProjetoAtividadeState("3801")
        ))

    private val grupoOnibusCompensacoes = GrupoState(
        nome = "Ônibus - Compensação tarifária",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeRowState("4701")
        ))

    private val grupoMeioAmbiente = GrupoState(
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

    val listaGrupos = listOf(
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

    val listaCustomizadaProjetosAtividades = mutableStateListOf<ProjetoAtividadeRowState>()

    val fullList: MutableStateFlow<LoadingState<List<ProjetoAtividade>>> = MutableStateFlow(
        LoadingState.NotStarted())

    init {
        load()
    }

    fun load() {
        loadAllGrupos()

        launchCoroutine {
            loadSearchList()
        }
    }

    private fun loadAllGrupos() {
        for (grupo in listaGrupos) {
            loadGrupo(grupo)
        }
    }

    fun loadGrupo(grupoState: GrupoState) = launchCoroutine {
        grupoState.refreshing.value = true
        grupoState.statePagoTotal.value = LoadingState.Loading()
        grupoState.stateEmpenhadoLiquidoTotal.value = LoadingState.Loading()

        val jobs = grupoState.listaProjetosAtividades.map {
            launchCoroutine {
                loadProjetoAtividadeComTotalDespesas(it)
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

    fun removerDaListaCustomizada(projetoAtividadeRowState: ProjetoAtividadeRowState) {

        listaCustomizadaProjetosAtividades.remove(projetoAtividadeRowState)
    }

    fun adicionarAListaCustomizada(projetoAtividade: ProjetoAtividade) {

        if (listaCustomizadaProjetosAtividades
            .map { it.codigo }
            .contains(projetoAtividade.codProjetoAtividade)
        ) return

        val projetoAtividadeRowState = ProjetoAtividadeRowState(
            codigo = projetoAtividade.codProjetoAtividade,
            stateProjeto = MutableStateFlow(LoadingState.Success(projetoAtividade))
        )

        listaCustomizadaProjetosAtividades.add(0, projetoAtividadeRowState)
        launchCoroutine {
            loadProjetoAtividadeComTotalDespesas(projetoAtividadeRowState)
        }
    }

    private suspend fun loadSearchList() {
        fullList.value = LoadingState.Loading()
        fullList.value =
            try {
                val projetosAtividades = sofApi.getProjetoAtividade(codProjetoAtividade = null).lstProjetosAtividades
                LoadingState.Success(projetosAtividades)
            } catch (e: Exception) {
                e.printStackTrace()
                LoadingState.Failure("Erro ao carregar projeto/atividade: ${e::class.simpleName}")
            }
    }

    private suspend fun loadProjetoAtividadeComTotalDespesas(projetoAtividadeRowState: ProjetoAtividadeRowState) {
        if (projetoAtividadeRowState.stateProjeto.value !is LoadingState.Success) loadProjetoNome(projetoAtividadeRowState)

        loadTotalDespesas(projetoAtividadeRowState)
    }

    private suspend fun loadProjetoNome(projetoAtividadeRowState: ProjetoAtividadeRowState) {

        projetoAtividadeRowState.stateProjeto.value = LoadingState.Loading()
        projetoAtividadeRowState.stateProjeto.value = try {
            LoadingState.Loading<String>()
            val nome = sofApi
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

            val despesasResponse = sofApi
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


    private fun launchCoroutine(block: suspend  () -> Unit): Job =
        viewModelScope.launch(Dispatchers.Default) {
            block.invoke()
        }


    @Suppress("UNCHECKED_CAST")
    class Factory: ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
            return AppViewModel() as T
        }
    }

    companion object {

        var screenState: MutableState<Screen> = mutableStateOf(Screen.Home())

        fun navigateToHome() {
            screenState.value = Screen.Home()
        }

        fun navigateToFundurb() {
            screenState.value = Screen.Fundurb()
        }

        fun navigateToBuscaProjeto() {
            screenState.value = Screen.BuscaProjeto(::navigateToHome)
        }

        fun navigateToBuscaEmpenho() {
            screenState.value = Screen.BuscaEmpenho(::navigateToHome)
        }

        fun navigateToProjetoAtividade(projetoAtividade: ProjetoAtividade, navigateUp: (() -> Unit)?) {
            screenState.value = Screen.ProjetoAtividade(
                projetoAtividade,
                true,
                navigateUp ?: ::navigateToHome
            )
        }

        fun navigateToEmpenho(empenho: Empenho, navigateUp: (() -> Unit)?) {
            screenState.value = Screen.Empenho(
                empenho,
                true,
                navigateUp ?: ::navigateToHome
            )
        }

        fun navigateToGrupo(grupoState: GrupoState) {
            screenState.value = Screen.Grupo(
                grupoState,
                true,
                ::navigateToHome
            )
        }
    }

}