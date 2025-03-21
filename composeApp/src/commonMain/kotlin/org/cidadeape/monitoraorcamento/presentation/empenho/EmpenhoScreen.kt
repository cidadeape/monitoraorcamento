package org.cidadeape.monitoraorcamento.presentation.empenho

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.cidadeape.monitoraorcamento.common.AppColors
import org.cidadeape.monitoraorcamento.common.TextTitle
import org.cidadeape.monitoraorcamento.common.TextTitleValue
import org.cidadeape.monitoraorcamento.common.Util
import org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho
import org.cidadeape.monitoraorcamento.data.model.mockListEmpenhos
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun EmpenhoScreen(
    empenho: Empenho = mockListEmpenhos[0]
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(32.dp)
    ) {

        TextTitle("Empenho ${empenho.codEmpenho} / ${empenho.anoEmpenho}")
        Text("Data: ${empenho.datEmpenho}")
        Text(
            fontWeight = FontWeight.Bold,
            text = "Valor empenhado líquido: ${Util.formatToCurrency(empenho.valEmpenhadoLiquido)}"
        )
        TextTitleValue("Fonte do recurso", "${empenho.codFonteRecurso} - ${empenho.txDescricaoFonteRecurso}")

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            fontStyle = FontStyle.Italic,
            fontSize = 12.sp,
            text = "Projeto: ${empenho.txDescricaoProjetoAtividade}"
        )
        Text(
            fontStyle = FontStyle.Italic,
            fontSize = 12.sp,
            text = "Cód: ${empenho.codProjetoAtividade}"
        )

        BoxedColumn {
            TextTitleValue("Total empenhado", Util.formatToCurrency(empenho.valTotalEmpenhado))
            TextTitleValue("Anulado", Util.formatToCurrency(empenho.valAnuladoEmpenho))
            TextTitleValue("Empenhado Líquido", Util.formatToCurrency(empenho.valEmpenhadoLiquido))
            TextTitleValue("Pago (exercício)", Util.formatToCurrency(empenho.valPagoExercicio))
            TextTitleValue("Pago (restos)", Util.formatToCurrency(empenho.valPagoRestos))
            TextTitleValue("Liquidado", Util.formatToCurrency(empenho.valLiquidado))
        }

        BoxedColumn {
            /*Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextTitleValue("Processo", empenho.codProcesso)
                Text(
                    modifier = Modifier.clickable {
                        val openUrl = Intent(Intent.ACTION)
                    },
                    text = "Buscar processo SEI"
                )
            }*/
            TextTitleValue("Processo", empenho.codProcesso)
            TextTitleValue("Contrato nº", "${empenho.numContrato} / ${empenho.anoContrato}")
            TextTitleValue("CPF/CNPJ", empenho.numCpfCnpj)
            TextTitleValue("Razão Social", empenho.txtRazaoSocial)
        }

        BoxedColumn {
            TextTitleValue("Órgão", "${empenho.codOrgao} - ${empenho.txDescricaoOrgao}")
            TextTitleValue("Unidade",  "${empenho.codUnidade} - ${empenho.txDescricaoUnidade}")
            TextTitleValue("Empresa","${empenho.codEmpresa} - ${empenho.nomEmpresa}")
            TextTitleValue("Função", "${empenho.codFuncao} - ${empenho.txDescricaoFuncao}")
            TextTitleValue("SubFunção", "${empenho.codSubFuncao} - ${empenho.txDescricaoSubFuncao}")
            TextTitleValue("Item/Despesa", "${empenho.codItemDespesa} - ${empenho.txDescricaoItemDespesa}")
        }


        empenho.anexos?.let {anexos ->
            Text(
                fontWeight = FontWeight.Bold,
                text = "Anexos:"
            )

            for (anexo in anexos) {
                BoxedColumn {
                    Text("Unidade de medida: ${anexo.siglaUnidadeMedida}")
                    Text("Descrição: ${anexo.descricaoAnexo}")
                    Text("Quantidade anexo: ${anexo.qtdeAnexo}")
                    Text("Valor unitário anexo: ${Util.formatToCurrency(anexo.valorUnitarioAnexo)}")
                }
            }
        }
    }
}

@Composable
fun BoxedColumn(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 16.dp, 0.dp, 16.dp)
            .border(1.dp, AppColors.SuperLightGray)
            .padding(16.dp),
        content = content
    )
}
