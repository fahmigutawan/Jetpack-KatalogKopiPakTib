package com.mmdub.qofee.data.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mmdub.qofee.model.response.category.CategoryItem
import com.mmdub.qofee.model.response.coffee.CoffeeItem
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FirebaseSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: StorageReference
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

    fun getFirstAllCoffee(): Flow<Resource<List<CoffeeItem>>> = callbackFlow {
        trySend(Resource.Loading())
        delay(1500)
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
                val tmp = ArrayList<CoffeeItem>()
                if (it.documents.isEmpty()) {
                    trySend(Resource.Success(listOf()))
                } else {
                    it.documents.forEach { doc ->
                        firestore
                            .collection("category")
                            .document(doc.get("category_id").toString())
                            .addSnapshotListener { category, errorCategory ->
                                tmp.add(
                                    CoffeeItem(
                                        id = doc.get("id").toString(),
                                        name = doc.get("name").toString(),
                                        description = doc.get("description").toString(),
                                        category = category?.get("word").toString(),
                                        thumbnail = doc.get("thumbnail").toString(),
                                        prices = doc.get("prices") as List<Long>
                                    )
                                )

                                if (tmp.size == it.documents.size) {
                                    trySend(Resource.Success(tmp))
                                }
                            }
                    }
                }
            }
        }

        awaitClose { listener.remove() }
    }

    fun getNextAllCoffee(
        lastId: String
    ): Flow<Resource<List<CoffeeItem>>> = callbackFlow {
        trySend(Resource.Loading())
        delay(1500)
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
                            val tmp = ArrayList<CoffeeItem>()

                            if (it.documents.isEmpty()) {
                                trySend(Resource.Success(listOf()))
                            } else {
                                it.documents.forEach { doc ->
                                    firestore
                                        .collection("category")
                                        .document(doc.get("category_id").toString())
                                        .addSnapshotListener { category, errorCategory ->
                                            tmp.add(
                                                CoffeeItem(
                                                    id = doc.get("id").toString(),
                                                    name = doc.get("name").toString(),
                                                    description = doc.get("description").toString(),
                                                    category = category?.get("word").toString(),
                                                    thumbnail = doc.get("thumbnail").toString(),
                                                    prices = doc.get("prices") as List<Long>
                                                )
                                            )

                                            if (tmp.size == it.documents.size) {
                                                trySend(Resource.Success(tmp))
                                            }
                                        }
                                }
                            }
                        }
                    }
            }

        awaitClose { listener.remove() }
    }

    fun getFirstCoffeeByCategoryId(
        categoryId: String
    ): Flow<Resource<List<CoffeeItem>>> = callbackFlow {
        trySend(Resource.Loading())
        delay(1500)
        val ref = firestore
            .collection("coffee")
            .whereEqualTo("category_id", categoryId)
            .orderBy("created_at", Query.Direction.ASCENDING)
            .limit(6)

        val listener = ref.addSnapshotListener { value, error ->
            if (error != null) {
                trySend(Resource.Error(error.message ?: "Error!"))
                return@addSnapshotListener
            }

            value?.let {
                val tmp = ArrayList<CoffeeItem>()

                if (it.documents.isEmpty()) {
                    trySend(Resource.Success(listOf()))
                } else {
                    it.documents.forEach { doc ->
                        firestore
                            .collection("category")
                            .document(doc.get("category_id").toString())
                            .addSnapshotListener { category, errorCategory ->
                                tmp.add(
                                    CoffeeItem(
                                        id = doc.get("id").toString(),
                                        name = doc.get("name").toString(),
                                        description = doc.get("description").toString(),
                                        category = category?.get("word").toString(),
                                        thumbnail = doc.get("thumbnail").toString(),
                                        prices = doc.get("prices") as List<Long>
                                    )
                                )

                                if (tmp.size == it.documents.size) {
                                    trySend(Resource.Success(tmp))
                                }
                            }
                    }
                }

            }
        }

        awaitClose { listener.remove() }
    }

    fun getNextCoffeeByCategory(
        lastId: String,
        categoryId: String
    ): Flow<Resource<List<CoffeeItem>>> = callbackFlow {
        trySend(Resource.Loading())
        delay(1500)
        val listener = firestore
            .collection("coffee")
            .document(lastId)
            .addSnapshotListener { value, error ->
                firestore
                    .collection("coffee")
                    .whereEqualTo("category_id", categoryId)
                    .orderBy("created_at", Query.Direction.ASCENDING)
                    .startAfter(value)
                    .limit(6)
                    .addSnapshotListener { value2, error2 ->
                        if (error2 != null) {
                            trySend(Resource.Error(error2.message ?: "Error!"))
                            return@addSnapshotListener
                        }

                        value2?.let {
                            val tmp = ArrayList<CoffeeItem>()

                            if (it.documents.isEmpty()) {
                                trySend(Resource.Success(listOf()))
                            } else {
                                it.documents.forEach { doc ->
                                    firestore
                                        .collection("category")
                                        .document(doc.get("category_id").toString())
                                        .addSnapshotListener { category, errorCategory ->
                                            tmp.add(
                                                CoffeeItem(
                                                    id = doc.get("id").toString(),
                                                    name = doc.get("name").toString(),
                                                    description = doc.get("description").toString(),
                                                    category = category?.get("word").toString(),
                                                    thumbnail = doc.get("thumbnail").toString(),
                                                    prices = doc.get("prices") as List<Long>
                                                )
                                            )

                                            if (tmp.size == it.documents.size) {
                                                trySend(Resource.Success(tmp))
                                            }
                                        }
                                }
                            }
                        }
                    }
            }

        awaitClose { listener.remove() }
    }

    fun getCoffeeByCoffeeId(
        coffeeId: String
    ): Flow<Resource<CoffeeItem>> = callbackFlow {
        trySend(Resource.Loading())

        val listener = firestore
            .collection("coffee")
            .document(coffeeId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Error"))
                }

                value?.let { doc ->
                    firestore
                        .collection("category")
                        .document(doc.get("category_id").toString())
                        .addSnapshotListener { value2, error2 ->
                            if (error2 != null) {
                                trySend(Resource.Error(error2.message ?: "Error"))
                            }

                            value2?.let { doc2 ->

                                trySend(
                                    Resource.Success(
                                        CoffeeItem(
                                            id = doc.get("id").toString(),
                                            name = doc.get("name").toString(),
                                            description = doc.get("description").toString(),
                                            category = doc2.get("word").toString(),
                                            thumbnail = doc.get("thumbnail").toString(),
                                            prices = doc.get("prices") as List<Long>
                                        )
                                    )
                                )
                            }
                        }
                }
            }

        awaitClose { listener.remove() }
    }

    fun getAllBannerUrl(): Flow<Resource<List<String>>> = callbackFlow {
        trySend(Resource.Loading())
        delay(1500)
        storage
            .child("home-banner")
            .listAll()
            .addOnSuccessListener {
                val tmp = ArrayList<String>()

                it.items.forEach { ref ->
                    ref.downloadUrl.addOnSuccessListener { url ->
                        tmp.add(url.toString())

                        if (tmp.size == it.items.size) {
                            trySend(Resource.Success(tmp))
                        }
                    }
                }
            }.addOnFailureListener {
                trySend(Resource.Error(it.message ?: "Error"))
            }

        awaitClose()
    }
}