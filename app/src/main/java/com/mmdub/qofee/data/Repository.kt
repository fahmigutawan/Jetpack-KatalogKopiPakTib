package com.mmdub.qofee.data

import com.mmdub.qofee.data.datastore.DatastoreSource
import com.mmdub.qofee.data.firebase.FirebaseSource
import javax.inject.Inject

class Repository @Inject constructor(
    private val firebaseSource: FirebaseSource,
    private val datastoreSource: DatastoreSource
) {
}