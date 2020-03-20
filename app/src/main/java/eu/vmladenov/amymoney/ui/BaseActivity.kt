package eu.vmladenov.amymoney.ui

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.SingleSubject

abstract class BaseActivity protected constructor(): AppCompatActivity() {
    private val liveCycleSubject: SingleSubject<Boolean> = SingleSubject.create<Boolean>()
    protected val destroyNotifier: Observable<Boolean> = liveCycleSubject.toObservable()

    override fun onDestroy() {
        liveCycleSubject.onSuccess(true)
        super.onDestroy()
    }
}
