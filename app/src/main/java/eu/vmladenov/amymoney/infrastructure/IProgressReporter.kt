package eu.vmladenov.amymoney.infrastructure

import android.os.Handler
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.io.Closeable

interface IProgressReporter: Closeable {
    fun progress(message: String)

    val messages: Observable<String>
}

class ProgressReporter(private val handler: Handler): IProgressReporter {
    private val messagesObservable = PublishSubject.create<String>()

    override fun progress(message: String) {
        handler.post {
            messagesObservable.onNext(message)
        }
    }

    override val messages: Observable<String>
        get() = messagesObservable

    override fun close() {
        messagesObservable.onComplete()
    }
}
