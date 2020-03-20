package eu.vmladenov.amymoney.ui.views

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.SingleSubject

open class DisposableViewModel: ViewModel() {
    private val liveCycleSubject: SingleSubject<Boolean> = SingleSubject.create<Boolean>()
    protected val destroyNotifier: Observable<Boolean> = liveCycleSubject.toObservable()

    override fun onCleared() {
        liveCycleSubject.onSuccess(true)
        super.onCleared()
    }
}
