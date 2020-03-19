package eu.vmladenov.amymoney.ui.views.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.vmladenov.amymoney.dagger.ServiceProvider
import eu.vmladenov.amymoney.infrastructure.Fraction
import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository
import eu.vmladenov.amymoney.models.*
import eu.vmladenov.amymoney.ui.views.DisposableViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import java.util.*

data class AccountBalance(val account: Account, val balance: Fraction)

class HomeViewModel(val repository: IAMyMoneyRepository) : DisposableViewModel() {
    private val assetAccounts: Observable<List<Account>> = repository.accounts.map { m -> m.getAccountsForType(AccountStandardType.Asset) }
    private val liabilityAccounts: Observable<List<Account>> = repository.accounts.map { m -> m.getAccountsForType(AccountStandardType.Liability) }

    val isDataLoaded: Observable<Boolean>
        get() = repository.accounts.map { a -> a.any() }

    val assetAccountsBalance: Observable<Sequence<AccountBalance>> =
        Observable.combineLatest(
                repository.transactions,
                assetAccounts,
                BiFunction { a: Transactions, b: List<Account> -> Pair(a, b) }
            )
            .map { getAccountsBalance(it) }

    val liabilityAccountsBalance: Observable<Sequence<AccountBalance>> =
        Observable.combineLatest(
                repository.transactions,
                liabilityAccounts,
                BiFunction { a: Transactions, b: List<Account> -> Pair(a, b) }
            )
            .map { getAccountsBalance(it) }

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val repository = ServiceProvider.getService(IAMyMoneyRepository::class)
            return HomeViewModel(repository) as T
        }
    }

    private fun getAccountsBalance(pair: Pair<Transactions, List<Account>>): Sequence<AccountBalance> {
        val transactions = pair.first
        val index = transactions.accountsIndex
        val accounts = pair.second
        return sequence<AccountBalance> {
            for (account in accounts) {
                val transactionIds = index[account.id] ?: emptySet()
                val balance = getBalance(account, transactionIds.mapNotNull { id -> transactions[id] })
                yield(AccountBalance(account, balance))
            }
        }
    }

    private fun getBalance(account: Account, transactions: List<Transaction>): Fraction {
        return transactions
            .filter { t -> t.postDate!! < GregorianCalendar().time }
            .fold(Fraction(0, 1)) { acc: Fraction, transaction: Transaction ->
                val split = transaction.splits.find { it.accountId == account.id }!!
                val res = (acc + split.value) // We need to simplify because during the process the denominator is growing too much and creates overflow
                if (res.denominator == 0L) {
                    throw Exception("Denominator is zero")
                }
                return@fold res
            }
    }
}
