package org.cidadeape.monitoraorcamento.common

import kotlinx.browser.window

actual fun showMessage(message: String) {
    window.alert(message)
}