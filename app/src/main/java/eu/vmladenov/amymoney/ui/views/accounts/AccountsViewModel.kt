package eu.vmladenov.amymoney.ui.views.accounts

import androidx.lifecycle.*
import eu.vmladenov.amymoney.dagger.ServiceProvider
import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository
import eu.vmladenov.amymoney.models.Account
import eu.vmladenov.amymoney.models.Accounts
import eu.vmladenov.amymoney.models.Institution
import eu.vmladenov.amymoney.models.Institutions
import eu.vmladenov.amymoney.ui.views.DisposableViewModel
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject

class AccountsViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val repository = ServiceProvider.getService(IAMyMoneyRepository::class)
        return AccountsViewModel(repository) as T
    }
}

class AccountsViewModel(private val repository: IAMyMoneyRepository) : DisposableViewModel() {
    // private val institutionId: Subject<String> = PublishSubject.create<String>()
    val selectedInstitution: Subject<Institution> = PublishSubject.create<Institution>()
    val institutions: Observable<Institutions>
        get() {
            return repository.institutions
        }


    val accounts: Observable<List<Account>>
        get() {
            return Observable.combineLatest(
                repository.accounts,
                selectedInstitution,
                BiFunction { a: Accounts, b: Institution -> Pair(a, b) }
            ).map { pair ->
                return@map pair.first.filter { a ->
                    pair.second.accountIds.any { id -> a.id == id }
                }
            }
        }

    init {
        selectedInstitution.onNext()
    }



//    val accounts: LiveData<Accounts>
//        get() {
//
//            return LiveDataReactiveStreams.fromPublisher(
//                repository.accounts.filter { accounts ->
//                    if (institutionId == "") {
//                        return@filter true
//                    } else {
//                        val institution = repository.institutions.value.find { i -> i.id == institutionId }!!
//                        return@filter institution.accountIds.any { id -> accounts.any { a -> a.id == id } }
//                    }
//                }
//                    .toFlowable(BackpressureStrategy.LATEST)
//            )
//        }
}
