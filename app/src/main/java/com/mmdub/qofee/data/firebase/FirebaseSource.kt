package com.mmdub.qofee.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mmdub.qofee.model.response.category.CategoryItem
import com.mmdub.qofee.model.response.coffee.CoffeeItem
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

    fun getFirstAllCoffee(): Flow<Resource<List<CoffeeItem?>>> = callbackFlow {
        trySend(Resource.Loading())

        val ref = firestore
            .collection("coffee")
            .orderBy("created_at", Query.Direction.ASCENDING)
            .limit(6)

        val listener = ref.addSnapshotListener { value, error ->
            if (error != null) {
                trySend(Resource.Error(error.message ?: "Error!"))
                return@addSnapshotListener
            }

            value?.let {
                val tmp = it.documents.map { doc -> doc.toObject(CoffeeItem::class.java) }
                trySend(Resource.Success(tmp))
            }
        }

        awaitClose { listener.remove() }
    }

    fun getNextAllCoffee(
        lastId: String
    ): Flow<Resource<List<CoffeeItem?>>> = callbackFlow {
        trySend(Resource.Loading())

        val listener = firestore
            .collection("coffee")
            .document(lastId)
            .addSnapshotListener { value, error ->
                firestore
                    .collection("coffee")
                    .orderBy("created_at", Query.Direction.ASCENDING)
                    .startAfter(value)
                    .limit(6)
                    .addSnapshotListener { value2, error2 ->
                        if (error2 != null) {
                            trySend(Resource.Error(error2.message ?: "Error!"))
                            return@addSnapshotListener
                        }

                        value2?.let {
                            val tmp = it.documents.map { doc -> doc.toObject(CoffeeItem::class.java) }
                            trySend(Resource.Success(tmp))
                        }
                    }
            }

        awaitClose { listener.remove() }
    }
}