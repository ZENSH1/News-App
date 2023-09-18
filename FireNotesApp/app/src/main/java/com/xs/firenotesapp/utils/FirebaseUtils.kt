package com.xs.firenotesapp.utils

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


//just trying to understand how await function works
//this is more or less it.
suspend fun <T> Task<T>.await():T{
    return suspendCancellableCoroutine {cont->
        addOnCompleteListener {
            if (it.exception != null){
                cont.resumeWithException(it.exception!!)
            }else{
                cont.resume(it.result)
            }
        }
    }
}