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
import org.cidadeape.monitoraorcamento.data.model.CategoriaDespesa
import org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho
import org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetoAtividade
import org.cidadeape.monitoraorcamento.presentation.grupo.GrupoState
import org.cidadeape.monitoraorcamento.presentation.grupo.ProjetoAtividadeState
import org.cidadeape.monitoraorcamento.presentation.grupo.TotalDespesas
import kotlin.reflect.KClass

class AppViewModel(
    private val sofApi: IApiSof = ApiSof()
): ViewModel() {

    var screenState: MutableState<Screen> = mutableStateOf(Screen.Home())

    private val grupoAsfalto = GrupoState(
        nome = "Asfalto",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeState("1137"),
            ProjetoAtividadeState("2340")
        ))

    private val grupoCalcadas = GrupoState(
        nome = "Calçadas",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeState("1169")
        ))

    private val grupoBicicleta = GrupoState(
        nome = "Ciclovias e Ciclofaixas",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeState("1097"),
            ProjetoAtividadeState("1098"),
            ProjetoAtividadeState("2093"),
            ProjetoAtividadeState("2098")
        ))

    private val grupoSegurancaViaria = GrupoState(
        nome = "Segurança Viária",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeState("3757")
        ))

    private val grupoOnibusCorredores = GrupoState(
        nome = "Ônibus - Corredores e Faixas",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeState("1099"),
            ProjetoAtividadeState("1100"),
            ProjetoAtividadeState("2099"),
            ProjetoAtividadeState("5391"),
            ProjetoAtividadeState("5392"),
//            ProjetoAtividadeState("5393"),
        ))

    private val grupoOnibusTerminais = GrupoState(
        nome = "Ônibus - Terminais",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeState("1095"),
            ProjetoAtividadeState("1096"),
            ProjetoAtividadeState("2096"),
//            ProjetoAtividadeState("3749"),
            ProjetoAtividadeState("4663"),
        ))

    private val grupoOnibusFrota = GrupoState(
        nome = "Ônibus - Frota",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeState("1800"),
//            ProjetoAtividadeState("3800"),
//            ProjetoAtividadeState("3801")
        ))

    private val grupoOnibusCompensacoes = GrupoState(
        nome = "Ônibus - Compensação tarifária",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeState("4701")
        ))

    private val grupoMeioAmbiente = GrupoState(
        nome = "Meio ambiente",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeState("1702"),
            ProjetoAtividadeState("1703"),
            ProjetoAtividadeState("1704"),
            ProjetoAtividadeState("2324"),
            ProjetoAtividadeState("2386"),
            ProjetoAtividadeState("2702"),
            ProjetoAtividadeState("2703"),
            ProjetoAtividadeState("2704"),
            ProjetoAtividadeState("2705"),
            ProjetoAtividadeState("5087"),
            ProjetoAtividadeState("6651"),
            ProjetoAtividadeState("6655"),
            ProjetoAtividadeState("6659"),
            ProjetoAtividadeState("6660"),
            ProjetoAtividadeState("6669"),
            ProjetoAtividadeState("6681"),
            ProjetoAtividadeState("6682"),
            ProjetoAtividadeState("6686"),
            ProjetoAtividadeState("7117"),
            ProjetoAtividadeState("7127")
        ))

    val listaGrupos = listOf(
        grupoAsfalto,
        grupoCalcadas,
        grupoBicicleta,
        grupoSegurancaViaria,
        grupoOnibusCorredores,
        grupoOnibusTerminais,
        grupoOnibusFrota,
        grupoOnibusCompensacoes,
        grupoMeioAmbiente
    )

    val listaProjetosAtividades = mutableStateListOf<ProjetoAtividadeState>()

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
        grupoState.statePagoCapital.value = LoadingState.Loading()

        val jobs = grupoState.listaProjetosAtividades.map {
            launchCoroutine {
                loadProjetoAtividadeComTotalDespesas(it)
            }
        }
        jobs.joinAll()

        var pagoGrupo = 0.0
        var empenhadoLiquidoGrupo = 0.0
        var pagoCapitalGrupo = 0.0

        var failedDespesasTotal = false
        var failedPagoCapital = false

        grupoState.listaProjetosAtividades.forEach {

            failedDespesasTotal = when (val state = it.stateDespesasTotal.value) {
                is LoadingState.Success -> {
                    empenhadoLiquidoGrupo += state.response.empenhadoLiquido
                    pagoGrupo += state.response.pago
                    false
                }
                else -> true
            }

            failedPagoCapital = when (val state = it.stateDespesasCapital.value) {
                is LoadingState.Success -> {
                    pagoCapitalGrupo += state.response.pago
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

        if (failedPagoCapital) {
            grupoState.statePagoCapital.value = LoadingState.Failure("Erro ao carregar despesas de capital")
        } else {
            grupoState.statePagoCapital.value = LoadingState.Success(pagoCapitalGrupo)
        }

        grupoState.refreshing.value = false
    }

    fun removeFromList(projetoAtividadeState: ProjetoAtividadeState) {

        listaProjetosAtividades.remove(projetoAtividadeState)
    }

    fun addToList(projetoAtividade: ProjetoAtividade) {

        val projetoAtividadeState = ProjetoAtividadeState(
            codigo = projetoAtividade.codProjetoAtividade,
            stateProjeto = MutableStateFlow(LoadingState.Success(projetoAtividade))
        )

        if (listaProjetosAtividades.contains(projetoAtividadeState)) return

        listaProjetosAtividades.add(0, projetoAtividadeState)
        launchCoroutine {
            loadProjetoAtividadeComTotalDespesas(projetoAtividadeState)
        }
    }

    private suspend fun loadSearchList() {
        fullList.value = LoadingState.Loading()
        fullList.value =
            try {
                val projetosAtividades = sofApi.getProjetoAtividade("2025", null).lstProjetosAtividades
                LoadingState.Success(projetosAtividades)
            } catch (e: Exception) {
                e.printStackTrace()
                LoadingState.Failure("Erro ao carregar projeto/atividade: ${e::class.simpleName}")
            }
    }

    private suspend fun loadProjetoAtividadeComTotalDespesas(projetoAtividadeState: ProjetoAtividadeState) {
        if (projetoAtividadeState.stateProjeto.value !is LoadingState.Success) loadProjetoNome(projetoAtividadeState)

        loadTotalDespesas(projetoAtividadeState)
    }

    private suspend fun loadProjetoNome(projetoAtividadeState: ProjetoAtividadeState) {

        projetoAtividadeState.stateProjeto.value = LoadingState.Loading()
        projetoAtividadeState.stateProjeto.value = try {
            LoadingState.Loading<String>()
            val nome = sofApi
                .getProjetoAtividade("2025", projetoAtividadeState.codigo)
                .lstProjetosAtividades[0]
            LoadingState.Success(nome)
        } catch (e: Exception) {
            e.printStackTrace()
            LoadingState.Failure("Erro ao carregar projeto/atividade: ${e::class.simpleName}")
        }
    }

    private suspend fun loadTotalDespesas(projetoAtividadeState: ProjetoAtividadeState) {

        try {
            projetoAtividadeState.stateDespesasTotal.value = LoadingState.Loading()

            val despesasResponse = sofApi
                .getDespesas("2025", "12", projetoAtividadeState.codigo)

            val totalDespesas =
                if (despesasResponse.metaDados.txtStatus == "SEM REGISTROS") {
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

            projetoAtividadeState.stateDespesasTotal.value = LoadingState.Success(totalDespesas)
        } catch (e: Exception) {
            e.printStackTrace()
            projetoAtividadeState.stateDespesasTotal.value = LoadingState.Failure("Erro ao carregar empenhos: ${e::class.simpleName}")
        }

        try {
            projetoAtividadeState.stateDespesasCapital.value = LoadingState.Loading()
            val despesasResponse = sofApi
                .getDespesas("2025", "12", projetoAtividadeState.codigo, CategoriaDespesa.DESPESAS_CAPITAL)

            val totalDespesas =
                if (despesasResponse.metaDados.txtStatus == "SEM REGISTROS") {
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

            projetoAtividadeState.stateDespesasCapital.value = LoadingState.Success(totalDespesas)
        } catch (e: Exception) {
            e.printStackTrace()
            projetoAtividadeState.stateDespesasCapital.value = LoadingState.Failure("Erro ao carregar empenhos: ${e::class.simpleName}")
        }
    }


    private fun launchCoroutine(block: suspend  () -> Unit): Job =
        viewModelScope.launch(Dispatchers.Default) {
            block.invoke()
        }

    fun navigateToHome() {
        screenState.value = Screen.Home()
    }

    fun navigateToBusca() {
        screenState.value = Screen.Busca(::navigateToHome)
    }

    fun navigateToProjetoAtividade(projetoAtividade: ProjetoAtividade, navigateUp: (() -> Unit)?) {
        screenState.value = Screen.ProjetoAtividade(
            projetoAtividade,
            true,
            navigateUp ?: ::navigateToHome
        )
    }

    fun navigateToEmpenho(projetoAtividade: ProjetoAtividade, empenho: Empenho) {
        screenState.value = Screen.Empenho(
            empenho,
            true,
            { navigateToProjetoAtividade(projetoAtividade, null) }
        )
    }

    fun navigateToGrupo(grupoState: GrupoState) {
        screenState.value = Screen.Grupo(
            grupoState,
            true,
            ::navigateToHome
        )
    }

    @Suppress("UNCHECKED_CAST")
    class Factory: ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
            return AppViewModel() as T
        }
    }

}