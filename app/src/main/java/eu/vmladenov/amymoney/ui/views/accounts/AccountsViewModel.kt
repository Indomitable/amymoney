package eu.vmladenov.amymoney.ui.views.accounts

import android.accounts.Account
import androidx.lifecycle.*
import eu.vmladenov.amymoney.dagger.ServiceProvider
import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository
import eu.vmladenov.amymoney.models.Accounts
import eu.vmladenov.amymoney.ui.views.DisposableViewModel
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

class AccountsViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val repository = ServiceProvider.getService(IAMyMoneyRepository::class)
        return AccountsViewModel(repository) as T
    }
}

class AccountsViewModel (private val repository: IAMyMoneyRepository): DisposableViewModel() {
    val mutableList: MutableLiveData<Accounts> = MutableLiveData()
    var institutionId: String = ""

    init {
        repository.accounts
            .takeUntil(liveCycleSubject)
            .subscribe(object: Observer<Accounts> {
                override fun onNext(t: Accounts?) {
                    mutableList.value = t
                }

                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable?) {
                }

                override fun onError(e: Throwable?) {
                }
            })
    }

    val accounts: LiveData<Accounts>
        get() {

            return LiveDataReactiveStreams.fromPublisher(
                repository.accounts.filter { accounts ->
                    if (institutionId == "") {
                        return@filter true
                    } else {
                        val institution = repository.institutions.value.find { i -> i.id == institutionId }!!
                        return@filter institution.accountIds.any { id -> accounts.any { a -> a.id == id } }
                    }
                }
                    .toFlowable(BackpressureStrategy.LATEST)
            )
        }
}
