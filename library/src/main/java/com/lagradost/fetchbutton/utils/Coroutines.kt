package com.lagradost.fetchbutton.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object Coroutines {
    fun ioThread(work: suspend (() -> Unit)): Job {
        return CoroutineScope(Dispatchers.IO).launch {
            work()
        }
    }

    fun mainThread(work: suspend (() -> Unit)): Job {
        return CoroutineScope(Dispatchers.Main).launch {
            work()
        }
    }
}