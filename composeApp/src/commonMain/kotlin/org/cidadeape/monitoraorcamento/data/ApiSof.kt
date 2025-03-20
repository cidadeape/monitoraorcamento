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
import org.cidadeape.monitoraorcamento.data.model.empenhos.EmpenhoResponse
import org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetosAtividadesResponse
import org.cidadeape.monitoraorcamento.data.model.TokenResponse

class ApiSof: IApiSof {

    private val https_protocol = URLProtocol.HTTPS
    private val baseUrl = "gateway.apilib.prefeitura.sp.gov.br/sf/sof/v4"
    private val endpointEmpenhos = "empenhos"
    private val endpointProjetosAtividades = "projetosAtividades"

    private val auth_update_token = "Basic eV9XaVBpc2U3TTdSOGVtZURQa1hUbEk5YXA0YTpJRUV0OUJIREhWbDMyeWRhbmdVWFFYSmVGM29h"
    private var token = "ecf4080a-52ab-37f7-b487-1b4d026246e5"
    private var tokenTimeout: Long = Clock.System.now().toEpochMilliseconds()

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
            requestTimeoutMillis = 5000
            socketTimeoutMillis = 5000
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

    override suspend fun getEmpenhos(ano: String, mes: String, codProjetoAtividade: String): EmpenhoResponse {
//        updateAuthToken()
        val response = client.get() {
            header(HttpHeaders.Authorization, "Bearer $token")
            url {
                protocol = https_protocol
                host = baseUrl
                path(endpointEmpenhos)
                parameters.append("anoEmpenho", ano)
                parameters.append("mesEmpenho", mes)
                parameters.append("codProjetoAtividade", codProjetoAtividade)
            }
        }

        return response.body()
    }

    override suspend fun getProjetoAtividade(ano: String, codProjetoAtividade: String?): ProjetosAtividadesResponse {
//        updateAuthToken()
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
