package org.cidadeape.monitoraorcamento.common

import androidx.compose.runtime.Composable
import kotlinx.browser.window
import org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag

@Composable
actual fun ShowMessage(message: String) {
    window.alert(message)
}

@Composable
actual fun OpenInNewWindow(url: String) {
    window.open(url, "_blank")
}

@Composable
actual fun DownloadCsv(fileName: String, listaEmpenhos: List<Empenho>) {
    val csvDataBuilder = StringBuilder()

    val csvSeparator = ";"
    // CSV Headers
    csvDataBuilder.append("codProjetoAtividade")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("txDescricaoProjetoAtividade")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("codEmpresa")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("nomEmpresa")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("codEmpenho")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("anoEmpenho")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("mesEmpenho")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("datEmpenho")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("codProcesso")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("numCpfCnpj")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("txtRazaoSocial")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("numContrato")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("anoContrato")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("codCategoria")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("txDescricaoCategoriaEconomica")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("codGrupo")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("txDescricaoGrupoDespesa")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("codFonteRecurso")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("txDescricaoFonteRecurso")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("codOrgao")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("txDescricaoOrgao")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("codUnidade")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("txDescricaoUnidade")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("codElemento")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("txDescricaoElemento")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("codFuncao")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("txDescricaoFuncao")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("codSubFuncao")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("txDescricaoSubFuncao")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("codItemDespesa")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("txDescricaoItemDespesa")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("anexos")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("valTotalEmpenhado")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("valAnuladoEmpenho")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("valEmpenhadoLiquido")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("valLiquidado")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("valPagoExercicio")
    csvDataBuilder.append(csvSeparator)
    csvDataBuilder.append("valPagoRestos")
    csvDataBuilder.append("\n")

    for (i in listaEmpenhos.indices) {
        val empenho = listaEmpenhos[i]

        val anexoDesc = empenho.anexos?.joinToString(" | ") {
            "${it.descricaoAnexo} - ${it.qtdeAnexo} ${it.siglaUnidadeMedida} - " +
                    "Valor unitário: ${Util.formatToCurrency(it.valorUnitarioAnexo)}"
        }?.replace("\n","") ?: " "

        val values = arrayOf(
            empenho.codProjetoAtividade,
            empenho.txDescricaoProjetoAtividade,
            empenho.codEmpresa,
            empenho.nomEmpresa,
            empenho.codEmpenho,
            empenho.anoEmpenho,
            empenho.mesEmpenho,
            empenho.datEmpenho,
            empenho.codProcesso,
            empenho.numCpfCnpj,
            empenho.txtRazaoSocial,
            empenho.numContrato,
            empenho.anoContrato,
            empenho.codCategoria,
            empenho.txDescricaoCategoriaEconomica,
            empenho.codGrupo,
            empenho.txDescricaoGrupoDespesa,
            empenho.codFonteRecurso,
            empenho.txDescricaoFonteRecurso,
            empenho.codOrgao,
            empenho.txDescricaoOrgao,
            empenho.codUnidade,
            empenho.txDescricaoUnidade,
            empenho.codElemento,
            empenho.txDescricaoElemento,
            empenho.codFuncao,
            empenho.txDescricaoFuncao,
            empenho.codSubFuncao,
            empenho.txDescricaoSubFuncao,
            empenho.codItemDespesa,
            empenho.txDescricaoItemDespesa,
            anexoDesc,
            empenho.valTotalEmpenhado.formatToBrasil(),
            empenho.valAnuladoEmpenho.formatToBrasil(),
            empenho.valEmpenhadoLiquido.formatToBrasil(),
            empenho.valLiquidado.formatToBrasil(),
            empenho.valPagoExercicio.formatToBrasil(),
            empenho.valPagoRestos.formatToBrasil()
        )
        csvDataBuilder.append(values.joinToString(csvSeparator)).append("\n")
    }
    val jsArray = JsArray<JsAny?>()
    jsArray[0] = csvDataBuilder.toString().toJsString()
    val blob = Blob(jsArray, BlobPropertyBag("text/csv"))
    val url = URL.createObjectURL(blob)
    downloadFile(url, "$fileName.csv")
}

fun downloadFile(url: String, fileName: String) {
    js(
        "const a = document.createElement('a');" +
                "a.href = url;" +
                "a.download = fileName;" +
                " a.click();"
    )
}