package com.mmdub.qofee.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.mmdub.qofee.model.response.category.CategoryItem
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FirebaseSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    fun getAllCategory(): Flow<Resource<List<CategoryItem?>>> = callbackFlow {
        trySend(Resource.Loading())

        val ref = firestore.collection("category")

        val listener = ref.addSnapshotListener { value, error ->
            if (error != null) {
                trySend(Resource.Error(error.message ?: "Error!"))
                return@addSnapshotListener
            }

            value?.let {
                val tmp = it.documents.map { doc -> doc.toObject(CategoryItem::class.java) }
                trySend(Resource.Success(tmp))
            }
        }

        awaitClose { listener.remove() }
    }
}