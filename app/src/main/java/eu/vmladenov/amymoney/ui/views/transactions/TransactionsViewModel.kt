package eu.vmladenov.amymoney.ui.views.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.vmladenov.amymoney.dagger.ServiceProvider
import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository
import eu.vmladenov.amymoney.ui.views.DisposableViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import kotlinx.coroutines.*
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

class TransactionsViewModelFactory(private val filter: TransactionsFilter) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val repository = ServiceProvider.getService(IAMyMoneyRepository::class)
        val viewModelFactory = ServiceProvider.getService(ITransactionViewModelFactory::class)
        return TransactionsViewModel(repository, viewModelFactory, filter) as T
    }
}

class TransactionsViewModel(
    private val repository: IAMyMoneyRepository,
    private val viewModelFactory: ITransactionViewModelFactory,
    filter: TransactionsFilter
) : DisposableViewModel() {
    private val filterSubject = BehaviorSubject.createDefault(filter)
    private val formatters = mutableMapOf<String, NumberFormat>()
    private val firstVisibleItemSubject = PublishSubject.create<Int>()

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
                        .filter { p.second.checkTransaction(it) }
                        .sortedByDescending { t -> t.postDate }
                        .map map@{
                            return@map viewModelFactory.map(it, p.second.counterAccount.id)
                        }
                }
        }

    val totalBalance: Observable<BigDecimal>
        get() {
            return transactions
                .switchMap { transactions ->
                    return@switchMap Observable.create<BigDecimal> { emitter ->
                        if (transactions.isNotEmpty()) {
                            GlobalScope.launch {
                                val balance = getBalanceAsync(transactionsFilter.counterAccount.id, transactions, 0).await()
//                                val formattedBalance = getFormatter().format(balance)
                                withContext(Dispatchers.Main) {
                                    emitter.onNext(balance)
                                }
                                emitter.onComplete()
                            }
                        } else {
                            emitter.onNext(BigDecimal(0.0))
                            emitter.onComplete()
                        }
                    }
                }
        }

    val balance: Observable<BigDecimal>
        get() {
            return Observable.combineLatest(
                    firstVisibleItemSubject,
                    transactions,
                    BiFunction { f, t -> Pair(f, t) }
                )
                .switchMap { pair ->
                    return@switchMap Observable.create<BigDecimal> { emitter ->
                        if (pair.second.isNotEmpty()) {
                            GlobalScope.launch {
                                val balance = getBalanceAsync(transactionsFilter.counterAccount.id, pair.second, pair.first).await()
//                                val formattedBalance = getFormatter().format(balance)
                                withContext(Dispatchers.Main) {
                                    emitter.onNext(balance)
                                }
                                emitter.onComplete()
                            }
                        } else {
                            emitter.onNext(BigDecimal(0.0))
                            emitter.onComplete()
                        }
                    }
                }
        }

    var transactionsFilter: TransactionsFilter
        get() = filterSubject.value
        set(value) {
            filterSubject.onNext(value)
        }

    private suspend fun getBalanceAsync(accountId: String, transactions: Iterable<TransactionViewModel>, skip: Int): Deferred<BigDecimal> =
        coroutineScope {
            return@coroutineScope async {
                return@async transactions
                    .filter { t -> t.date!! < GregorianCalendar().time }
                    .drop(skip)
                    .fold(BigDecimal(0.0)) { acc: BigDecimal, transaction: TransactionViewModel ->
                        return@fold acc + transaction.value
                    }
            }
        }

    private fun getFormatter(): NumberFormat {
        return formatters.getOrPut(transactionsFilter.counterAccount.currencyId, {
            return@getOrPut NumberFormat.getCurrencyInstance().also {
                it.currency = Currency.getInstance(transactionsFilter.counterAccount.currencyId)
            }
        })
    }

    fun recalculateBalance(findFirstVisibleItemPosition: Int) {
        firstVisibleItemSubject.onNext(findFirstVisibleItemPosition)
    }
}
