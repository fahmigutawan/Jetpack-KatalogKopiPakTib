package com.mmdub.katalogkopipaktib.data

import com.mmdub.katalogkopipaktib.data.datastore.DatastoreSource
import com.mmdub.katalogkopipaktib.data.firebase.FirebaseSource
import javax.inject.Inject

class Repository @Inject constructor(
    private val firebaseSource: FirebaseSource,
    private val datastoreSource: DatastoreSource
) {
}