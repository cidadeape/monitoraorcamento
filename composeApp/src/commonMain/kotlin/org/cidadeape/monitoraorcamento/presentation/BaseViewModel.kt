package org.cidadeape.monitoraorcamento.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.cidadeape.monitoraorcamento.common.Logger

open class BaseViewModel: ViewModel() {

    private val jobs = ArrayList<Job>()

    protected fun launchCoroutine(block: suspend  () -> Unit): Job {
        val job = viewModelScope.launch(Dispatchers.Default) {
            block.invoke()
        }
        jobs.add(job)
        return job
    }

    protected fun cancel() {
        for (job in jobs) job.cancel()
        Logger.i("App", "BaseViewModel cancel()")
    }

    override fun onCleared() {
        super.onCleared()
        cancel()
    }
}
