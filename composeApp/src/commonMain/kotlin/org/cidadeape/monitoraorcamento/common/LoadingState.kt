package org.cidadeape.monitoraorcamento.common

sealed class LoadingState <T> {

    class NotStarted<T>: LoadingState<T>()
    class Loading<T>: LoadingState<T>()
    class Success<T>(val response: T): LoadingState<T>()
    class Failure<T>(val message: String): LoadingState<T>()

}
