package eu.vmladenov.amymoney.ui.views

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.subjects.PublishSubject

open class DisposableViewModel: ViewModel() {
    protected val liveCycleSubject: PublishSubject<Boolean> = PublishSubject.create<Boolean>()

    override fun onCleared() {
        liveCycleSubject.onNext(true)
        liveCycleSubject.onComplete()
        super.onCleared()
    }
}
