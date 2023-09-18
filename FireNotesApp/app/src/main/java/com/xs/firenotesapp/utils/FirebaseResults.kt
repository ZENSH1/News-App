package com.xs.firenotesapp.utils

import java.lang.Exception


//these will be returned to the viewModel.
sealed class FirebaseResults<out R> {
    data class Success<out T>(val result: T): FirebaseResults<T>()
    data class Failure(val exception: Exception): FirebaseResults<Nothing>()
    object Loading: FirebaseResults<Nothing>()
}