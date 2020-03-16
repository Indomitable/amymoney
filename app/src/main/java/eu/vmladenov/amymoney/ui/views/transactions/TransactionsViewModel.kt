package eu.vmladenov.amymoney.ui.views.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.vmladenov.amymoney.dagger.ServiceProvider
import eu.vmladenov.amymoney.infrastructure.Fraction
import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository
import eu.vmladenov.amymoney.models.Account
import eu.vmladenov.amymoney.models.Transaction
import eu.vmladenov.amymoney.ui.views.DisposableViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.*
import java.text.NumberFormat
import java.util.*

class TransactionsViewModelFactory(private val filter: TransactionsFilter) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val repository = ServiceProvider.getService(IAMyMoneyRepository::class)
        return TransactionsViewModel(repository, filter) as T
    }
}

class TransactionsViewModel(
    private val repository: IAMyMoneyRepository,
    filter: TransactionsFilter
) : DisposableViewModel() {
    private val filterSubject = BehaviorSubject.createDefault(filter)

    val transactions: Observable<List<Transaction>>
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

    val balance: Observable<String>
        get() {
            return transactions
                .switchMap { transactions ->
                    return@switchMap Observable.create<String> { emitter ->
                        if (transactions.isNotEmpty()) {
                            val format: NumberFormat = NumberFormat.getCurrencyInstance()
                            format.currency = Currency.getInstance(transactions[0].commodity)
                            GlobalScope.launch {
                                val balance = getBalanceAsync(transactionsFilter.counterAccountId, transactions).await().toDecimal().toDouble()
                                val formattedBalance = format.format(balance)
                                withContext(Dispatchers.Main) {
                                    emitter.onNext(formattedBalance)
                                }
                                emitter.onComplete()
                            }
                        } else {
                            emitter.onNext("0")
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

    private suspend fun getBalanceAsync(accountId: String, transactions: Iterable<Transaction>): Deferred<Fraction> = coroutineScope {
        return@coroutineScope async {
            return@async transactions
                .filter { t -> t.postDate!! < GregorianCalendar().time }
                .fold(Fraction(0, 1)) { acc: Fraction, transaction: Transaction ->
                    val split = transaction.splits.find { it.accountId == accountId }!!
                    val res =
                        (acc + split.value) // We need to simplify because during the process the denominator is growing too much and creates overflow
                    if (res.denominator == 0L) {
                        throw Exception("Denominator is zero")
                    }
                    return@fold res
                }
        }
    }
}
