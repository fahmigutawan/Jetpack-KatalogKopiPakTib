package com.mmdub.qofee.data

import com.mmdub.qofee.data.datastore.DatastoreSource
import com.mmdub.qofee.data.firebase.FirebaseSource
import javax.inject.Inject

class Repository @Inject constructor(
    private val firebaseSource: FirebaseSource,
    private val datastoreSource: DatastoreSource
) {
    fun getAllCategory() = firebaseSource.getAllCategory()

    fun getFirstCoffee(uid: String = "", category_id: String = "") =
        firebaseSource.getFirstCoffee(uid, category_id)

    fun getNextCoffee(lastId: String, uid: String = "", category_id: String = "") =
        firebaseSource.getNextCoffee(lastId, uid, category_id)

    fun getCoffeeByCoffeeId(coffeeId: String) = firebaseSource.getCoffeeByCoffeeId(coffeeId)

    fun getAllBannerUrl() = firebaseSource.getAllBannerUrl()

    fun getSellerDetail(uid: String) = firebaseSource.getSellerDetail(uid)

    fun getAllSeller() = firebaseSource.getAllSeller()
}