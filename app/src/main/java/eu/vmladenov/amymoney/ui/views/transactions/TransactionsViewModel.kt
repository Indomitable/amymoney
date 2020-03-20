package eu.vmladenov.amymoney.ui.views.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.vmladenov.amymoney.dagger.ServiceProvider
import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository
import eu.vmladenov.amymoney.ui.views.DisposableViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.functions.Function3
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.coroutines.*
import java.math.BigDecimal
import java.util.*

class TransactionsViewModel(
    private val repository: IAMyMoneyRepository,
    private val viewModelFactory: ITransactionViewModelFactory,
    filter: TransactionsFilter
) : DisposableViewModel() {
    private val filterSubject = BehaviorSubject.createDefault(filter)
    private val firstVisibleItemSubject = PublishSubject.create<Int>()
    private var totalBalanceSubject = BehaviorSubject.createDefault(BigDecimal(0.0))
    private val firstVisibleItemObservable: Observable<Int> = firstVisibleItemSubject.distinctUntilChanged { t1, t2 -> t1 == t2 }

    val transactions: Observable<List<TransactionViewModel>>
        get() {
            return Observable.combineLatest(
                    repository.transactions,
                    filterSubject,
                    BiFunction { t, f -> Pair(t, f) }
                )
                .map topMap@{ p ->
                    val transactions = p.first.values
                    return@topMap transactions
                        .filter {
                            it.postDate != null && it.postDate < GregorianCalendar().time && p.second.checkTransaction(it)
                        }
                        .map map@{
                            return@map viewModelFactory.map(it, p.second.counterAccount.id)
                        }
                        .sortedWith(compareByDescending<TransactionViewModel> { it.date }.thenByDescending { it.number } )
                }
                .doOnNext {
                    GlobalScope.launch {
                        totalBalanceSubject.onNext(getBalanceAsync(it).await())
                    }
                }
        }

    val balance: Observable<BigDecimal>
        get() {
            return Observable.combineLatest(
                    firstVisibleItemObservable,
                    transactions,
                    totalBalanceSubject,
                    Function3 { f, t, b -> Triple(f, t, b) }
                )
                .switchMap { pair ->
                    return@switchMap Observable.create<BigDecimal> { emitter ->
                        if (pair.second.isNotEmpty()) {
                            GlobalScope.launch {
                                val balance = getBalanceAsync(pair.second, pair.first).await()
                                emitter.onNext(pair.third - balance)
                                emitter.onComplete()
                            }
                        } else {
                            emitter.onNext(BigDecimal(0.0))
                            emitter.onComplete()
                        }
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
        }

    var transactionsFilter: TransactionsFilter
        get() = filterSubject.value
        set(value) {
            filterSubject.onNext(value)
        }

    fun recalculateBalance(findFirstVisibleItemPosition: Int) {
        firstVisibleItemSubject.onNext(findFirstVisibleItemPosition)
    }

    class Factory(private val filter: TransactionsFilter) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val repository = ServiceProvider.getService(IAMyMoneyRepository::class)
            val viewModelFactory = ServiceProvider.getService(ITransactionViewModelFactory::class)
            return TransactionsViewModel(repository, viewModelFactory, filter) as T
        }
    }

    private suspend fun getBalanceAsync(transactions: Iterable<TransactionViewModel>, take: Int): Deferred<BigDecimal> =
        coroutineScope {
            return@coroutineScope async {
                return@async transactions
                    .take(take)
                    .fold(BigDecimal(0.0)) { acc: BigDecimal, transaction: TransactionViewModel ->
                        return@fold acc + transaction.value
                    }
            }
        }

    private suspend fun getBalanceAsync(transactions: Iterable<TransactionViewModel>): Deferred<BigDecimal> =
        coroutineScope {
            return@coroutineScope async {
                return@async transactions
                    .fold(BigDecimal(0.0)) { acc: BigDecimal, transaction: TransactionViewModel ->
                        return@fold acc + transaction.value
                    }
            }
        }


}
