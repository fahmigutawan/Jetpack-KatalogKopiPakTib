package com.mmdub.qofee.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.StorageReference
import com.mmdub.qofee.model.response.category.CategoryItem
import com.mmdub.qofee.model.response.coffee.CoffeeItem
import com.mmdub.qofee.model.response.seller.SellerResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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

    fun getFirstCoffee(
        uid: String = "",
        category_id: String = ""
    ): Flow<Resource<List<CoffeeItem>>> = callbackFlow {
        trySend(Resource.Loading())
        delay(1500)
        var ref = firestore
            .collection("coffee")
            .orderBy("created_at", Query.Direction.DESCENDING)
            .limit(6)

        if (uid.isNotEmpty()) {
            ref = ref.whereEqualTo("admin_uid", uid)
        }

        if (category_id.isNotEmpty()) {
            ref = ref.whereEqualTo("category_id", category_id)
        }

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
                                        prices = doc.get("prices") as List<Map<String, Long>>,
                                        admin_uid = doc.get("admin_uid").toString()
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

    fun getNextCoffee(
        lastId: String,
        uid: String = "",
        category_id: String = ""
    ): Flow<Resource<List<CoffeeItem>>> = callbackFlow {
        trySend(Resource.Loading())
        delay(1500)
        val listener = firestore
            .collection("coffee")
            .document(lastId)
            .addSnapshotListener { value, error ->
                var ref = firestore
                    .collection("coffee")
                    .orderBy("created_at", Query.Direction.DESCENDING)
                    .startAfter(value?.get("created_at"))
                    .limit(6)

                if (uid.isNotEmpty()) {
                    ref = ref.whereEqualTo("admin_uid", uid)
                }

                if (category_id.isNotEmpty()) {
                    ref = ref.whereEqualTo("category_id", category_id)
                }

                ref.addSnapshotListener { value2, error2 ->
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
                                                prices = doc.get("prices") as List<Map<String, Long>>,
                                                admin_uid = doc.get("admin_uid").toString()
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
                                            prices = doc.get("prices") as List<Map<String, Long>>,
                                            admin_uid = doc.get("admin_uid").toString()
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

        val listener = firestore
            .collection("home-banner")
            .orderBy("created_at", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Error"))
                    return@addSnapshotListener
                }

                value?.let {
                    trySend(
                        Resource.Success(
                            it.documents.map { it.get("url").toString() }
                        )
                    )
                }
            }

        awaitClose {
            listener.remove()
        }
    }

    fun getSellerDetail(
        uid: String
    ): Flow<Resource<SellerResponse?>> = callbackFlow {
        trySend(Resource.Loading())

        val listener = firestore
            .collection("seller")
            .document(uid)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Error"))
                    return@addSnapshotListener
                }

                value?.let {
                    trySend(Resource.Success(it.toObject(SellerResponse::class.java)))
                }
            }

        awaitClose { listener.remove() }
    }

    fun getAllSeller(): Flow<Resource<List<SellerResponse?>>> = callbackFlow {
        val listener = firestore
            .collection("seller")
            .orderBy("name", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Error"))
                    return@addSnapshotListener
                }

                value?.let {
                    trySend(
                        Resource.Success(
                            it.documents.map {
                                it.toObject(SellerResponse::class.java)
                            }
                        )
                    )
                }
            }

        awaitClose { listener.remove() }
    }
}