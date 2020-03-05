package eu.vmladenov.amymoney.ui.views

import androidx.fragment.app.Fragment
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.SingleSubject

open class DisposableFragment: Fragment() {
    private val liveCycleSubject: SingleSubject<Boolean> = SingleSubject.create<Boolean>()
    protected val destroyNotifier: Observable<Boolean> = liveCycleSubject.toObservable()

    override fun onDestroy() {
        liveCycleSubject.onSuccess(true)
        super.onDestroy()
    }
}
