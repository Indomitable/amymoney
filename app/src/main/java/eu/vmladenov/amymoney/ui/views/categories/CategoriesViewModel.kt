package eu.vmladenov.amymoney.ui.views.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.vmladenov.amymoney.dagger.ServiceProvider
import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository
import eu.vmladenov.amymoney.models.Accounts
import eu.vmladenov.amymoney.ui.views.payees.PayeesViewModel
import io.reactivex.rxjava3.core.Observable

class CategoriesViewModel(private val repository: IAMyMoneyRepository) : ViewModel() {
    val payees: Observable<Accounts>
        get() {
            return repository.accounts
        }

    class Factory: ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val repository = ServiceProvider.getService(IAMyMoneyRepository::class)
            return PayeesViewModel(repository) as T
        }
    }
}
