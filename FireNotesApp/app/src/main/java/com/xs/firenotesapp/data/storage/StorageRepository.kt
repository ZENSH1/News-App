package com.xs.firenotesapp.data.storage

interface StorageRepository {

    suspend fun uploadImage():String

    suspend fun DeleteImage():String
}