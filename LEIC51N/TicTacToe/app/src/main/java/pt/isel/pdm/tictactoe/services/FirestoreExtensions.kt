package pt.isel.pdm.tictactoe.services

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun <T> DocumentReference.waitForDocumentToChange(predicate: (DocumentSnapshot?) -> T): T {
    val doc = this
    return suspendCancellableCoroutine { continuation ->
        var listener: ListenerRegistration? = null
        listener = doc.addSnapshotListener { docSnapshot, err ->
            try {
                if (err != null)
                    continuation.resumeWithException(Exception(err.message))
                else {
                    val ret = predicate(docSnapshot)
                    if (ret != null)
                        continuation.resume(ret)
                }

            } catch (e: Exception) {
                continuation.resumeWithException(e)
            } finally {
                if (continuation.isCompleted)
                    listener!!.remove()
            }
        }

        continuation.invokeOnCancellation {
            listener.remove()
        }
    }

}
