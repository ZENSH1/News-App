package com.xs.firenotesapp.data.storage

import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    storageDb: StorageDatabase
): StorageRepository {
    override suspend fun uploadImage(): String {
        TODO("Not yet implemented")
    }

    override suspend fun DeleteImage(): String {
        TODO("Not yet implemented")
    }
}