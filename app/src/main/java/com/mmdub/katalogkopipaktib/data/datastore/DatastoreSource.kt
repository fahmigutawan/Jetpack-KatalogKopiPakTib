package com.mmdub.katalogkopipaktib.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import javax.inject.Inject

val Context._datastore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DatastoreSource @Inject constructor(
    context: Context
) {
    val datastore = context._datastore
}