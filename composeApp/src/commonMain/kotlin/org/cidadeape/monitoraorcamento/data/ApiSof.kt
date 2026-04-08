package org.cidadeape.monitoraorcamento.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.parameters
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import org.cidadeape.monitoraorcamento.data.model.CategoriaDespesa
import org.cidadeape.monitoraorcamento.data.model.empenhos.EmpenhoResponse
import org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetosAtividadesResponse
import org.cidadeape.monitoraorcamento.data.model.TokenResponse
import org.cidadeape.monitoraorcamento.data.model.despesa.DespesaResponse
import org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho

class ApiSof(override var ano: String) : IApiSof {

    override var mesDefault: String = "12"

    private val https_protocol = URLProtocol.HTTPS
    private val baseUrl = "gateway.apilib.prefeitura.sp.gov.br/sf/sof/v4"
    private val endpointEmpenhos = "empenhos"
    private val endpointDespesas = "despesas"
    private val endpointProjetosAtividades = "projetosAtividades"

    private val auth_update_token = "Basic M2J0MWlvNkM2SHBhcWthakdTanRmN2NmNjVZYTpPamkwMXluQ21WYUw0QUZvVkJvZmVYeFIxbnNh"
    private var token = "264b6df8-bfb3-373d-be9b-169a944ac893"
    private var tokenTimeout: Long = Clock.System.now().toEpochMilliseconds()

    init {
        ano.toInt()
    }

    private fun hasTokenTimedOut(): Boolean {
        val now = Clock.System.now().toEpochMilliseconds()
        return (tokenTimeout <= now)
    }

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 10000
            socketTimeoutMillis = 10000
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }

    private suspend fun updateAuthToken() {
        if (!hasTokenTimedOut()) return
        val response = client.submitForm(
            url = "https://gateway.apilib.prefeitura.sp.gov.br/token",
            formParameters = parameters {
                append("grant_type", "client_credentials")
            }
        ) {
            header(HttpHeaders.Authorization, auth_update_token)
        }

        val tokenResponse: TokenResponse = response.body()

        token = tokenResponse.accessToken
        tokenTimeout = Clock.System.now().toEpochMilliseconds() + tokenResponse.expiresIn
    }

    override suspend fun getEmpenhos(
        ano: String,
        mes: String,
        codProjetoAtividade: String?,
        codOrgao: Int?,
        codFonteRecurso: String?,
        codReferencia: String?,
        codDestinacaoRecurso: String?,
        codVinculacaoRecurso: String?,
    ): List<Empenho>  {
        val response = ArrayList<Empenho>()
        var pagina = 1
        do {
            val empenhoResponse = getEmpenhos(
                ano,
                mes,
                codProjetoAtividade,
                codOrgao,
                codFonteRecurso,
                codReferencia,
                codDestinacaoRecurso,
                codVinculacaoRecurso,
                pagina
            )
            response.addAll(empenhoResponse.lstEmpenhos)
            pagina++
        } while (empenhoResponse.metaDados.qtdPaginas >= pagina)

        return response
    }

    private suspend fun getEmpenhos(
        ano: String,
        mes: String,
        codProjetoAtividade: String?,
        codOrgao: Int?,
        codFonteRecurso: String?,
        codReferencia: String?,
        codDestinacaoRecurso: String?,
        codVinculacaoRecurso: String?,
        pagina: Int
    ): EmpenhoResponse {
        val response = client.get() {
            header(HttpHeaders.Authorization, "Bearer $token")
            url {
                protocol = https_protocol
                host = baseUrl
                path(endpointEmpenhos)
                parameters.append("anoEmpenho", ano)
                parameters.append("mesEmpenho", mes)
                parameters.append("numPagina", pagina.toString())
                codProjetoAtividade?.let { parameters.append("codProjetoAtividade", it) }
                codOrgao?.let { parameters.append("codOrgao", it.toString()) }
                codFonteRecurso?.let { parameters.append("codFonteRecurso", it) }
                codReferencia?.let { parameters.append("codReferencia", it) }
                codDestinacaoRecurso?.let { parameters.append("codDestinacaoRecurso", it) }
                codVinculacaoRecurso?.let { parameters.append("codVinculacaoRecurso", it) }
            }
        }
        val empenhoResponse = response.body<EmpenhoResponse>()

        return empenhoResponse
    }

    override suspend fun getDespesa(
        ano: String,
        mes: String,
        codProjetoAtividade: String?,
        codOrgao: Int?,
        categoriaDespesa: CategoriaDespesa?
    ): DespesaResponse {
        val response = client.get() {
            header(HttpHeaders.Authorization, "Bearer $token")
            url {
                protocol = https_protocol
                host = baseUrl
                path(endpointDespesas)
                parameters.append("anoDotacao", ano)
                parameters.append("mesDotacao", mes)
                codProjetoAtividade?.let {
                    parameters.append("codProjetoAtividade", it)
                }
                codOrgao?.let {
                    parameters.append("codOrgao", it.toString())
                }
                categoriaDespesa?.let {
                    parameters.append("codCategoria", it.codigo)
                }
            }
        }
        val despesaResponse = response.body<DespesaResponse>()

        return despesaResponse
    }

    override suspend fun getProjetoAtividade(ano: String, codProjetoAtividade: String?): ProjetosAtividadesResponse {
        val response = client.get() {
            header(HttpHeaders.Authorization, "Bearer $token")
            url {
                protocol = https_protocol
                host = baseUrl
                path(endpointProjetosAtividades)
                parameters.append("anoExercicio", ano)
                if (codProjetoAtividade != null)
                    parameters.append("codProjetoAtividade", codProjetoAtividade)
            }
        }

        return response.body()
    }

}
