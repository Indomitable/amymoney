package eu.vmladenov.amymoney.ui.views.payees

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.vmladenov.amymoney.dagger.ServiceProvider
import eu.vmladenov.amymoney.infrastructure.IAMyMoneyRepository
import eu.vmladenov.amymoney.models.Payee
import io.reactivex.rxjava3.core.Observable

class PayeesViewModel(private val repository: IAMyMoneyRepository) : ViewModel() {
    val payees: Observable<List<Payee>>
        get() {
            return repository.payees.map {
                it.values.sortedBy { p -> p.name }
            }
        }

    class Factory: ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val repository = ServiceProvider.getService(IAMyMoneyRepository::class)
            return PayeesViewModel(repository) as T
        }
    }
}
