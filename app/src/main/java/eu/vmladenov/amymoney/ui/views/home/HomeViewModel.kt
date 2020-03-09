package eu.vmladenov.amymoney.ui.views.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.vmladenov.amymoney.dagger.ServiceProvider
import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository
import eu.vmladenov.amymoney.ui.views.DisposableViewModel
import io.reactivex.rxjava3.core.Observable

class HomeViewModelFactory() : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val repository = ServiceProvider.getService(IAMyMoneyRepository::class)
        return HomeViewModel(repository) as T
    }
}

class HomeViewModel(val repository: IAMyMoneyRepository) : DisposableViewModel() {
    val isDataLoaded: Observable<Boolean>
        get() = repository.accounts.map {
            it.size > 0
        }
}
