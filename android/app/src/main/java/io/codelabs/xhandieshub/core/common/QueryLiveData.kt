package io.codelabs.xhandieshub.core.common

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import io.codelabs.xhandieshub.core.debugger

class QueryLiveData<Model>(private val query: Query, private val type: Class<out Model>) :
    LiveData<Model>(), EventListener<QuerySnapshot> {


    override fun onEvent(snapshot: QuerySnapshot?, ex: FirebaseFirestoreException?) {
        if (ex != null) {
            postValue(null)
            return
        }

        postValue(snapshot?.toObjects(type) as? Model)
    }

    override fun onActive() {
        super.onActive()

    }

    override fun onInactive() {
        super.onInactive()

    }

    private fun toDocList(snapshot: QuerySnapshot?): MutableList<out Model?> {
        if (snapshot == null) {
            debugger("Snapshot required for conversion is null. Returning empty list...")
            return mutableListOf()
        }
        val results = mutableListOf<Model?>()

        for (data in snapshot.documents) {
            results.add(data.toObject(type))
        }
        return results
    }


}