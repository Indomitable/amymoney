package eu.vmladenov.amymoney.ui.views.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.vmladenov.amymoney.dagger.ServiceProvider
import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository
import eu.vmladenov.amymoney.models.Account
import eu.vmladenov.amymoney.models.Accounts
import eu.vmladenov.amymoney.models.Institution
import eu.vmladenov.amymoney.ui.views.DisposableViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import kotlinx.coroutines.yield

class AccountsViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val repository = ServiceProvider.getService(IAMyMoneyRepository::class)
        return AccountsViewModel(repository) as T
    }
}

class AccountsViewModel(private val repository: IAMyMoneyRepository) : DisposableViewModel() {
    val selectedInstitution: Subject<Institution?> = PublishSubject.create<Institution?>()
    private val emptyInstitution: Institution = Institution(name = "Accounts with no institution assigned")

    val institutions: Observable<Sequence<Institution>>
        get() {
            return repository.institutions.map { institutions ->
                return@map sequence {
                    yield(emptyInstitution)
                    yieldAll(institutions)
                }
            }
        }


    val accounts: Observable<Sequence<Account>>
        get() {
            return Observable.combineLatest(
                repository.accounts,
                selectedInstitution,
                BiFunction { a: Accounts, b: Institution? -> Pair(a, b) }
            ).map { pair ->
                val selectedInstitution = pair.second
                return@map sequence<Account> {
                    if (selectedInstitution != null) {
                        if (selectedInstitution != emptyInstitution) {
                            for (accountId in selectedInstitution.accountIds) {
                                val account = pair.first[accountId]
                                if (account != null) {
                                    yield(account)
                                }
                            }
                        } else {
                            for (account in repository.accounts.value.getUserAccounts().filter { it.institutionId.isEmpty() }) {
                                yield(account)
                            }
                        }
                    }
                }
            }
        }

    init {
        if (repository.institutions.value.size > 0) {
            val firstInstitution = repository.institutions.value.first()
            selectedInstitution.onNext(firstInstitution)
        } else {
            selectedInstitution.onNext(null)
        }
    }

    fun selectInstitution(institution: Institution?) {
        selectedInstitution.onNext(institution)
    }
}
