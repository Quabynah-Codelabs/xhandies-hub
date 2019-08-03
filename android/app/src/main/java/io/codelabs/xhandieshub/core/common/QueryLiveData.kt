package io.codelabs.xhandieshub.core.common

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.*
import io.codelabs.xhandieshub.core.debugger

class QueryLiveData<Model>(private val query: Query, private val type: Class<Model>) :
    LiveData<MutableList<Model?>>(), EventListener<QuerySnapshot> {
    private var listener: ListenerRegistration? = null

    override fun onEvent(snapshot: QuerySnapshot?, ex: FirebaseFirestoreException?) {
        if (ex != null) {
            postValue(null)
            return
        }

        postValue(toDocList(snapshot))
    }

    override fun onActive() {
        super.onActive()
        listener = query.addSnapshotListener(this)
    }

    override fun onInactive() {
        super.onInactive()
        listener?.remove()
        listener = null
    }

    private fun toDocList(snapshot: QuerySnapshot?): MutableList<Model?> {
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