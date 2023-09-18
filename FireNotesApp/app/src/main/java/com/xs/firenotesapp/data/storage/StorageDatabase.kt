package com.xs.firenotesapp.data.storage

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.xs.firenotesapp.utils.FirebaseResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StorageDatabase @Inject constructor(
    private val fbStorage: FirebaseStorage
) {

    suspend fun uploadImage(imageUri: Uri,name:String): String{
    TODO()
    }

    suspend fun DeleteImage(url: String): FirebaseResults<String>{
        TODO()
    }

}