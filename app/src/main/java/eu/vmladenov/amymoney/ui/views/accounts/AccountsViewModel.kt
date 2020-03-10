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
import io.reactivex.rxjava3.subjects.BehaviorSubject

class AccountsViewModelFactory(private val initialInstitutionId: String) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val repository = ServiceProvider.getService(IAMyMoneyRepository::class)
        return AccountsViewModel(repository, initialInstitutionId) as T
    }
}

class AccountsViewModel(private val repository: IAMyMoneyRepository, private val initialInstitutionId: String) : DisposableViewModel() {
    private val selectedInstitutionSubject: BehaviorSubject<Institution?> = BehaviorSubject.create<Institution?>()
    private val emptyInstitution: Institution = Institution(name = "Accounts with no institution assigned")

    init {
        if (repository.institutions.value.size > 0) {
            val firstInstitution = if (initialInstitutionId.isEmpty()) emptyInstitution else repository.institutions.value.find { it -> it.id == initialInstitutionId }
            if (firstInstitution != null) {
                selectedInstitutionSubject.onNext(firstInstitution)
            }
        }
    }

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
                selectedInstitutionSubject,
                BiFunction { a: Accounts, b: Institution? -> Pair(a, b) }
            )
                .map { pair ->
                val selectedInstitution = pair.second
                val userAccounts = pair.first.getUserAccounts()
                return@map sequence<Account> {
                    if (selectedInstitution != null) {
                        if (selectedInstitution != emptyInstitution) {
                            for (account in userAccounts.filter { a -> a.institutionId == selectedInstitution.id }) {
                                yield(account)
                            }
                        } else {
                            for (account in userAccounts.filter { it.institutionId.isEmpty() }) {
                                yield(account)
                            }
                        }
                    }
                }
            }
        }

    var selectedInstitution: Institution?
        get() = selectedInstitutionSubject.value
        set(value) {
            if (value == null || (selectedInstitutionSubject.hasValue() && selectedInstitutionSubject.value!!.id == value.id)) {
                return
            }
            selectedInstitutionSubject.onNext(value)
        }
}
