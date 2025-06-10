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

    // CSV Headers
    csvDataBuilder.append("codEmpresa")
    csvDataBuilder.append(",")
    csvDataBuilder.append("nomEmpresa")
    csvDataBuilder.append(",")
    csvDataBuilder.append("codEmpenho")
    csvDataBuilder.append(",")
    csvDataBuilder.append("anoEmpenho")
    csvDataBuilder.append(",")
    csvDataBuilder.append("mesEmpenho")
    csvDataBuilder.append(",")
    csvDataBuilder.append("datEmpenho")
    csvDataBuilder.append(",")
    csvDataBuilder.append("codProcesso")
    csvDataBuilder.append(",")
    csvDataBuilder.append("numCpfCnpj")
    csvDataBuilder.append(",")
    csvDataBuilder.append("txtRazaoSocial")
    csvDataBuilder.append(",")
    csvDataBuilder.append("numContrato")
    csvDataBuilder.append(",")
    csvDataBuilder.append("anoContrato")
    csvDataBuilder.append(",")
    csvDataBuilder.append("codProjetoAtividade")
    csvDataBuilder.append(",")
    csvDataBuilder.append("txDescricaoProjetoAtividade")
    csvDataBuilder.append(",")
    csvDataBuilder.append("codCategoria")
    csvDataBuilder.append(",")
    csvDataBuilder.append("txDescricaoCategoriaEconomica")
    csvDataBuilder.append(",")
    csvDataBuilder.append("codGrupo")
    csvDataBuilder.append(",")
    csvDataBuilder.append("txDescricaoGrupoDespesa")
    csvDataBuilder.append(",")
    csvDataBuilder.append("codFonteRecurso")
    csvDataBuilder.append(",")
    csvDataBuilder.append("txDescricaoFonteRecurso")
    csvDataBuilder.append(",")
    csvDataBuilder.append("codOrgao")
    csvDataBuilder.append(",")
    csvDataBuilder.append("txDescricaoOrgao")
    csvDataBuilder.append(",")
    csvDataBuilder.append("codUnidade")
    csvDataBuilder.append(",")
    csvDataBuilder.append("txDescricaoUnidade")
    csvDataBuilder.append(",")
    csvDataBuilder.append("codElemento")
    csvDataBuilder.append(",")
    csvDataBuilder.append("txDescricaoElemento")
    csvDataBuilder.append(",")
    csvDataBuilder.append("codFuncao")
    csvDataBuilder.append(",")
    csvDataBuilder.append("txDescricaoFuncao")
    csvDataBuilder.append(",")
    csvDataBuilder.append("codSubFuncao")
    csvDataBuilder.append(",")
    csvDataBuilder.append("txDescricaoSubFuncao")
    csvDataBuilder.append(",")
    csvDataBuilder.append("codItemDespesa")
    csvDataBuilder.append(",")
    csvDataBuilder.append("txDescricaoItemDespesa")
    csvDataBuilder.append(",")
    csvDataBuilder.append("valTotalEmpenhado")
    csvDataBuilder.append(",")
    csvDataBuilder.append("valAnuladoEmpenho")
    csvDataBuilder.append(",")
    csvDataBuilder.append("valEmpenhadoLiquido")
    csvDataBuilder.append(",")
    csvDataBuilder.append("valLiquidado")
    csvDataBuilder.append(",")
    csvDataBuilder.append("valPagoExercicio")
    csvDataBuilder.append(",")
    csvDataBuilder.append("valPagoRestos")
    csvDataBuilder.append("\n")

    for (i in listaEmpenhos.indices) {
        val empenho = listaEmpenhos[i]
        val values = arrayOf(
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
            empenho.codProjetoAtividade,
            empenho.txDescricaoProjetoAtividade,
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
            empenho.valTotalEmpenhado.formatToBrasil(),
            empenho.valAnuladoEmpenho.formatToBrasil(),
            empenho.valEmpenhadoLiquido.formatToBrasil(),
            empenho.valLiquidado.formatToBrasil(),
            empenho.valPagoExercicio.formatToBrasil(),
            empenho.valPagoRestos.formatToBrasil()
        )
        csvDataBuilder.append(values.joinToString(",")).append("\n")
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