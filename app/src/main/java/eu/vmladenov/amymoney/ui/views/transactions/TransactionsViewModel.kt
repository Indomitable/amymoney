package eu.vmladenov.amymoney.ui.views.transactions

import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository
import eu.vmladenov.amymoney.models.Transaction
import eu.vmladenov.amymoney.ui.views.DisposableViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.subjects.BehaviorSubject

class TransactionsViewModel(
    private val repository: IAMyMoneyRepository,
    filter: TransactionsFilter
) : DisposableViewModel() {
    private val filterSubject = BehaviorSubject.createDefault(filter)

    val transactions: Observable<Iterable<Transaction>>
        get() {
            return Observable.combineLatest(
                    repository.transactions,
                    filterSubject,
                    BiFunction { t, f -> Pair(t, f) }
                )
                .map { p ->
                    val transactions = p.first.values
                    return@map transactions.filter { p.second.checkTransaction(it) }
                }
        }

    var transactionsFilter: TransactionsFilter
        get() = filterSubject.value
        set(value) {
            filterSubject.onNext(value)
        }
}
